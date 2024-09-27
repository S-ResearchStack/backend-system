package researchstack.backend.adapter.incoming.mapper.task

import researchstack.backend.adapter.incoming.mapper.toGrpc
import researchstack.backend.application.port.incoming.task.ActivityTaskResponse
import researchstack.backend.application.port.incoming.task.SurveyTaskResponse
import researchstack.backend.application.port.incoming.task.TaskSpecResponse
import researchstack.backend.domain.task.ActivityTask
import researchstack.backend.domain.task.SurveyTask
import researchstack.backend.domain.task.TaskSpec
import researchstack.backend.grpc.TaskSpec as GrpcTaskSpec

fun TaskSpecResponse.toGrpc(): GrpcTaskSpec {
    val grpcTaskSpec = GrpcTaskSpec.newBuilder()
        .setId(id)
        .setStudyId(studyId)
        .setTitle(title)
        .setDescription(description)
        .setSchedule(schedule)
        .setStartTime(startTime?.toGrpc())
        .setEndTime(endTime?.toGrpc())
        .setValidMin(validMin)
        .setInClinic(inClinic)

    when (task) {
        is ActivityTaskResponse -> grpcTaskSpec.setActivityTask(task.toGrpc())
        is SurveyTaskResponse -> grpcTaskSpec.setSurveyTask(task.toGrpc())
    }
    return grpcTaskSpec.build()
}

fun TaskSpec.toResponse(): TaskSpecResponse =
    TaskSpecResponse(
        id = id,
        studyId = studyId,
        title = title,
        description = description,
        schedule = schedule,
        createdAt = createdAt,
        publishedAt = publishedAt,
        startTime = startTime,
        endTime = endTime,
        validMin = validMin,
        duration = duration,
        iconUrl = iconUrl,
        inClinic = inClinic,
        taskType = taskType,
        task = task.toResponse()
    )

private fun TaskSpec.Task.toResponse(): TaskSpecResponse.Task =
    when (this) {
        is SurveyTask -> this.toResponse()
        is ActivityTask -> this.toResponse()
    }
