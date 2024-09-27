package researchstack.backend.application.service.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import researchstack.backend.application.port.incoming.dashboard.CreateDashboardCommand
import researchstack.backend.domain.dashboard.Dashboard

@Mapper
abstract class DashboardMapper {
    abstract fun toDomain(command: CreateDashboardCommand, studyId: String): Dashboard
}

private val converter = Mappers.getMapper(DashboardMapper::class.java)

fun CreateDashboardCommand.toDomain(studyId: String): Dashboard =
    converter.toDomain(this, studyId)
