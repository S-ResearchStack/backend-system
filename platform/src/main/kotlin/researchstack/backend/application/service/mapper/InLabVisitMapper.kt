package researchstack.backend.application.service.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import researchstack.backend.adapter.outgoing.mongo.entity.inlabvisit.InLabVisitEntity
import researchstack.backend.application.port.incoming.inlabvisit.CreateInLabVisitCommand
import researchstack.backend.domain.inlabvisit.InLabVisit

@Mapper
abstract class InLabVisitMapper {
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    abstract fun toDomain(command: CreateInLabVisitCommand, creatorId: String): InLabVisit

    abstract fun toEntity(inLabVisit: InLabVisit): InLabVisitEntity
}

private val converter = Mappers.getMapper(InLabVisitMapper::class.java)

fun CreateInLabVisitCommand.toDomain(creatorId: String): InLabVisit =
    converter.toDomain(this, creatorId)
