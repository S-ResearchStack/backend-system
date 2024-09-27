package researchstack.backend.application.service.studydata

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.StudyDataTestUtil.Companion.dummyStudyDataFileList
import researchstack.backend.StudyDataTestUtil.Companion.dummyStudyDataFolderList
import researchstack.backend.StudyDataTestUtil.Companion.dummySubjectInfoList
import researchstack.backend.StudyDataTestUtil.Companion.dummyTaskSpecList
import researchstack.backend.StudyDataTestUtil.Companion.testParentId
import researchstack.backend.StudyDataTestUtil.Companion.testStudyId
import researchstack.backend.application.port.outgoing.storage.DownloadObjectPort
import researchstack.backend.application.port.outgoing.studydata.GetStudyDataOutPort
import researchstack.backend.application.port.outgoing.studydata.GetSubjectInfoOutPort
import researchstack.backend.application.port.outgoing.task.GetTaskResultOutPort
import researchstack.backend.application.port.outgoing.task.GetTaskSpecOutPort
import researchstack.backend.enums.StudyDataType
import researchstack.backend.enums.SubjectStatus
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class GetStudyDataServiceTest {
    private val getSubjectInfoOutPort = mockk<GetSubjectInfoOutPort>()
    private val downloadObjectPort = mockk<DownloadObjectPort>()
    private val getTaskSpecOutPort = mockk<GetTaskSpecOutPort>()
    private val getTaskResultOutPort = mockk<GetTaskResultOutPort>(relaxed = true)
    private val getStudyDataOutPort = mockk<GetStudyDataOutPort>(relaxed = true)

    private val getStudyDataService = GetStudyDataService(
        getSubjectInfoOutPort,
        downloadObjectPort,
        getTaskSpecOutPort,
        getTaskResultOutPort,
        getStudyDataOutPort
    )

    private val studyDataFileListCount = 7L
    private val studyDataFolderListCount = 8L
    private val subjectInfoListCount = 10L

    @Test
    @Tag(POSITIVE_TEST)
    fun `getSubjectInfoList should work properly`() = runTest {
        coEvery { getSubjectInfoOutPort.getSubjectInfoList(testStudyId) } returns dummySubjectInfoList

        val response = getStudyDataService.getSubjectInfoList(testStudyId)
        assertEquals(dummySubjectInfoList[0].subjectNumber, response[0].subjectNumber)
        assertEquals(SubjectStatus.PARTICIPATING, response[0].status)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getSubjectInfoList should work properly when includeTaskProgress is true`() = runTest {
        coEvery { getSubjectInfoOutPort.getSubjectInfoList(testStudyId) } returns dummySubjectInfoList
        coEvery { getTaskSpecOutPort.getTaskSpecs(testStudyId) } returns dummyTaskSpecList

        val response = getStudyDataService.getSubjectInfoList(testStudyId, true)
        assertEquals(dummySubjectInfoList[0].subjectNumber, response[0].subjectNumber)
        assertEquals(SubjectStatus.PARTICIPATING, response[0].status)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getSubjectInfoListCount should work properly`() = runTest {
        coEvery {
            getSubjectInfoOutPort.getSubjectInfoListCount(testStudyId)
        } returns subjectInfoListCount

        val response = getStudyDataService.getSubjectInfoListCount(testStudyId)
        assertEquals(subjectInfoListCount, response.totalCount)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getStudyDataInfoList should work properly when studyDataType is folder`() = runTest {
        coEvery {
            getStudyDataOutPort.getStudyDataFolderList(
                testStudyId,
                testParentId,
                null,
                null
            )
        } returns dummyStudyDataFolderList

        val response = getStudyDataService.getStudyDataInfoList(testStudyId, testParentId, StudyDataType.FOLDER)

        assertEquals(dummyStudyDataFolderList[0].id, response[0].id)
        assertEquals(dummyStudyDataFolderList[1].id, response[1].id)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getStudyDataInfoList should work properly when studyDataType is file`() = runTest {
        coEvery {
            getStudyDataOutPort.getStudyDataFileList(
                testStudyId,
                testParentId,
                null,
                null
            )
        } returns dummyStudyDataFileList

        val response = getStudyDataService.getStudyDataInfoList(testStudyId, testParentId, StudyDataType.FILE)

        assertEquals(dummyStudyDataFileList[0].id, response[0].id)
        assertEquals(dummyStudyDataFileList[1].id, response[1].id)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getStudyDataInfoListCount should work properly when studyDataType is folder`() = runTest {
        coEvery {
            getStudyDataOutPort.getStudyDataFolderListCount(testStudyId, testParentId)
        } returns studyDataFolderListCount

        val response = getStudyDataService.getStudyDataInfoListCount(testStudyId, testParentId, StudyDataType.FOLDER)
        assertEquals(studyDataFolderListCount, response.totalCount)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getStudyDataInfoListCount should work properly when studyDataType is file`() = runTest {
        coEvery {
            getStudyDataOutPort.getStudyDataFileListCount(testStudyId, testParentId)
        } returns studyDataFileListCount

        val response = getStudyDataService.getStudyDataInfoListCount(testStudyId, testParentId, StudyDataType.FILE)
        assertEquals(studyDataFileListCount, response.totalCount)
    }
}
