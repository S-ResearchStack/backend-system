package researchstack.backend.adapter.outgoing.mongo.task

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import reactor.core.publisher.Mono
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.mongo.repository.TaskSpecRepository

@ExperimentalCoroutinesApi
internal class DeleteTaskSpecMongoAdapterTest {
    private val taskSpecRepository = mockk<TaskSpecRepository>()
    private val adapter = DeleteTaskSpecMongoAdapter(taskSpecRepository)

    @Test
    @Tag(POSITIVE_TEST)
    fun `deleteTaskSpec should work properly`() = runTest {
        val taskId = "tid"
        val studyId = "sid"

        every {
            taskSpecRepository.deleteByIdAndStudyId(taskId, studyId)
        } returns Mono.empty()

        assertDoesNotThrow {
            adapter.deleteTaskSpec(taskId, studyId)
        }
    }
}
