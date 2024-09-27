package researchstack.backend.adapter.outgoing.mongo.education

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import reactor.kotlin.core.publisher.toFlux
import researchstack.backend.EducationTestUtil
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.mongo.entity.education.EducationalContentEntity
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.EducationalContentRepository
import researchstack.backend.enums.EducationalContentType
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class GetEducationalContentMongoAdapterTest {
    private val educationalContentRepository = mockk<EducationalContentRepository>()
    private val adapter = GetEducationalContentMongoAdapter(educationalContentRepository)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getEducationalContentList should return empty list when there is no educational content`() = runTest {
        every {
            educationalContentRepository.findAll()
        } answers {
            listOf<EducationalContentEntity>().toFlux()
        }

        val educationalContentList = adapter.getEducationalContentList()

        assertEquals(educationalContentList.size, 0)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getEducationalContentList with status should return empty list when there is no educational content`() =
        runTest {
            val status = EducationTestUtil.status
            every {
                educationalContentRepository.findByStatus(status)
            } answers {
                listOf<EducationalContentEntity>().toFlux()
            }

            val educationalContentList = adapter.getEducationalContentList(status)

            assertEquals(educationalContentList.size, 0)
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getEducationalContentList should return empty list when there is no educational content corresponding to a given status`() =
        runTest {
            every {
                educationalContentRepository.findAll()
            } answers {
                listOf<EducationalContentEntity>().toFlux()
            }

            val educationalContentList = adapter.getEducationalContentList()

            assertEquals(educationalContentList.size, 0)
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getEducationalContentList with status should return empty list when there is no educational content corresponding to a given status`() =
        runTest {
            val status = EducationTestUtil.status

            every {
                educationalContentRepository.findByStatus(status)
            } answers {
                listOf<EducationalContentEntity>().toFlux()
            }

            val educationalContentList = adapter.getEducationalContentList(status)

            assertEquals(educationalContentList.size, 0)
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getEducationalContentList should work properly`() = runTest {
        val educationalContent = EducationTestUtil.getEducationalContent()

        every {
            educationalContentRepository.findAll()
        } answers {
            listOf(educationalContent.toEntity()).toFlux()
        }

        val educationalContentList = assertDoesNotThrow {
            adapter.getEducationalContentList()
        }

        assertEquals(educationalContentList.size, 1)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getEducationalContentList PDF type should work properly`() = runTest {
        val educationalContent = EducationTestUtil.getEducationalContent(
            contentType = EducationalContentType.PDF
        )

        every {
            educationalContentRepository.findAll()
        } answers {
            listOf(educationalContent.toEntity()).toFlux()
        }

        val educationalContentList = assertDoesNotThrow {
            adapter.getEducationalContentList()
        }

        assertEquals(educationalContentList.size, 1)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getEducationalContentList Video type should work properly`() = runTest {
        val educationalContent = EducationTestUtil.getEducationalContent(
            contentType = EducationalContentType.VIDEO
        )

        every {
            educationalContentRepository.findAll()
        } answers {
            listOf(educationalContent.toEntity()).toFlux()
        }

        val educationalContentList = assertDoesNotThrow {
            adapter.getEducationalContentList()
        }

        assertEquals(educationalContentList.size, 1)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getEducationalContentList with status should work properly`() = runTest {
        val status = EducationTestUtil.status
        val educationalContent = EducationTestUtil.getEducationalContent()

        every {
            educationalContentRepository.findByStatus(status)
        } answers {
            listOf(educationalContent.toEntity()).toFlux()
        }

        val educationalContentList = assertDoesNotThrow {
            adapter.getEducationalContentList(status)
        }

        assertEquals(educationalContentList.size, 1)
    }
}
