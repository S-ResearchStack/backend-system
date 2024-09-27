package researchstack.backend.adapter.outgoing.mongo.education

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import reactor.core.publisher.Mono
import researchstack.backend.EducationTestUtil
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.mongo.repository.EducationalContentRepository

@ExperimentalCoroutinesApi
internal class DeleteEducationalContentMongoAdapterTest {
    private val educationalContentRepository = mockk<EducationalContentRepository>()
    private val adapter = DeleteEducationalContentMongoAdapter(educationalContentRepository)

    @Test
    @Tag(POSITIVE_TEST)
    fun `deleteEducationalContent should work properly`() = runTest {
        val contentId = EducationTestUtil.contentId

        every {
            educationalContentRepository.deleteById(contentId)
        } answers {
            Mono.empty()
        }

        assertDoesNotThrow {
            adapter.deleteEducationalContent(contentId)
        }
    }
}
