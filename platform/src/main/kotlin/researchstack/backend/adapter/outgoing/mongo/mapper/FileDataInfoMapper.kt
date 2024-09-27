package researchstack.backend.adapter.outgoing.mongo.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import researchstack.backend.adapter.outgoing.mongo.entity.studydata.StudyDataFileEntity
import researchstack.backend.domain.studydata.StudyDataFile
import java.time.ZonedDateTime

@Mapper
abstract class FileDataInfoMapper {
    abstract fun toEntity(fileDataInfo: StudyDataFile): StudyDataFileEntity

    fun mapCreatedAt(createdAt: ZonedDateTime): String = createdAt.toString()

    abstract fun toDomain(studyDataFileEntity: StudyDataFileEntity): StudyDataFile

    fun mapCreatedAt(createdAt: String): ZonedDateTime = ZonedDateTime.parse(createdAt)
}

private val converter = Mappers.getMapper(FileDataInfoMapper::class.java)

fun StudyDataFileEntity.toDomain(): StudyDataFile = converter.toDomain(this)

fun StudyDataFile.toEntity(): StudyDataFileEntity = converter.toEntity(this)
