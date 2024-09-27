package researchstack.backend.adapter.outgoing.mongo.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import researchstack.backend.adapter.outgoing.mongo.entity.studydata.StudyDataFolderEntity
import researchstack.backend.domain.studydata.StudyDataFolder

@Mapper
abstract class StudyDataInfoMapper {
    @Mapping(target = "id", ignore = true)
    abstract fun toEntity(studyDataFolder: StudyDataFolder): StudyDataFolderEntity

    abstract fun toDomain(studyDataFolderEntity: StudyDataFolderEntity): StudyDataFolder
}

private val converter = Mappers.getMapper(StudyDataInfoMapper::class.java)

fun StudyDataFolderEntity.toDomain(): StudyDataFolder = converter.toDomain(this)

fun StudyDataFolder.toEntity(): StudyDataFolderEntity = converter.toEntity(this)
