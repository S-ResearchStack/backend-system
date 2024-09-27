package researchstack.backend.adapter.outgoing.mongo.entity.dashboard

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("chart")
data class ChartEntity(
    @Id
    val id: String?,
    val dashboardId: String,
    val source: ChartSource,
    val configBasic: ConfigBasic,
    val configSpecific: String
) {
    data class ChartSource(
        val database: String,
        val query: String,
        val transform: SourceTransform
    )

    data class SourceTransform(val limit: Int)

    data class ConfigBasic(
        val name: String,
        val type: String
    )
}
