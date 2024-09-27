package researchstack.backend.adapter.outgoing.mongo.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import researchstack.backend.adapter.outgoing.mongo.entity.healthdata.HealthDataGroupEntity
import researchstack.backend.domain.study.HealthDataGroup

@Mapper
abstract class HealthDataGroupMapper {
    abstract fun toDomain(healthDataGroupEntity: HealthDataGroupEntity): HealthDataGroup
}

private val converter = Mappers.getMapper(HealthDataGroupMapper::class.java)

fun HealthDataGroupEntity.toDomain(): HealthDataGroup = converter.toDomain(this)
