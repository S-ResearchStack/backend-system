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
import org.springframework.dao.DuplicateKeyException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import researchstack.backend.EducationTestUtil
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.EducationalContentRepository
import researchstack.backend.application.exception.AlreadyExistsException
import researchstack.backend.enums.EducationalContentType
import researchstack.backend.enums.ScratchContentBlockType
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class CreateEducationalContentMongoAdapterTest {
    private val educationalContentRepository = mockk<EducationalContentRepository>()
    private val adapter = CreateEducationalContentMongoAdapter(educationalContentRepository)

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
    fun `createEducationalContent should throw AlreadyExistsException if educational content with the given study id and title already exist`() =
        runTest {
            val educationalContent = EducationTestUtil.getEducationalContent()

            every {
                educationalContentRepository.existsByTitle(
                    EducationTestUtil.title
                )
            } answers {
                true.toMono()
            }

            val exception = assertThrows<AlreadyExistsException> {
                adapter.createEducationalContent(educationalContent)
            }
            val expectedMessage = "educationalContent(" +
                "title: ${educationalContent.title}, " +
                ") already exists"
            assertEquals(expectedMessage, exception.message)
        }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `createEducationalContent should throw AlreadyExistsException when it fails to create because of a duplicate key`() =
        runTest {
            val educationalContent = EducationTestUtil.getEducationalContent()

            every {
                educationalContentRepository.existsByTitle(
                    EducationTestUtil.title
                )
            } answers {
                false.toMono()
            }

            every {
                educationalContentRepository.insert(educationalContent.toEntity())
            } answers {
                Mono.error(DuplicateKeyException("duplicate key"))
            }

            assertThrows<AlreadyExistsException> {
                adapter.createEducationalContent(educationalContent)
            }
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `createEducationalContent SCRATCH type with TEXT blocks should work properly`() = runTest {
        val educationalContent = EducationTestUtil.getEducationalContent(
            contentType = EducationalContentType.SCRATCH,
            blockType = ScratchContentBlockType.TEXT
        )
        val educationalContentEntity = educationalContent.toEntity()

        every {
            educationalContentRepository.existsByTitle(
                EducationTestUtil.title
            )
        } answers {
            false.toMono()
        }

        every {
            educationalContentRepository.insert(educationalContentEntity)
        } answers {
            educationalContentEntity.toMono()
        }

        assertDoesNotThrow {
            adapter.createEducationalContent(educationalContent)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `createEducationalContent SCRATCH type with IMAGE blocks should work properly`() = runTest {
        val educationalContent = EducationTestUtil.getEducationalContent(
            contentType = EducationalContentType.SCRATCH,
            blockType = ScratchContentBlockType.IMAGE
        )
        val educationalContentEntity = educationalContent.toEntity()

        every {
            educationalContentRepository.existsByTitle(
                EducationTestUtil.title
            )
        } answers {
            false.toMono()
        }

        every {
            educationalContentRepository.insert(educationalContentEntity)
        } answers {
            educationalContentEntity.toMono()
        }

        assertDoesNotThrow {
            adapter.createEducationalContent(educationalContent)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `createEducationalContent SCRATCH type with VIDEO blocks should work properly`() = runTest {
        val educationalContent = EducationTestUtil.getEducationalContent(
            contentType = EducationalContentType.SCRATCH,
            blockType = ScratchContentBlockType.VIDEO
        )
        val educationalContentEntity = educationalContent.toEntity()

        every {
            educationalContentRepository.existsByTitle(
                EducationTestUtil.title
            )
        } answers {
            false.toMono()
        }

        every {
            educationalContentRepository.insert(educationalContentEntity)
        } answers {
            educationalContentEntity.toMono()
        }

        assertDoesNotThrow {
            adapter.createEducationalContent(educationalContent)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `createEducationalContent PDF type should work properly`() = runTest {
        val educationalContent = EducationTestUtil.getEducationalContent(
            contentType = EducationalContentType.PDF
        )
        val educationalContentEntity = educationalContent.toEntity()

        every {
            educationalContentRepository.existsByTitle(
                EducationTestUtil.title
            )
        } answers {
            false.toMono()
        }

        every {
            educationalContentRepository.insert(educationalContentEntity)
        } answers {
            educationalContentEntity.toMono()
        }

        assertDoesNotThrow {
            adapter.createEducationalContent(educationalContent)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `createEducationalContent VIDEO type should work properly`() = runTest {
        val educationalContent = EducationTestUtil.getEducationalContent(
            contentType = EducationalContentType.VIDEO
        )
        val educationalContentEntity = educationalContent.toEntity()

        every {
            educationalContentRepository.existsByTitle(
                EducationTestUtil.title
            )
        } answers {
            false.toMono()
        }

        every {
            educationalContentRepository.insert(educationalContentEntity)
        } answers {
            educationalContentEntity.toMono()
        }

        assertDoesNotThrow {
            adapter.createEducationalContent(educationalContent)
        }
    }
}
