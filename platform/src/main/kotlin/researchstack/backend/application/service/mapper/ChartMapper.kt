package researchstack.backend.application.service.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import researchstack.backend.application.port.incoming.dashboard.ChartResponse
import researchstack.backend.application.port.incoming.dashboard.CreateChartCommand
import researchstack.backend.application.port.incoming.dashboard.UpdateChartCommand
import researchstack.backend.domain.dashboard.Chart

@Mapper
abstract class ChartMapper {
    @Mapping(target = "id", ignore = true)
    abstract fun toDomain(command: CreateChartCommand, studyId: String, dashboardId: String): Chart

    fun mapConfigSpecific(configSpecific: Map<String, Any>): String =
        toDomainConfigSpecific(configSpecific)

    abstract fun toResponse(chart: Chart): ChartResponse

    fun mapConfigSpecific(configSpecific: String): Map<String, Any> =
        toResponseConfigSpecific(configSpecific)
}

private val converter = Mappers.getMapper(ChartMapper::class.java)
private val gson = Gson()
private val responseConfigSpecificType = object : TypeToken<Map<String, Any>>() {}.type

fun CreateChartCommand.toDomain(studyId: String, dashboardId: String): Chart =
    converter.toDomain(this, studyId, dashboardId)

fun UpdateChartCommand.toDomainConfigSpecific(): String? =
    if (configSpecific != null) {
        toDomainConfigSpecific(configSpecific)
    } else {
        null
    }

fun Chart.toResponse(): ChartResponse =
    converter.toResponse(this)

private fun toDomainConfigSpecific(configSpecific: Map<String, Any>): String =
    gson.toJson(configSpecific)

private fun toResponseConfigSpecific(configSpecific: String): Map<String, Any> =
    gson.fromJson(configSpecific, responseConfigSpecificType)
