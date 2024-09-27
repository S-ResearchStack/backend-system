package researchstack.backend.application.port.incoming.task

import researchstack.backend.enums.ActivityType

data class ActivityTaskResponse(
    val completionTitle: String,
    val completionDescription: String,
    val type: ActivityType,
    val imageUrl: String? = null,
    val audioUrl: String? = null,
    val recordingTime: Long? = null
) : TaskSpecResponse.Task
