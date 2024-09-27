package researchstack.backend.adapter.outgoing.mongo.studydata

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import researchstack.backend.adapter.outgoing.mongo.mapper.toDomain
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.StudyDataFileRepository
import researchstack.backend.adapter.outgoing.mongo.repository.StudyDataFolderRepository
import researchstack.backend.application.exception.AlreadyExistsException
import researchstack.backend.application.port.outgoing.studydata.AddStudyDataInfoOutPort
import researchstack.backend.domain.studydata.StudyDataFile
import researchstack.backend.domain.studydata.StudyDataFolder

@Component
class AddStudyDataInfoMongoAdapter(
    private val studyDataFolderRepository: StudyDataFolderRepository,
    private val studyDataFileRepository: StudyDataFileRepository
) : AddStudyDataInfoOutPort {
    override suspend fun addStudyDataInfo(studyDataFolder: StudyDataFolder) {
        studyDataFolderRepository.existsByStudyIdAndParentIdAndName(
            studyDataFolder.studyId,
            studyDataFolder.parentId,
            studyDataFolder.name
        )
            .flatMap { exist ->
                if (exist) {
                    Mono.error(AlreadyExistsException("${studyDataFolder.name} already exists."))
                } else {
                    when (studyDataFolder) {
                        is StudyDataFile -> studyDataFileRepository.save(studyDataFolder.toEntity())
                            .map { it.toDomain() }

                        else -> studyDataFolderRepository.save(studyDataFolder.toEntity()).map { it.toDomain() }
                    }
                }
            }.awaitSingle()
    }
}
