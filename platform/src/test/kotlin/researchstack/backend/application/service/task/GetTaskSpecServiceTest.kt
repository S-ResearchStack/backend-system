package researchstack.backend.application.service.task

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.exception.NotFoundException
import researchstack.backend.application.port.outgoing.task.GetTaskSpecOutPort
import researchstack.backend.domain.task.ActivityTask
import researchstack.backend.domain.task.TaskSpec
import researchstack.backend.enums.ActivityType
import researchstack.backend.enums.TaskStatus
import researchstack.backend.enums.TaskType
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class GetTaskSpecServiceTest {
    private val getTaskSpecOutPort = mockk<GetTaskSpecOutPort>(relaxed = true)
    private val getTaskResultService = GetTaskSpecService(getTaskSpecOutPort)

    private val taskId = "t1"
    private val studyId = "0"
    private val now = LocalDateTime.now()
    private val dummyTask = TaskSpec(
        id = taskId,
        studyId = studyId,
        title = "Physical activity survey",
        description = "description",
        status = TaskStatus.PUBLISHED,
        schedule = "0 0 12 ? * SUN *",
        createdAt = now,
        publishedAt = now,
        startTime = now,
        endTime = now.plusDays(5),
        validMin = 180,
        duration = "takes 5 minutes",
        iconUrl = "http://www.test.com",
        inClinic = true,
        taskType = TaskType.ACTIVITY,
        task = ActivityTask(
            completionTitle = "Great activity task!",
            completionDescription = "Your task was successfully completed!",
            type = ActivityType.TAPPING_SPEED
        )
    )

    @BeforeEach
    fun setUp() {
        coEvery {
            getTaskSpecOutPort.getTaskSpecs(studyId)
        } returns listOf(dummyTask)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getTaskSpec should throw NotFoundException when the studyId is unmatched`() = runTest {
        val unmatchedStudyId = "unmatched-study-id"
        coEvery {
            getTaskSpecOutPort.getTaskSpec(taskId)
        } returns dummyTask

        val exception = assertThrows<NotFoundException> {
            getTaskResultService.getTaskSpec(unmatchedStudyId, taskId)
        }
        assertEquals("no task($taskId) on study($unmatchedStudyId)", exception.message)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `get taskSpecs by studyId properly`() = runTest {
        val result = getTaskResultService.getTaskSpecs(studyId)

        assertEquals(1, result.size)
        assertEquals(dummyTask.id, result[0].id)
        assertEquals(dummyTask.studyId, result[0].studyId)
        assertEquals(dummyTask.title, result[0].title)
        assertEquals(dummyTask.description, result[0].description)
        assertEquals(dummyTask.schedule, result[0].schedule)
        assertEquals(dummyTask.startTime, result[0].startTime)
        assertEquals(dummyTask.endTime, result[0].endTime)
        assertEquals(dummyTask.validMin, result[0].validMin)
        assertEquals(dummyTask.duration, result[0].duration)
        assertEquals(dummyTask.iconUrl, result[0].iconUrl)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `get taskSpecs returns empty list when its studyId was empty`() = runTest {
        val result = getTaskResultService.getTaskSpecs("")
        assertTrue(result.isEmpty())
    }
}
