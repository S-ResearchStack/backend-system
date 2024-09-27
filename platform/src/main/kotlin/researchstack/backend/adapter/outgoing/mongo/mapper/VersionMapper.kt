package researchstack.backend.adapter.outgoing.mongo.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import researchstack.backend.adapter.outgoing.mongo.entity.VersionEntity
import researchstack.backend.domain.version.Version

@Mapper
abstract class VersionMapper {
    abstract fun toEntity(version: Version): VersionEntity
    abstract fun toDomain(versionEntity: VersionEntity): Version
}

private val converter = Mappers.getMapper(VersionMapper::class.java)

fun Version.toEntity() = converter.toEntity(this)

fun VersionEntity.toDomain() = converter.toDomain(this)
