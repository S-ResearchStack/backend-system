package researchstack.backend.adapter.incoming.mapper.task

import researchstack.backend.adapter.incoming.mapper.toGrpc
import researchstack.backend.application.port.incoming.task.ActivityTaskResponse
import researchstack.backend.domain.task.ActivityTask
import researchstack.backend.grpc.ActivityTask as GrpcActivityTask

fun ActivityTaskResponse.toGrpc(): GrpcActivityTask {
    val activityTaskBuilder = GrpcActivityTask.newBuilder()
        .setCompletionTitle(completionTitle)
        .setCompletionDescription(completionDescription)
        .setType(type.toGrpc())
    imageUrl?.let { activityTaskBuilder.setImageUrl(imageUrl) }
    audioUrl?.let { activityTaskBuilder.setAudioUrl(audioUrl) }
    recordingTime?.let { activityTaskBuilder.setRecordingTime(recordingTime) }
    return activityTaskBuilder.build()
}

fun ActivityTask.toResponse(): ActivityTaskResponse =
    ActivityTaskResponse(
        completionTitle = completionTitle,
        completionDescription = completionDescription,
        type = type,
        imageUrl = imageUrl,
        audioUrl = audioUrl,
        recordingTime = recordingTime
    )
