package researchstack.backend.adapter.incoming.rest.task

import com.linecorp.armeria.common.HttpStatus
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.TaskTestUtil
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.application.port.incoming.task.CreateTaskResultUseCase
import researchstack.backend.application.port.incoming.task.CreateTaskSpecUseCase
import researchstack.backend.application.port.incoming.task.DeleteTaskSpecUseCase
import researchstack.backend.application.port.incoming.task.GetTaskSpecUseCase
import researchstack.backend.application.port.incoming.task.UpdateTaskSpecUseCase
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class TaskRestControllerTest {
    private val createTaskSpecUseCase = mockk<CreateTaskSpecUseCase>()
    private val getTaskSpecUseCase = mockk<GetTaskSpecUseCase>()
    private val updateTaskSpecUseCase = mockk<UpdateTaskSpecUseCase>()
    private val deleteTaskSpecUseCase = mockk<DeleteTaskSpecUseCase>()
    private val createTaskResultUseCase = mockk<CreateTaskResultUseCase>()
    private val taskHttpController = TaskRestController(
        createTaskSpecUseCase,
        getTaskSpecUseCase,
        updateTaskSpecUseCase,
        deleteTaskSpecUseCase,
        createTaskResultUseCase
    )

    private val studyId = "mentalCareStudy"
    private val taskId = "mentalSurveyTask"
    private val schedule = "0 0 0 * * ? *"

    @Test
    @Tag(POSITIVE_TEST)
    fun `createTaskSpec should work properly`() = runTest {
        val command = TaskTestUtil.createCreateTaskSpecCommand(schedule)
        coEvery {
            createTaskSpecUseCase.createTaskSpec(command, studyId)
        } returns Unit

        val response = taskHttpController.createTaskSpec(studyId, command).aggregate().join()
        assertEquals(HttpStatus.OK, response.status())
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `createTaskSpec throws IllegalArgumentException when studyId was empty`(studyId: String) = runTest {
        val command = TaskTestUtil.createCreateTaskSpecCommand(schedule)
        val exception = assertThrows<IllegalArgumentException> {
            taskHttpController.createTaskSpec(studyId, command)
        }
        assertEquals(ExceptionMessage.EMPTY_STUDY_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `createTaskSpec throws IllegalArgumentException when schedule was empty`(schedule: String) = runTest {
        assertThrows<IllegalArgumentException> {
            taskHttpController.createTaskSpec(
                studyId,
                TaskTestUtil.createCreateTaskSpecCommand(schedule)
            )
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getTaskSpec should work properly`() = runTest {
        val taskSpecResponse = TaskTestUtil.createTaskSpecResponse()
        coEvery {
            getTaskSpecUseCase.getTaskSpec(studyId, taskId)
        } returns taskSpecResponse

        val response = taskHttpController.getTaskSpec(studyId, taskId).aggregate().join()
        assertEquals(HttpStatus.OK, response.status())
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getTaskSpec throws IllegalArgumentException when studyId was empty`(studyId: String) = runTest {
        val exception = assertThrows<IllegalArgumentException> {
            taskHttpController.getTaskSpec(studyId, taskId)
        }
        assertEquals(ExceptionMessage.EMPTY_STUDY_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getTaskSpec throws IllegalArgumentException when taskId was empty`(taskId: String) = runTest {
        val exception = assertThrows<IllegalArgumentException> {
            taskHttpController.getTaskSpec(studyId, taskId)
        }
        assertEquals(ExceptionMessage.EMPTY_TASK_ID, exception.message)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `updateTaskSpec should work properly`() = runTest {
        val command = TaskTestUtil.createUpdateTaskSpecCommand(schedule)
        coEvery {
            updateTaskSpecUseCase.updateTaskSpec(command, taskId, studyId)
        } returns Unit

        val response = taskHttpController.updateTaskSpec(studyId, taskId, command).aggregate().join()
        assertEquals(HttpStatus.OK, response.status())
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `updateTaskSpec throws IllegalArgumentException when studyId was empty`(studyId: String) = runTest {
        val command = TaskTestUtil.createUpdateTaskSpecCommand(schedule)
        val exception = assertThrows<IllegalArgumentException> {
            taskHttpController.updateTaskSpec(studyId, taskId, command)
        }
        assertEquals(ExceptionMessage.EMPTY_STUDY_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `updateTaskSpec throws IllegalArgumentException when taskId was empty`(taskId: String) = runTest {
        val command = TaskTestUtil.createUpdateTaskSpecCommand(schedule)
        val exception = assertThrows<IllegalArgumentException> {
            taskHttpController.updateTaskSpec(studyId, taskId, command)
        }
        assertEquals(ExceptionMessage.EMPTY_TASK_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `updateTaskSpec throws IllegalArgumentException when schedule was empty`(schedule: String) = runTest {
        assertThrows<IllegalArgumentException> {
            taskHttpController.updateTaskSpec(
                studyId,
                taskId,
                TaskTestUtil.createUpdateTaskSpecCommand(schedule)
            )
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `deleteTaskSpec should work properly`() = runTest {
        coEvery {
            deleteTaskSpecUseCase.deleteTaskSpec(taskId, studyId)
        } returns Unit

        val response = taskHttpController.deleteTaskSpec(studyId, taskId).aggregate().join()
        assertEquals(HttpStatus.OK, response.status())
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `deleteTaskSpec throws IllegalArgumentException when taskId was empty`(taskId: String) = runTest {
        val exception = assertThrows<IllegalArgumentException> {
            taskHttpController.deleteTaskSpec(studyId, taskId)
        }
        assertEquals(ExceptionMessage.EMPTY_TASK_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `deleteTaskSpec throws IllegalArgumentException when studyId was empty`(studyId: String) = runTest {
        val exception = assertThrows<IllegalArgumentException> {
            taskHttpController.deleteTaskSpec(studyId, taskId)
        }
        assertEquals(ExceptionMessage.EMPTY_STUDY_ID, exception.message)
    }
}
