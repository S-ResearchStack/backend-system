package researchstack.backend.adapter.outgoing.mongo.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import researchstack.backend.adapter.outgoing.mongo.entity.AppLogEntity
import researchstack.backend.domain.applog.AppLog

@Mapper
abstract class AppLogMapper {
    @Mapping(target = "id", ignore = true)
    abstract fun toEntity(appLog: AppLog): AppLogEntity
    abstract fun toDomain(appLogEntity: AppLogEntity): AppLog
}

private val converter = Mappers.getMapper(AppLogMapper::class.java)

fun AppLog.toEntity(): AppLogEntity = converter.toEntity(this)

fun AppLogEntity.toDomain(): AppLog = converter.toDomain(this)
