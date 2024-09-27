package researchstack.backend.application.service.education

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import researchstack.backend.EducationTestUtil
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.port.outgoing.education.DeleteEducationalContentOutPort

@ExperimentalCoroutinesApi
internal class DeleteEducationalContentServiceTest {
    private val deleteEducationalContentOutPort = mockk<DeleteEducationalContentOutPort>()
    private val deleteEducationalContentService = DeleteEducationalContentService(
        deleteEducationalContentOutPort
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `deleteEducationalContent should work properly`() = runTest {
        val studyId = EducationTestUtil.studyId
        val contentId = EducationTestUtil.contentId

        coEvery {
            deleteEducationalContentOutPort.deleteEducationalContent(contentId)
        } returns Unit

        assertDoesNotThrow {
            deleteEducationalContentService.deleteEducationalContent(studyId, contentId)
        }
    }
}
