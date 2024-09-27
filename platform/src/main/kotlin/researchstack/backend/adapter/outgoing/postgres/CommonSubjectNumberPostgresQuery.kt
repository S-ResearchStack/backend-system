package researchstack.backend.adapter.outgoing.postgres

import org.springframework.dao.DuplicateKeyException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.BadSqlGrammarException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import researchstack.backend.application.exception.AlreadyExistsException
import researchstack.backend.application.exception.NotFoundException

object CommonSubjectNumberPostgresQuery {
    const val CHECK_EXIST_QUERY = """
        SELECT EXISTS(
            SELECT 1
             FROM study_joins
             WHERE subject_id = :subjectId and study_id = :studyId
     )
    """

    fun toDomainException(ex: Throwable): Throwable = when (ex) {
        is BadSqlGrammarException -> {
            NotFoundException("not found study")
        }

        is DuplicateKeyException -> {
            AlreadyExistsException("already participated study")
        }
        // TODO handle exception: DataAccessException to domain exception
        else -> ex
    }

    fun isAlreadyParticipated(studyId: String, subjectId: String, jdbcTemplate: NamedParameterJdbcTemplate) =
        jdbcTemplate.queryForObject(
            CHECK_EXIST_QUERY,
            toNamedParameter(studyId, subjectId),
            Boolean::class.java
        ) ?: throw EmptyResultDataAccessException(1)

    fun toNamedParameter(studyId: String, subjectId: String) = mapOf(
        "studyId" to studyId,
        "subjectId" to subjectId
    )
}
