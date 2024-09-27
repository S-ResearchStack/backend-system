package researchstack.backend.adapter.outgoing.mongo.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("appLog")
data class AppLogEntity(
    @Id
    val id: String?,
    val name: String,
    val timestamp: LocalDateTime,
    val data: Map<String, String>
)
