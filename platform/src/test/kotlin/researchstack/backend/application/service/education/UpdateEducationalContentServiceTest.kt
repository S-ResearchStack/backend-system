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
import researchstack.backend.application.port.outgoing.education.GetEducationalContentOutPort
import researchstack.backend.application.port.outgoing.education.UpdateEducationalContentOutPort

@ExperimentalCoroutinesApi
internal class UpdateEducationalContentServiceTest {
    private val getEducationalContentOutPort = mockk<GetEducationalContentOutPort>()
    private val updateEducationalContentOutPort = mockk<UpdateEducationalContentOutPort>()
    private val updateEducationalContentService = UpdateEducationalContentService(
        getEducationalContentOutPort,
        updateEducationalContentOutPort
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `updateEducationalContent should work properly`() = runTest {
        val studyId = EducationTestUtil.studyId
        val contentId = EducationTestUtil.contentId
        val command = EducationTestUtil.getUpdateEducationalContentCommand()

        coEvery {
            getEducationalContentOutPort.getEducationalContent(contentId)
        } returns EducationTestUtil.getEducationalContent(id = contentId)

        coEvery {
            updateEducationalContentOutPort.updateEducationalContent(
                EducationTestUtil.getEducationalContent(id = contentId)
            )
        } returns Unit

        assertDoesNotThrow {
            updateEducationalContentService.updateEducationalContent(studyId, contentId, command)
        }
    }
}
