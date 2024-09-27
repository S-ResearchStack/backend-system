package researchstack.backend.adapter.outgoing.mongo.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import researchstack.backend.adapter.outgoing.mongo.entity.dashboard.DashboardEntity
import researchstack.backend.domain.dashboard.Dashboard

@Mapper
abstract class DashboardMapper {

    abstract fun toEntity(dashboard: Dashboard): DashboardEntity

    abstract fun toDomain(dashboardEntity: DashboardEntity): Dashboard
}

private val converter = Mappers.getMapper(DashboardMapper::class.java)

fun Dashboard.toEntity(): DashboardEntity =
    converter.toEntity(this)

fun DashboardEntity.toDomain(): Dashboard =
    converter.toDomain(this)
