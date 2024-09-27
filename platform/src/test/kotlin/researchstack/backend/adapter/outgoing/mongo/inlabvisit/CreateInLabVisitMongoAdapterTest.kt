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
import org.junit.jupiter.api.assertThrows
import org.springframework.dao.DuplicateKeyException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import researchstack.backend.InLabVisitTestUtil
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.InLabVisitRepository
import researchstack.backend.application.exception.AlreadyExistsException

@ExperimentalCoroutinesApi
internal class CreateInLabVisitMongoAdapterTest {
    private val inLabVisitRepository = mockk<InLabVisitRepository>()
    private val adapter = CreateInLabVisitMongoAdapter(inLabVisitRepository)

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
    @Tag(NEGATIVE_TEST)
    fun `createInLabVisit should throw AlreadyExistsException when it fails to create because of a duplicate key`() =
        runTest {
            val subjectNumber = InLabVisitTestUtil.subjectNumber
            val inLabVisit = InLabVisitTestUtil.getInLabVisit()

            every {
                inLabVisitRepository.existsBySubjectNumberAndStartTimeBetweenOrEndTimeBetween(
                    subjectNumber = subjectNumber,
                    startTimeFrom = any(),
                    startTimeTo = any(),
                    endTimeFrom = any(),
                    endTimeTo = any()
                )
            } answers {
                false.toMono()
            }

            every {
                inLabVisitRepository.insert(inLabVisit.toEntity())
            } answers {
                Mono.error(DuplicateKeyException("duplicate key"))
            }

            assertThrows<AlreadyExistsException> {
                adapter.createInLabVisit(inLabVisit)
            }
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `createInLabVisit should work properly`() = runTest {
        val subjectNumber = InLabVisitTestUtil.subjectNumber
        val inLabVisit = InLabVisitTestUtil.getInLabVisit()
        val inLabVisitEntity = inLabVisit.toEntity()

        every {
            inLabVisitRepository.existsBySubjectNumberAndStartTimeBetweenOrEndTimeBetween(
                subjectNumber = subjectNumber,
                startTimeFrom = any(),
                startTimeTo = any(),
                endTimeFrom = any(),
                endTimeTo = any()
            )
        } answers {
            false.toMono()
        }

        every {
            inLabVisitRepository.insert(inLabVisitEntity)
        } answers {
            inLabVisitEntity.toMono()
        }

        assertDoesNotThrow {
            adapter.createInLabVisit(inLabVisit)
        }
    }
}
