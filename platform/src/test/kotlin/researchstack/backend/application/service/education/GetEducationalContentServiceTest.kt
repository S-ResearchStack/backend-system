package researchstack.backend.application.service.education

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.backend.EducationTestUtil
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.port.outgoing.education.GetEducationalContentOutPort
import researchstack.backend.enums.EducationalContentType
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class GetEducationalContentServiceTest {
    private val getEducationalContentOutPort = mockk<GetEducationalContentOutPort>()
    private val getEducationalContentService = GetEducationalContentService(
        getEducationalContentOutPort
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `getEducationalContentList with studyId should work properly`() = runTest {
        val studyId = EducationTestUtil.studyId

        coEvery {
            getEducationalContentOutPort.getEducationalContentList()
        } returns listOf(EducationTestUtil.getEducationalContent(contentType = EducationalContentType.PDF))

        val contentList = getEducationalContentService.getEducationalContentList(studyId)
        assertEquals(contentList.size, 1)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getEducationalContentList with studyId and status should work properly`() = runTest {
        val studyId = EducationTestUtil.studyId
        val status = EducationTestUtil.status

        coEvery {
            getEducationalContentOutPort.getEducationalContentList(status)
        } returns listOf(EducationTestUtil.getEducationalContent())

        val contentList = getEducationalContentService.getEducationalContentList(studyId, status)
        assertEquals(contentList.size, 1)
    }
}
