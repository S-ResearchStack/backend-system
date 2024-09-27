package researchstack.backend.adapter.outgoing.mongo.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import researchstack.backend.adapter.outgoing.mongo.entity.dashboard.ChartEntity
import researchstack.backend.domain.dashboard.Chart

@Mapper
abstract class ChartMapper {
    abstract fun toEntity(chart: Chart): ChartEntity

    abstract fun toDomain(chartEntity: ChartEntity): Chart
}

private val converter = Mappers.getMapper(ChartMapper::class.java)

fun Chart.toEntity(): ChartEntity =
    converter.toEntity(this)

fun ChartEntity.toDomain(): Chart =
    converter.toDomain(this)
