package researchstack.backend.adapter.outgoing.mongo.task

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.dao.DuplicateKeyException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.TaskTestUtil
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.TaskSpecRepository
import researchstack.backend.application.exception.AlreadyExistsException

@ExperimentalCoroutinesApi
internal class UpdateTaskSpecMongoAdapterTest {
    private val taskSpecRepository = mockk<TaskSpecRepository>()
    private val adapter = UpdateTaskSpecMongoAdapter(taskSpecRepository)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `updateTaskSpec should throw AlreadyExistsException when it fails to create because of a duplicate key`() =
        runTest {
            val taskSpec = TaskTestUtil.createActivityTaskSpec()
            val taskSpecEntity = taskSpec.toEntity()

            every {
                taskSpecRepository.findByIdAndStudyId(taskSpec.id, taskSpec.studyId)
            } returns taskSpecEntity.toMono()
            every {
                taskSpecRepository.save(taskSpecEntity)
            } returns Mono.error(DuplicateKeyException("duplicate key"))

            assertThrows<AlreadyExistsException> {
                adapter.updateTaskSpec(taskSpec)
            }
        }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `updateTaskSpec should throw NoSuchElementException when there is no study matched with the given study id`() =
        runTest {
            val taskSpec = TaskTestUtil.createActivityTaskSpec()

            every {
                taskSpecRepository.findByIdAndStudyId(taskSpec.id, taskSpec.studyId)
            } returns Mono.empty()

            assertThrows<NoSuchElementException> {
                adapter.updateTaskSpec(taskSpec)
            }
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `updateTaskSpec should work properly`() = runTest {
        val taskSpec = TaskTestUtil.createActivityTaskSpec()
        val taskSpecEntity = taskSpec.toEntity()

        every {
            taskSpecRepository.findByIdAndStudyId(taskSpec.id, taskSpec.studyId)
        } returns taskSpecEntity.toMono()
        every {
            taskSpecRepository.save(taskSpecEntity)
        } returns taskSpecEntity.toMono()

        assertDoesNotThrow {
            adapter.updateTaskSpec(taskSpec)
        }
    }
}
