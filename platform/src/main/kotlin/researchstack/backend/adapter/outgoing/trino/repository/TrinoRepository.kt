package researchstack.backend.adapter.outgoing.trino.repository

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import researchstack.backend.application.exception.InternalServerException

@Repository
class TrinoRepository(
    @Qualifier("trinoJdbcTemplate")
    private val jdbcTemplate: JdbcTemplate
) {
    suspend fun getMetaData(query: String): Map<String, Int> {
        val map = mutableMapOf<String, Int>()
        jdbcTemplate.query(query) {
            try {
                val metadata = it.metaData
                val columnCount = metadata.columnCount

                for (i in 1..columnCount) {
                    val name = metadata.getColumnName(i)
                    val type = metadata.getColumnType(i)
                    map[name] = type
                }
            } catch (e: Exception) {
                throw InternalServerException(e.message ?: "")
            }
        }
        return map
    }

    suspend fun execute(query: String): List<Map<String, Any>> {
        return jdbcTemplate.queryForList(query)
    }
}
