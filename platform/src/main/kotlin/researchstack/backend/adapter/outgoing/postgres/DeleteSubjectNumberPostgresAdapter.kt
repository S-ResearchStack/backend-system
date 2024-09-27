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
import researchstack.backend.application.exception.NotFoundException
import researchstack.backend.application.port.outgoing.study.DeleteSubjectNumberOutPort

private const val DELETE_QUERY = """
            DELETE FROM study_joins
            WHERE subject_id = :subjectId and study_id = :studyId
            RETURNING subject_number
        """

@Component
class DeleteSubjectNumberPostgresAdapter(
    @Qualifier("postgresNamedParameterJdbcTemplate")
    private val jdbcTemplate: NamedParameterJdbcTemplate
) : DeleteSubjectNumberOutPort {
    override suspend fun deleteSubjectNumber(studyId: String, subjectId: String) {
        Mono.fromCallable {
            isAlreadyParticipated(studyId, subjectId, jdbcTemplate)
        }.filter { it }
            .switchIfEmpty { Mono.error(NotFoundException("subject not found")) }
            .map {
                delete(studyId, subjectId)
            }.onErrorMap { ex ->
                toDomainException(ex)
            }
            .subscribeOn(Schedulers.boundedElastic())
            .awaitSingle()
    }

    private fun delete(studyId: String, subjectId: String) =
        jdbcTemplate.queryForObject(
            DELETE_QUERY,
            toNamedParameter(studyId, subjectId),
            String::class.java
        ) ?: throw EmptyResultDataAccessException(1)
}
