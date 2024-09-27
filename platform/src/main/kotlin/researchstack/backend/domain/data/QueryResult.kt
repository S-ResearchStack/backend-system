package researchstack.backend.domain.data

data class QueryResult(
    val columns: Map<String, String>,
    val data: List<Map<String, Any>>
)
