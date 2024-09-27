package researchstack.backend.adapter.outgoing.mongo.studydata

import kotlinx.coroutines.reactive.awaitFirstOrDefault
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.config.pagination
import researchstack.backend.adapter.outgoing.mongo.mapper.toDomain
import researchstack.backend.adapter.outgoing.mongo.repository.StudyDataFileRepository
import researchstack.backend.adapter.outgoing.mongo.repository.StudyDataFolderRepository
import researchstack.backend.application.port.outgoing.studydata.GetStudyDataOutPort
import researchstack.backend.domain.studydata.StudyDataFile
import researchstack.backend.domain.studydata.StudyDataFolder
import researchstack.backend.enums.StudyDataType

@Component
class GetStudyDataInfoMongoAdapter(
    private val studyDataFolderRepository: StudyDataFolderRepository,
    private val studyDataFileRepository: StudyDataFileRepository
) : GetStudyDataOutPort {
    override suspend fun getStudyDataFolderList(
        studyId: String,
        parentId: String,
        page: Long?,
        size: Long?
    ): List<StudyDataFolder> {
        return studyDataFolderRepository
            .findAllByStudyIdAndParentIdAndType(studyId, parentId, StudyDataType.FOLDER)
            .pagination(page, size)
            .map { it.toDomain() }.collectList().awaitSingle()
    }

    override suspend fun getStudyDataFolderListCount(studyId: String, parentId: String): Long {
        return studyDataFolderRepository
            .findAllByStudyIdAndParentIdAndType(studyId, parentId, StudyDataType.FOLDER)
            .count()
            .awaitSingle()
    }

    override suspend fun getStudyDataFileList(
        studyId: String,
        parentId: String,
        page: Long?,
        size: Long?
    ): List<StudyDataFile> {
        return studyDataFileRepository
            .findAllByStudyIdAndParentIdAndType(studyId, parentId, StudyDataType.FILE)
            .pagination(page, size)
            .map { it.toDomain() }.collectList().awaitSingle()
    }

    override suspend fun getStudyDataFileListCount(studyId: String, parentId: String): Long {
        return studyDataFileRepository
            .findAllByStudyIdAndParentIdAndType(studyId, parentId, StudyDataType.FILE)
            .count()
            .awaitSingle()
    }

    override suspend fun verifyParentPresence(id: String): Boolean {
        return studyDataFolderRepository.existsById(id).awaitFirstOrDefault(false)
    }

    override suspend fun verifyStudyDataPresence(studyId: String, parentId: String?, name: String): Boolean {
        return studyDataFolderRepository
            .existsByStudyIdAndParentIdAndName(studyId, parentId, name)
            .awaitFirstOrDefault(false)
    }

    override suspend fun getStudyDataFolderId(studyId: String, parentId: String, name: String): String? {
        return studyDataFolderRepository
            .findByStudyIdAndParentIdAndName(studyId, parentId, name)
            .awaitSingleOrNull()?.id
    }
}
