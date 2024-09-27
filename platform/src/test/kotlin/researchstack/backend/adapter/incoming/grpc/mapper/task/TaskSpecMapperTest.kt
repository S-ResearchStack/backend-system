package researchstack.backend.adapter.incoming.grpc.mapper.task

import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.incoming.mapper.task.toGrpc
import researchstack.backend.adapter.incoming.mapper.task.toResponse
import researchstack.backend.adapter.incoming.mapper.toGrpc
import researchstack.backend.application.port.incoming.task.ActivityTaskResponse
import researchstack.backend.application.port.incoming.task.CreateTaskSpecCommand
import researchstack.backend.application.port.incoming.task.Section
import researchstack.backend.application.port.incoming.task.SurveyTaskResponse
import researchstack.backend.application.port.incoming.task.TaskSpecResponse
import researchstack.backend.domain.task.TaskSpec
import researchstack.backend.enums.ActivityType
import researchstack.backend.enums.TaskStatus
import researchstack.backend.enums.TaskType
import researchstack.backend.grpc.ActivityTask
import researchstack.backend.grpc.SurveyTask
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertEquals
import researchstack.backend.grpc.ActivityType as GrpcActivityType
import researchstack.backend.grpc.TaskSpec as GrpcTaskSpec

internal class TaskSpecMapperTest {
    private val id = "task-id"
    private val studyId = "study-id"
    private val title = "Lorem"
    private val description = "Lorem ipsum dolor sit amet."
    private val schedule = "0 0 0 * * ? *"
    private val startTime = LocalDateTime.now()
    private val endTime = LocalDateTime.now().plusDays(1)
    private val validMin = 123L
    private val duration = "10s"
    private val iconUrl = "example.com"
    private val inClinic = true

    @Test
    @Tag(POSITIVE_TEST)
    fun `Activity TaskSpec's toResponse should work properly`() {
        mockkStatic(UUID::class)
        every {
            UUID.randomUUID().toString()
        } returns id

        val response = createTaskSpec(
            TaskType.ACTIVITY,
            mapOf(
                "completionTitle" to "ct",
                "completionDescription" to "cd",
                "type" to ActivityType.STROOP_TEST.name
            )
        ).toResponse()

        assertEquals(
            createTaskSpecResponse(
                TaskType.ACTIVITY,
                ActivityTaskResponse("ct", "cd", ActivityType.STROOP_TEST),
                response.createdAt,
                response.publishedAt
            ),
            response
        )
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `Survey TaskSpec's toResponse should work properly`() {
        mockkStatic(UUID::class)
        every {
            UUID.randomUUID().toString()
        } returns id

        val response = createTaskSpec(
            TaskType.SURVEY,
            mapOf(
                "sections" to listOf<Section>()
            )
        ).toResponse()

        assertEquals(
            createTaskSpecResponse(
                TaskType.SURVEY,
                SurveyTaskResponse(listOf()),
                response.createdAt,
                response.publishedAt
            ),
            response
        )
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `Activity TaskSpecResponse's toGrpc should work properly`() {
        assertEquals(
            GrpcTaskSpec.newBuilder()
                .setId(id)
                .setStudyId(studyId)
                .setTitle(title)
                .setDescription(description)
                .setSchedule(schedule)
                .setStartTime(startTime.toGrpc())
                .setEndTime(endTime.toGrpc())
                .setValidMin(validMin)
                .setInClinic(inClinic)
                .setActivityTask(
                    ActivityTask.newBuilder()
                        .setCompletionTitle("ct")
                        .setCompletionDescription("cd")
                        .setType(GrpcActivityType.ACTIVITY_TYPE_STROOP_TEST)
                        .setImageUrl("")
                        .setAudioUrl("")
                        .setRecordingTime(30L)
                        .build()
                )
                .build(),
            createTaskSpecResponse(
                TaskType.ACTIVITY,
                ActivityTaskResponse("ct", "cd", ActivityType.STROOP_TEST, "", "", 30L)
            ).toGrpc()
        )
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `Survey TaskSpecResponse's toGrpc should work properly`() {
        assertEquals(
            GrpcTaskSpec.newBuilder()
                .setId(id)
                .setStudyId(studyId)
                .setTitle(title)
                .setDescription(description)
                .setSchedule(schedule)
                .setStartTime(startTime.toGrpc())
                .setEndTime(endTime.toGrpc())
                .setValidMin(validMin)
                .setInClinic(inClinic)
                .setSurveyTask(
                    SurveyTask.newBuilder()
                        .addAllSections(listOf())
                        .build()
                )
                .build(),
            createTaskSpecResponse(
                TaskType.SURVEY,
                SurveyTaskResponse(listOf())
            ).toGrpc()
        )
    }

    private fun createTaskSpec(taskType: TaskType, task: Map<String, Any>): TaskSpec {
        val command = CreateTaskSpecCommand(
            title = title,
            description = description,
            status = TaskStatus.PUBLISHED,
            schedule = schedule,
            startTime = startTime,
            endTime = endTime,
            validMin = 123,
            duration = duration,
            iconUrl = iconUrl,
            inClinic = inClinic,
            taskType = taskType,
            task = task
        )

        return command.toDomain(studyId)
    }

    private fun createTaskSpecResponse(
        type: TaskType,
        task: TaskSpecResponse.Task,
        createdAt: LocalDateTime? = null,
        publishedAt: LocalDateTime? = null
    ) = TaskSpecResponse(
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
        taskType = type,
        task = task
    )
}
