package researchstack.backend.adapter.outgoing.mongo.study

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import reactor.core.publisher.Mono
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.mongo.repository.StudyRepository

@ExperimentalCoroutinesApi
internal class DeleteStudyMongoAdapterTest {
    private val studyRepository = mockk<StudyRepository>()
    private val adapter = DeleteStudyMongoAdapter(studyRepository)

    @Test
    @Tag(POSITIVE_TEST)
    fun `deleteStudy should work properly`() = runTest {
        val studyId = "id"
        every {
            studyRepository.deleteById(studyId)
        } returns Mono.empty()

        assertDoesNotThrow {
            adapter.deleteStudy(studyId)
        }
    }
}
