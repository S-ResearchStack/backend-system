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
import researchstack.backend.StudyDataTestUtil.Companion.createAddStudyDataCommandFileType
import researchstack.backend.StudyDataTestUtil.Companion.createAddStudyDataCommandFolderType
import researchstack.backend.StudyDataTestUtil.Companion.createStudyDataFile
import researchstack.backend.StudyDataTestUtil.Companion.createStudyDataFolder
import researchstack.backend.StudyDataTestUtil.Companion.testFileName
import researchstack.backend.StudyDataTestUtil.Companion.testFilePath
import researchstack.backend.StudyDataTestUtil.Companion.testFilePreview
import researchstack.backend.StudyDataTestUtil.Companion.testFileSize
import researchstack.backend.StudyDataTestUtil.Companion.testParentId
import researchstack.backend.StudyDataTestUtil.Companion.testStudyId
import researchstack.backend.application.exception.NotFoundException
import researchstack.backend.application.port.outgoing.storage.DownloadObjectPort
import researchstack.backend.application.port.outgoing.studydata.AddStudyDataInfoOutPort
import researchstack.backend.application.port.outgoing.studydata.GetStudyDataOutPort

@ExperimentalCoroutinesApi
internal class AddStudyDataServiceTest {
    private val downloadObjectPort = mockk<DownloadObjectPort>()
    private val getStudyDataOutPort = mockk<GetStudyDataOutPort>()
    private val addStudyDataInfoOutPort = mockk<AddStudyDataInfoOutPort>(relaxed = true)
    private val addStudyDataService = AddStudyDataService(
        downloadObjectPort,
        getStudyDataOutPort,
        addStudyDataInfoOutPort
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `addStudyDataInfo should add a file when type is FILE`() = runTest {
        val testCommand = createAddStudyDataCommandFileType()
        coEvery { getStudyDataOutPort.verifyParentPresence(testParentId) } returns true
        coEvery { downloadObjectPort.getObjectSize(any()) } returns testFileSize
        coEvery { downloadObjectPort.getPartialObject(testFilePath) } returns testFilePreview
        coEvery { addStudyDataInfoOutPort.addStudyDataInfo(createStudyDataFile()) } returns Unit

        assertDoesNotThrow {
            addStudyDataService.addStudyDataInfo(testStudyId, testCommand)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `addStudyDataInfo should add a folder when type is FOLDER`() = runTest {
        val testCommand = createAddStudyDataCommandFolderType()
        coEvery { getStudyDataOutPort.verifyParentPresence(testParentId) } returns true
        coEvery { addStudyDataInfoOutPort.addStudyDataInfo(createStudyDataFolder()) } returns Unit

        assertDoesNotThrow {
            addStudyDataService.addStudyDataInfo(testStudyId, testCommand)
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `addStudyDataInfo should throw NotFoundException when parent is not found`() = runTest {
        val testCommand = createAddStudyDataCommandFolderType()
        coEvery { getStudyDataOutPort.verifyParentPresence(testParentId) } returns false

        assertThrows<NotFoundException> {
            addStudyDataService.addStudyDataInfo(testStudyId, testCommand)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `addStudyDataFileInfo should work properly when folder or file exists`() = runTest {
        coEvery { getStudyDataOutPort.verifyStudyDataPresence(testStudyId, any(), any()) } returns true
        coEvery { addStudyDataInfoOutPort.addStudyDataInfo(any()) } returns Unit
        coEvery { getStudyDataOutPort.getStudyDataFolderId(testStudyId, any(), any()) } returns testParentId

        assertDoesNotThrow {
            addStudyDataService.addStudyDataFileInfo(testStudyId, testFilePath, testFileName)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `addStudyDataFileInfo to add folder&file should work properly`() = runTest {
        coEvery { getStudyDataOutPort.verifyStudyDataPresence(testStudyId, any(), any()) } returns false
        coEvery { addStudyDataInfoOutPort.addStudyDataInfo(any()) } returns Unit
        coEvery { getStudyDataOutPort.getStudyDataFolderId(testStudyId, any(), any()) } returns testParentId
        coEvery { downloadObjectPort.getObjectSize(any()) } returns testFileSize

        assertDoesNotThrow {
            addStudyDataService.addStudyDataFileInfo(testStudyId, testFilePath, testFileName)
        }
    }
}
