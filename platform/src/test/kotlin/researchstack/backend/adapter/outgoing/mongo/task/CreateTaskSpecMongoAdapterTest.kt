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
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class CreateTaskSpecMongoAdapterTest {
    private val taskSpecRepository = mockk<TaskSpecRepository>()
    private val adapter = CreateTaskSpecMongoAdapter(taskSpecRepository)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `createTaskSpec should throw AlreadyExistsException when the given task id already exists`() = runTest {
        val taskSpec = TaskTestUtil.createActivityTaskSpec()

        every {
            taskSpecRepository.existsById(taskSpec.id)
        } returns true.toMono()

        val exception = assertThrows<AlreadyExistsException> {
            adapter.createTaskSpec(taskSpec)
        }
        assertEquals("task id (${taskSpec.id}) already exists", exception.message)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `createTaskSpec should throw AlreadyExistsException when it fails to create because of a duplicate key`() =
        runTest {
            val taskSpec = TaskTestUtil.createActivityTaskSpec()

            every {
                taskSpecRepository.existsById(taskSpec.id)
            } returns false.toMono()
            every {
                taskSpecRepository.save(taskSpec.toEntity())
            } returns Mono.error(DuplicateKeyException("duplicate key"))

            assertThrows<AlreadyExistsException> {
                adapter.createTaskSpec(taskSpec)
            }
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `createTaskSpec should work properly`() = runTest {
        val taskSpec = TaskTestUtil.createActivityTaskSpec()
        val taskSpecEntity = taskSpec.toEntity()

        every {
            taskSpecRepository.existsById(taskSpec.id)
        } returns false.toMono()
        every {
            taskSpecRepository.save(taskSpecEntity)
        } returns taskSpecEntity.toMono()

        assertDoesNotThrow {
            adapter.createTaskSpec(taskSpec)
        }
    }
}
