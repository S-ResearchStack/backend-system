package researchstack.backend.application.service.studydata

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.StudyDataTestUtil.Companion.dummySubjectInfo
import researchstack.backend.StudyDataTestUtil.Companion.testStudyId
import researchstack.backend.StudyDataTestUtil.Companion.testSubjectNumber
import researchstack.backend.application.port.outgoing.studydata.GetSubjectInfoOutPort
import researchstack.backend.application.port.outgoing.studydata.UpdateSubjectInfoOutPort
import researchstack.backend.enums.SubjectStatus

@ExperimentalCoroutinesApi
internal class UpdateStudyDataServiceTest {
    private val getSubjectInfoOutPort = mockk<GetSubjectInfoOutPort>()
    private val updateSubjectInfoMongoOutPort = mockk<UpdateSubjectInfoOutPort>()

    private val updateStudyDataService = UpdateStudyDataService(
        getSubjectInfoOutPort,
        updateSubjectInfoMongoOutPort
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `updateSubjectStatus should work properly`() = runTest {
        coEvery { getSubjectInfoOutPort.getSubjectInfo(testStudyId, testSubjectNumber) } returns dummySubjectInfo
        coEvery {
            updateSubjectInfoMongoOutPort.updateSubjectStatus(
                testStudyId,
                testSubjectNumber,
                SubjectStatus.PARTICIPATING,
                dummySubjectInfo.subjectId
            )
        } returns Unit

        assertDoesNotThrow {
            updateStudyDataService.updateSubjectStatus(testStudyId, testSubjectNumber, SubjectStatus.PARTICIPATING)
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `updateSubjectStatus should throw exception when getSubjectInfo fails`() = runTest {
        coEvery { getSubjectInfoOutPort.getSubjectInfo(testStudyId, testSubjectNumber) } throws NoSuchElementException()

        assertThrows<NoSuchElementException> {
            updateStudyDataService.updateSubjectStatus(testStudyId, testSubjectNumber, SubjectStatus.PARTICIPATING)
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `updateSubjectStatus should throw exception when updateSubjectInfo from mongo fails`() = runTest {
        coEvery { getSubjectInfoOutPort.getSubjectInfo(testStudyId, testSubjectNumber) } returns dummySubjectInfo
        coEvery {
            updateSubjectInfoMongoOutPort.updateSubjectStatus(
                testStudyId,
                testSubjectNumber,
                SubjectStatus.PARTICIPATING,
                dummySubjectInfo.subjectId
            )
        } throws Exception()

        assertThrows<Exception> {
            updateStudyDataService.updateSubjectStatus(testStudyId, testSubjectNumber, SubjectStatus.PARTICIPATING)
        }
    }
}
