package researchstack.backend.adapter.outgoing.mongo.entity.dashboard

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("dashboard")
data class DashboardEntity(
    @Id
    val id: String?,
    val title: String?
)
