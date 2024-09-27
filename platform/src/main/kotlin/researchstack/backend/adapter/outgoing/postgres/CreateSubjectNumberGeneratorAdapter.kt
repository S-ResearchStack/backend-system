package researchstack.backend.adapter.outgoing.postgres

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import researchstack.backend.application.port.outgoing.study.CreateSubjectNumberGeneratorOutPort

@Component
class CreateSubjectNumberGeneratorAdapter(
    @Qualifier("postgresJdbcTemplate")
    private val jdbcTemplate: JdbcTemplate
) : CreateSubjectNumberGeneratorOutPort {
    override suspend fun createSubjectNumberGenerator(studyId: String) {
        jdbcTemplate.execute("CREATE SEQUENCE $studyId")
    }
}
