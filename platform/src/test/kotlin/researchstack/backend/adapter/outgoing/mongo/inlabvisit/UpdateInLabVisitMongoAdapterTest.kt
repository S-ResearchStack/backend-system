package researchstack.backend.adapter.outgoing.mongo.inlabvisit

import com.linecorp.armeria.common.HttpMethod
import com.linecorp.armeria.common.HttpRequest
import com.linecorp.armeria.server.ServiceRequestContext
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import reactor.kotlin.core.publisher.toMono
import researchstack.backend.InLabVisitTestUtil
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.InLabVisitRepository

@ExperimentalCoroutinesApi
internal class UpdateInLabVisitMongoAdapterTest {
    private val inLabVisitRepository = mockk<InLabVisitRepository>()
    private val adapter = UpdateInLabVisitMongoAdapter(inLabVisitRepository)

    private val serviceRequestContext = ServiceRequestContext.builder(
        HttpRequest.of(HttpMethod.GET, "/test")
    ).build()

    @BeforeEach
    fun setup() {
        mockkStatic(ServiceRequestContext::class)
        coEvery {
            ServiceRequestContext.current()
        } returns serviceRequestContext
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `updateInLabVisit should work properly`() = runTest {
        val inLabVisit = InLabVisitTestUtil.getInLabVisit()
        val inLabVisitEntity = inLabVisit.toEntity()

        every {
            inLabVisitRepository.findById(InLabVisitTestUtil.id)
        } answers {
            inLabVisitEntity.toMono()
        }

        every {
            inLabVisitRepository.save(inLabVisitEntity)
        } answers {
            inLabVisitEntity.toMono()
        }

        assertDoesNotThrow {
            adapter.updateInLabVisit(inLabVisit)
        }
    }
}
