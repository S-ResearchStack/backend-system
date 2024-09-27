package researchstack.backend.adapter.incoming.grpc.task

import com.google.protobuf.Empty
import com.google.protobuf.Timestamp
import com.linecorp.armeria.server.ServiceRequestContext
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.incoming.mapper.task.toCommand
import researchstack.backend.adapter.incoming.mapper.toLocalDateTime
import researchstack.backend.application.port.incoming.task.CreateTaskResultUseCase
import researchstack.backend.application.port.incoming.task.GetTaskSpecUseCase
import researchstack.backend.application.port.incoming.task.SurveyTaskResponse
import researchstack.backend.config.getUserId
import researchstack.backend.domain.subject.Subject
import researchstack.backend.enums.QuestionTag
import researchstack.backend.enums.TaskType
import researchstack.backend.grpc.AllNewTaskRequest
import researchstack.backend.grpc.TaskResult
import researchstack.backend.grpc.TaskResult.QuestionResult
import researchstack.backend.grpc.TaskResult.SurveyResult
import researchstack.backend.grpc.TaskResultUploadRequest
import researchstack.backend.grpc.TaskSpecRequest
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
internal class TaskGrpcControllerTest {
    private val getTaskSpecUseCase = mockk<GetTaskSpecUseCase>()
    private val createTaskResultUseCase = mockk<CreateTaskResultUseCase>(relaxed = true)
    private val serviceRequestContext = mockk<ServiceRequestContext>()
    private val taskGrpcController = TaskGrpcController(
        getTaskSpecUseCase,
        createTaskResultUseCase
    )

    private val now = LocalDateTime.now()
    private val taskId = "mentalSurveyTask"
    private val studyId = "mentalCareStudy"
    private val title = "basic survey for mentalcare study"
    private val description = "conduct survey for mentalcare study"
    private val schedule = "0 0 0 * * ? *"
    private val startTime = now
    private val endTime = now.plusDays(20)
    private val validMin = 1440L
    private val duration = "takes 5 minutes"
    private val iconUrl = "http://www.test.com"
    private val inClinic = true
    private val surveyTask = SurveyTaskResponse(
        listOf(
            researchstack.backend.application.port.incoming.task.Section(
                listOf(
                    researchstack.backend.application.port.incoming.task.Question(
                        id = "1",
                        title = "How old are you?",
                        explanation = "aa",
                        tag = QuestionTag.SLIDER,
                        itemProperties = researchstack.backend.application.port.incoming.task.Question.ScaleProperties(
                            0,
                            1100,
                            "ll",
                            "hl"
                        ),
                        required = true
                    )
                )
            )
        )
    )

    private val taskSpecResponse = researchstack.backend.application.port.incoming.task.TaskSpecResponse(
        id = taskId,
        studyId = studyId,
        title = title,
        description = description,
        schedule = schedule,
        startTime = startTime,
        endTime = endTime,
        validMin = validMin,
        duration = duration,
        iconUrl = iconUrl,
        inClinic = inClinic,
        taskType = TaskType.SURVEY,
        task = surveyTask
    )
    private val taskSpecResponseList = listOf(
        taskSpecResponse
    )
    private val userId = "7e3ba225-0e3c-4c87-9d02-b1fbe0802239"
    private val lastSyncTime = Timestamp.newBuilder().setSeconds(1701411670).build()
    private val taskResultUploadRequest = TaskResultUploadRequest.newBuilder()
        .setTaskResult(
            TaskResult.newBuilder()
                .setTaskId(taskId)
                .setStudyId(studyId)
                .setStartedAt(Timestamp.newBuilder().setSeconds(1701411670).build())
                .setFinishedAt(Timestamp.newBuilder().setSeconds(1701411670).build())
                .setSurveyResult(
                    SurveyResult.newBuilder()
                        .addAllQuestionResults(
                            listOf(
                                QuestionResult.newBuilder()
                                    .setId("1")
                                    .setResult("yes")
                                    .build()
                            )
                        ).build()
                ).build()
        ).build()

    @Test
    @Tag(POSITIVE_TEST)
    fun `getTaskSpecs should work properly`() = runTest {
        coEvery {
            getTaskSpecUseCase.getTaskSpecs(studyId)
        } returns taskSpecResponseList

        val response = taskGrpcController.getTaskSpecs(
            TaskSpecRequest.newBuilder().setStudyId(studyId).build()
        )
        Assertions.assertEquals(taskId, response.taskSpecsList[0].id)
        Assertions.assertEquals(studyId, response.taskSpecsList[0].studyId)
        Assertions.assertEquals(title, response.taskSpecsList[0].title)
        Assertions.assertEquals(description, response.taskSpecsList[0].description)
        Assertions.assertEquals(schedule, response.taskSpecsList[0].schedule)
        Assertions.assertEquals(validMin, response.taskSpecsList[0].validMin)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getTaskSpecs should throw IllegalArgumentException if studyId is empty`(studyId: String) = runTest {
        assertThrows<IllegalArgumentException> {
            taskGrpcController.getTaskSpecs(TaskSpecRequest.newBuilder().setStudyId(studyId).build())
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getAllTaskSpecs should work properly`() = runTest {
        mockkStatic(ServiceRequestContext::class)
        coEvery {
            ServiceRequestContext.current()
        } returns serviceRequestContext
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        coEvery {
            getTaskSpecUseCase.getAllTaskSpecs(Subject.SubjectId.from(userId))
        } returns taskSpecResponseList

        val response = taskGrpcController.getAllTaskSpecs(
            Empty.newBuilder().build()
        )
        Assertions.assertEquals(taskId, response.taskSpecsList[0].id)
        Assertions.assertEquals(studyId, response.taskSpecsList[0].studyId)
        Assertions.assertEquals(title, response.taskSpecsList[0].title)
        Assertions.assertEquals(description, response.taskSpecsList[0].description)
        Assertions.assertEquals(schedule, response.taskSpecsList[0].schedule)
        Assertions.assertEquals(validMin, response.taskSpecsList[0].validMin)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getAllTaskSpecs should throw IllegalArgumentException if userId is empty`(userId: String) = runTest {
        mockkStatic(ServiceRequestContext::class)
        coEvery {
            ServiceRequestContext.current()
        } returns serviceRequestContext
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        assertThrows<IllegalArgumentException> {
            taskGrpcController.getAllTaskSpecs(Empty.newBuilder().build())
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getAllNewTaskSpecs should work properly`() = runTest {
        mockkStatic(ServiceRequestContext::class)
        coEvery {
            ServiceRequestContext.current()
        } returns serviceRequestContext
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        coEvery {
            getTaskSpecUseCase.getAllNewTaskSpecs(
                Subject.SubjectId.from(userId),
                lastSyncTime.toLocalDateTime()
            )
        } returns taskSpecResponseList

        val response = taskGrpcController.getAllNewTaskSpecs(
            AllNewTaskRequest.newBuilder().setLastSyncTime(
                lastSyncTime
            ).build()
        )
        Assertions.assertEquals(taskId, response.taskSpecsList[0].id)
        Assertions.assertEquals(studyId, response.taskSpecsList[0].studyId)
        Assertions.assertEquals(title, response.taskSpecsList[0].title)
        Assertions.assertEquals(description, response.taskSpecsList[0].description)
        Assertions.assertEquals(schedule, response.taskSpecsList[0].schedule)
        Assertions.assertEquals(validMin, response.taskSpecsList[0].validMin)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getAllNewTaskSpecs should throw IllegalArgumentException if userId is empty`(userId: String) = runTest {
        mockkStatic(ServiceRequestContext::class)
        coEvery {
            ServiceRequestContext.current()
        } returns serviceRequestContext
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        assertThrows<IllegalArgumentException> {
            taskGrpcController.getAllNewTaskSpecs(
                AllNewTaskRequest.newBuilder().setLastSyncTime(
                    lastSyncTime
                ).build()
            )
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `uploadTaskResult should work properly`() = runTest {
        mockkStatic(ServiceRequestContext::class)
        coEvery {
            ServiceRequestContext.current()
        } returns serviceRequestContext
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        coEvery {
            createTaskResultUseCase.createTaskResult(
                Subject.SubjectId.from(userId),
                taskResultUploadRequest.taskResult.studyId,
                taskResultUploadRequest.taskResult.toCommand()
            )
        } returns Unit

        assertDoesNotThrow {
            taskGrpcController.uploadTaskResult(
                taskResultUploadRequest
            )
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `uploadTaskResult should throw IllegalArgumentException if userId is empty`(userId: String) = runTest {
        mockkStatic(ServiceRequestContext::class)
        coEvery {
            ServiceRequestContext.current()
        } returns serviceRequestContext
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        assertThrows<IllegalArgumentException> {
            taskGrpcController.uploadTaskResult(
                taskResultUploadRequest
            )
        }
    }
}
