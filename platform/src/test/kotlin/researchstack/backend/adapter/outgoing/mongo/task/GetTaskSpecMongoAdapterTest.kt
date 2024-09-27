package researchstack.backend.adapter.outgoing.mongo.task

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.StudyTestUtil
import researchstack.backend.TaskTestUtil
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.TaskSpecRepository
import researchstack.backend.application.port.outgoing.study.GetStudyOutPort
import researchstack.backend.domain.subject.Subject
import researchstack.backend.enums.TaskStatus
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
internal class GetTaskSpecMongoAdapterTest {
    private val repository = mockk<TaskSpecRepository>()
    private val getStudyOutPort = mockk<GetStudyOutPort>()
    private val adapter = GetTaskSpecMongoAdapter(getStudyOutPort, repository)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getTaskSpec should throw NoSuchElementException when there is no task matched with the given task id`() =
        runTest {
            val taskId = "t1"

            every {
                repository.findById(taskId)
            } returns Mono.empty()

            assertThrows<NoSuchElementException> {
                adapter.getTaskSpec(taskId)
            }
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getTaskSpec should work properly`() = runTest {
        val taskId = "t1"
        val taskSpec = TaskTestUtil.createActivityTaskSpec()
        val taskSpecEntity = taskSpec.toEntity()

        every {
            repository.findById(taskId)
        } returns taskSpecEntity.toMono()

        assertDoesNotThrow {
            adapter.getTaskSpec(taskId)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getTaskSpecs should work properly`() = runTest {
        val studyId = "s1"
        val taskSpec = TaskTestUtil.createActivityTaskSpec()
        val taskSpecEntity = taskSpec.toEntity()

        every {
            repository.findByStudyIdAndStatus(studyId, TaskStatus.PUBLISHED)
        } returns listOf(taskSpecEntity).toFlux()

        assertDoesNotThrow {
            adapter.getTaskSpecs(studyId)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getAllNewTaskSpecs should work properly`() = runTest {
        val studyId = "s1"
        val subjectId = Subject.SubjectId.from("u1")
        val taskSpec = TaskTestUtil.createActivityTaskSpec()
        val taskSpecEntity = taskSpec.toEntity()

        coEvery {
            getStudyOutPort.getParticipatedStudyList(subjectId.value)
        } returns listOf(StudyTestUtil.createDummyStudy(studyId))

        every {
            repository.findByStudyIdAndStatus(studyId, TaskStatus.PUBLISHED)
        } returns listOf(taskSpecEntity).toFlux()

        assertDoesNotThrow {
            adapter.getAllNewTaskSpecs(subjectId, LocalDateTime.now())
        }
    }
}
