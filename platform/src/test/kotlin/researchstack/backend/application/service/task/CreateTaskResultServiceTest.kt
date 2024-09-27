package researchstack.backend.application.service.task

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.port.incoming.task.TaskResultCommand
import researchstack.backend.application.port.incoming.task.TaskResultCommand.ActivityResult.TappingSpeed
import researchstack.backend.application.port.outgoing.task.CreateTaskResultOutPort
import researchstack.backend.application.service.mapper.toDomain
import researchstack.backend.domain.subject.Subject
import java.time.Instant

@ExperimentalCoroutinesApi
internal class CreateTaskResultServiceTest {
    private val createTaskResultOutPort = mockk<CreateTaskResultOutPort>(relaxed = true)
    private val createTaskResultMongoOutPort = mockk<CreateTaskResultOutPort>(relaxed = true)
    private val createTaskResultService = CreateTaskResultService(
        createTaskResultOutPort
    )

    private val subjectId = Subject.SubjectId.from("test-subject")
    private val activityResult = TaskResultCommand.ActivityResult(TappingSpeed("TAPPING_SPEED"))
    private val command = TaskResultCommand(
        "study-id",
        "task-id",
        activityResult,
        Instant.now().toEpochMilli(),
        Instant.now().plusMillis(3600000).toEpochMilli(),
        32400000
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `createTaskResult should work properly`() = runTest {
        coEvery {
            createTaskResultOutPort.createTaskResult(subjectId.value, command.toDomain())
        } returns Unit

        coEvery {
            createTaskResultMongoOutPort.createTaskResult(subjectId.value, command.toDomain())
        } returns Unit

        createTaskResultService.createTaskResult(subjectId, command.studyId, command)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `createTaskResult throws Exception when subjectId was empty`() = runTest {
        assertThrows<IllegalArgumentException> {
            createTaskResultService.createTaskResult(
                Subject.SubjectId.from(null),
                command.studyId,
                command
            )
        }
    }
}
