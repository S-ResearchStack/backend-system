package researchstack.backend

import researchstack.backend.TaskTestUtil.Companion.createActivityTaskSpec
import researchstack.backend.application.port.incoming.studydata.AddStudyDataCommand
import researchstack.backend.domain.studydata.StudyDataFile
import researchstack.backend.domain.studydata.StudyDataFolder
import researchstack.backend.domain.subject.SubjectInfo
import researchstack.backend.enums.StudyDataFileType
import researchstack.backend.enums.StudyDataType
import researchstack.backend.enums.SubjectStatus
import researchstack.backend.util.getUUID

class StudyDataTestUtil {
    companion object {
        const val testStudyId = "test-study-id"
        const val testSubjectNumber = "test-subject-number"
        private const val testSubjectId = "test-subject-id"
        private const val testSubjectId2 = "test-subject-id-2"
        val testFileType = StudyDataFileType.RAW_DATA
        const val testFilePath = "path/to/folder/test-file.txt"
        const val testFileName = "test-file-name"
        const val testFileSize = 100L
        const val testFilePreview = "file-content"
        const val testParentId = "test-parent-id"
        private const val testFolderName = "test-folder-name"

        val dummySubjectInfo = SubjectInfo(testStudyId, testSubjectNumber, SubjectStatus.PARTICIPATING, testSubjectId)

        val dummySubjectInfoList = listOf(
            SubjectInfo(testStudyId, "1", SubjectStatus.PARTICIPATING, testSubjectId),
            SubjectInfo(testStudyId, "2", SubjectStatus.PARTICIPATING, testSubjectId2)
        )

        val dummyTaskSpecList = listOf(
            createActivityTaskSpec()
        )

        val dummyStudyDataFolderList = listOf(
            createStudyDataFolder(),
            createStudyDataFolder()
        )

        val dummyStudyDataFileList = listOf(
            createStudyDataFile(),
            createStudyDataFile()
        )

        fun createAddStudyDataCommandFolderType() = AddStudyDataCommand(
            name = testFolderName,
            type = StudyDataType.FOLDER,
            parentId = testParentId,
            fileInfo = null
        )

        fun createAddStudyDataCommandFileType() = AddStudyDataCommand(
            name = testFileName,
            type = StudyDataType.FILE,
            parentId = testParentId,
            fileInfo = createFileDataInfo()
        )

        fun createFileDataInfo() = AddStudyDataCommand.FileDataInfo(
            fileType = testFileType,
            filePath = testFilePath,
            fileSize = testFileSize,
            filePreview = testFilePreview
        )

        fun createStudyDataFile() = StudyDataFile(
            id = getUUID(),
            name = testFileName,
            studyId = testStudyId,
            parentId = testParentId,
            fileType = testFileType,
            filePath = testFilePath,
            fileSize = testFileSize,
            filePreview = testFilePreview
        )

        fun createStudyDataFolder() = StudyDataFolder(
            id = getUUID(),
            name = testFolderName,
            studyId = testStudyId,
            parentId = testParentId
        )
    }
}
