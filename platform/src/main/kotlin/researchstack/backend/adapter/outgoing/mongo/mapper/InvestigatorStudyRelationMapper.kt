package researchstack.backend.adapter.outgoing.mongo.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import researchstack.backend.adapter.outgoing.mongo.entity.studydata.InvestigatorStudyRelationEntity
import researchstack.backend.domain.common.Email
import researchstack.backend.domain.investigator.InvestigatorStudyRelation

@Mapper
abstract class InvestigatorStudyRelationMapper {
    @Mapping(target = "id", ignore = true)
    abstract fun toEntity(investigatorStudyRelation: InvestigatorStudyRelation): InvestigatorStudyRelationEntity

    fun mapEmail(email: Email): String = email.value

    abstract fun toDomain(invitationEntity: InvestigatorStudyRelationEntity): InvestigatorStudyRelation

    fun mapEmail(email: String): Email = Email(email)
}

private val converter = Mappers.getMapper(InvestigatorStudyRelationMapper::class.java)

fun InvestigatorStudyRelation.toEntity(): InvestigatorStudyRelationEntity =
    converter.toEntity(this)

fun InvestigatorStudyRelationEntity.toDomain(): InvestigatorStudyRelation =
    converter.toDomain(this)
