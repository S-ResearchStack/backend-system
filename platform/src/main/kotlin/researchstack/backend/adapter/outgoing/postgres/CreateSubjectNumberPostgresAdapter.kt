package researchstack.backend.adapter.outgoing.postgres

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.switchIfEmpty
import researchstack.backend.adapter.outgoing.postgres.CommonSubjectNumberPostgresQuery.isAlreadyParticipated
import researchstack.backend.adapter.outgoing.postgres.CommonSubjectNumberPostgresQuery.toDomainException
import researchstack.backend.adapter.outgoing.postgres.CommonSubjectNumberPostgresQuery.toNamedParameter
import researchstack.backend.application.exception.AlreadyExistsException
import researchstack.backend.application.port.outgoing.study.CreateSubjectNumberOutPort

private const val INSERT_QUERY = """
    INSERT INTO study_joins(subject_id, study_id, subject_number)
    VALUES (:subjectId,:studyId, nextval('%s'))
    RETURNING subject_number
"""

@Component
class CreateSubjectNumberPostgresAdapter(
    @Qualifier("postgresNamedParameterJdbcTemplate")
    private val jdbcTemplate: NamedParameterJdbcTemplate
) : CreateSubjectNumberOutPort {

    override suspend fun createSubjectNumber(studyId: String, subjectId: String): String =
        Mono.fromCallable {
            isAlreadyParticipated(studyId, subjectId, jdbcTemplate)
        }.filter { !it }
            .switchIfEmpty { Mono.error(AlreadyExistsException("already participated study")) }
            .map {
                insert(studyId, subjectId)
            }.onErrorMap { ex ->
                toDomainException(ex)
            }
            .subscribeOn(Schedulers.boundedElastic())
            .awaitSingle()

    private fun insert(studyId: String, subjectId: String) = jdbcTemplate.queryForObject(
        INSERT_QUERY.format(studyId),
        toNamedParameter(studyId, subjectId),
        String::class.java
    ) ?: throw EmptyResultDataAccessException(1)
}
