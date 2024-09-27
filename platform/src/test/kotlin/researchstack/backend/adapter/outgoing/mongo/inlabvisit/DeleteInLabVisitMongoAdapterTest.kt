package researchstack.backend.adapter.outgoing.mongo.inlabvisit

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import reactor.core.publisher.Mono
import researchstack.backend.InLabVisitTestUtil
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.mongo.repository.InLabVisitRepository

@ExperimentalCoroutinesApi
internal class DeleteInLabVisitMongoAdapterTest {
    private val inLabVisitRepository = mockk<InLabVisitRepository>()
    private val adapter = DeleteInLabVisitMongoAdapter(inLabVisitRepository)

    @Test
    @Tag(POSITIVE_TEST)
    fun `deleteInLabVisit should work properly`() = runTest {
        val inLabVisitId = InLabVisitTestUtil.id

        every {
            inLabVisitRepository.deleteById(inLabVisitId)
        } answers {
            Mono.empty()
        }

        assertDoesNotThrow {
            adapter.deleteInLabVisit(inLabVisitId)
        }
    }
}
