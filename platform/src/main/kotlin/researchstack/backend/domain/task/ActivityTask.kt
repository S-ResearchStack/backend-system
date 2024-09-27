package researchstack.backend.domain.task

import kotlinx.serialization.Serializable
import researchstack.backend.enums.ActivityType

@Serializable
data class ActivityTask(
    val completionTitle: String,
    val completionDescription: String,
    val type: ActivityType,
    val imageUrl: String? = null,
    val audioUrl: String? = null,
    val recordingTime: Long? = null
) : TaskSpec.Task
