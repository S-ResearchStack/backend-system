package researchstack.backend.application.service.studydata

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import researchstack.backend.application.exception.NotFoundException
import researchstack.backend.application.port.incoming.studydata.AddStudyDataCommand
import researchstack.backend.application.port.incoming.studydata.AddStudyDataUseCase
import researchstack.backend.application.port.outgoing.storage.DownloadObjectPort
import researchstack.backend.application.port.outgoing.studydata.AddStudyDataInfoOutPort
import researchstack.backend.application.port.outgoing.studydata.GetStudyDataOutPort
import researchstack.backend.config.MESSAGE_LOG_EXTENSION
import researchstack.backend.config.META_INFO_EXTENSION
import researchstack.backend.config.RAW_DATA_EXTENSION
import researchstack.backend.config.SLASH
import researchstack.backend.config.STUDY_DATA_ROOT_ID
import researchstack.backend.domain.studydata.StudyDataFile
import researchstack.backend.domain.studydata.StudyDataFolder
import researchstack.backend.enums.StudyDataFileType
import researchstack.backend.enums.StudyDataType
import researchstack.backend.util.getUUID

@Service
class AddStudyDataService(
    private val downloadObjectPort: DownloadObjectPort,
    private val getStudyDataOutPort: GetStudyDataOutPort,
    private val addStudyDataInfoOutPort: AddStudyDataInfoOutPort
) : StudyDataBaseService(downloadObjectPort), AddStudyDataUseCase {
    private val logger = LoggerFactory.getLogger(AddStudyDataService::class.java)

    override suspend fun addStudyDataInfo(
        studyId: String,
        studyDataCommand: AddStudyDataCommand
    ) {
        val parentExists = getStudyDataOutPort.verifyParentPresence(studyDataCommand.parentId)
        if (!parentExists) {
            throw NotFoundException("Parent(id: ${studyDataCommand.parentId}) is not found")
        }
        if (studyDataCommand.type == StudyDataType.FILE && studyDataCommand.fileInfo != null) {
            addStudyDataFile(
                studyId,
                studyDataCommand.name,
                studyDataCommand.parentId,
                studyDataCommand.fileInfo
            )
        } else {
            addStudyDataFolder(studyId, studyDataCommand.name, studyDataCommand.parentId)
        }
    }

    private suspend fun addStudyDataFolder(studyId: String, name: String, parentId: String) {
        val studyDataFolder = StudyDataFolder(
            id = getUUID(),
            name = name,
            studyId = studyId,
            parentId = parentId
        )
        addStudyDataInfoOutPort.addStudyDataInfo(studyDataFolder)
    }

    private suspend fun addStudyDataFile(
        studyId: String,
        name: String,
        parentId: String,
        fileInfo: AddStudyDataCommand.FileDataInfo
    ) {
        val studyDataFile = StudyDataFile(
            id = getUUID(),
            name = name,
            studyId = studyId,
            parentId = parentId,
            fileType = getStudyDataFileType(name),
            filePath = fileInfo.filePath,
            fileSize = downloadObjectPort.getObjectSize(fileInfo.filePath),
            filePreview = getPreview(fileInfo.fileType, fileInfo.filePath)
        )
        addStudyDataInfoOutPort.addStudyDataInfo(studyDataFile)
    }

    override suspend fun addStudyDataFileInfo(studyId: String, filePath: String, fileName: String) {
        val parentFolderList = filePath.split(SLASH).filter { it.isNotEmpty() }
        // 1. Get Study's parentId
        var parentFolderId = getStudyDataOutPort.getStudyDataFolderId(studyId, STUDY_DATA_ROOT_ID, studyId)

        // 2. Create studyDataFolder
        for (folderName in parentFolderList) {
            val studyDataFolderExists = getStudyDataOutPort.verifyStudyDataPresence(studyId, parentFolderId, folderName)
            if (!studyDataFolderExists && parentFolderId != null) {
                addStudyDataFolder(studyId, folderName, parentFolderId)
            } else {
                logger.debug("Folder(name: $folderName) already exists.")
            }
            parentFolderId = getStudyDataOutPort.getStudyDataFolderId(studyId, parentFolderId!!, folderName)
        }

        // 3. Create studyDataFile
        val studyDataFileExists =
            getStudyDataOutPort.verifyStudyDataPresence(studyId, parentFolderList.last(), filePath)
        if (!studyDataFileExists) {
            addStudyDataFile(studyId, fileName, parentFolderId!!, filePath)
        }
    }

    private suspend fun addStudyDataFile(
        studyId: String,
        fileName: String,
        parentId: String,
        filePath: String
    ) {
        val fileType = getStudyDataFileType(fileName)
        val fileFullPath = studyId.plus(SLASH).plus(filePath).plus(SLASH).plus(fileName)
        val studyDataFile = StudyDataFile(
            id = getUUID(),
            name = fileName,
            studyId = studyId,
            parentId = parentId,
            fileType = fileType,
            filePath = filePath.plus(SLASH).plus(fileName),
            fileSize = downloadObjectPort.getObjectSize(fileFullPath),
            filePreview = getPreview(fileType, fileFullPath)
        )
        addStudyDataInfoOutPort.addStudyDataInfo(studyDataFile)
    }

    private suspend fun getStudyDataFileType(fileName: String): StudyDataFileType {
        return when {
            fileName.endsWith(RAW_DATA_EXTENSION) -> StudyDataFileType.RAW_DATA
            fileName.endsWith(META_INFO_EXTENSION) -> StudyDataFileType.META_INFO
            fileName.endsWith(MESSAGE_LOG_EXTENSION) -> StudyDataFileType.MESSAGE_LOG
            else -> StudyDataFileType.UNSPECIFIED
        }
    }
}
