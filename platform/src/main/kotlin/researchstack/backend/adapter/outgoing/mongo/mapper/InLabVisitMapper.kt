package researchstack.backend.adapter.outgoing.mongo.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import researchstack.backend.adapter.outgoing.mongo.entity.inlabvisit.InLabVisitEntity
import researchstack.backend.domain.inlabvisit.InLabVisit

@Mapper
abstract class InLabVisitMapper {
    abstract fun toEntity(inLabVisit: InLabVisit): InLabVisitEntity

    abstract fun toDomain(inLabVisitEntity: InLabVisitEntity): InLabVisit
}

private val converter = Mappers.getMapper(InLabVisitMapper::class.java)

fun InLabVisit.toEntity(): InLabVisitEntity =
    converter.toEntity(this)

fun InLabVisitEntity.toDomain(): InLabVisit =
    converter.toDomain(this)
