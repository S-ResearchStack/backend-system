package researchstack.backend.adapter.outgoing.mongo.education

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
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import researchstack.backend.EducationTestUtil
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.EducationalContentRepository

@ExperimentalCoroutinesApi
internal class UpdateEducationalContentMongoAdapterTest {
    private val educationalContentRepository = mockk<EducationalContentRepository>()
    private val adapter = UpdateEducationalContentMongoAdapter(educationalContentRepository)

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
    fun `updateEducationalContent should throw IllegalArgumentException when the given id is null`() = runTest {
        val educationalContent = EducationTestUtil.getEducationalContent()

        assertThrows<IllegalArgumentException> {
            adapter.updateEducationalContent(educationalContent)
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `updateEducationalContent should throw NoSuchElementException when there is no content matched with the given content id`() =
        runTest {
            val contentId = EducationTestUtil.contentId
            val educationalContent = EducationTestUtil.getEducationalContent(id = contentId)

            every {
                educationalContentRepository.findById(contentId)
            } answers {
                Mono.empty()
            }

            assertThrows<NoSuchElementException> {
                adapter.updateEducationalContent(educationalContent)
            }
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `updateEducationalContent should work properly`() = runTest {
        val contentId = EducationTestUtil.contentId
        val educationalContent = EducationTestUtil.getEducationalContent(id = contentId)
        val educationalContentEntity = educationalContent.toEntity()

        every {
            educationalContentRepository.findById(contentId)
        } answers {
            educationalContentEntity.toMono()
        }

        every {
            educationalContentRepository.save(educationalContentEntity)
        } answers {
            educationalContentEntity.toMono()
        }

        assertDoesNotThrow {
            adapter.updateEducationalContent(educationalContent)
        }
    }
}
