package researchstack.backend.adapter.outgoing.mongo.inlabvisit

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import reactor.kotlin.core.publisher.toFlux
import researchstack.backend.InLabVisitTestUtil
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.InLabVisitRepository
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class GetInLabVisitMongoAdapterTest {
    private val inLabVisitRepository = mockk<InLabVisitRepository>(relaxed = true)
    private val adapter = GetInLabVisitMongoAdapter(inLabVisitRepository)

    @Test
    @Tag(POSITIVE_TEST)
    fun `getInLabVisitList should work properly`() = runTest {
        every {
            inLabVisitRepository.findAll()
        } answers {
            listOf(InLabVisitTestUtil.getInLabVisit().toEntity()).toFlux()
        }

        val inLabVisitList = assertDoesNotThrow {
            adapter.getInLabVisitList()
        }

        assertEquals(inLabVisitList.size, 1)
    }
}
