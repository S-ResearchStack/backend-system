package researchstack.backend.application.service.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import researchstack.backend.application.port.incoming.investigator.InviteInvestigatorCommand
import researchstack.backend.domain.common.Email
import researchstack.backend.domain.investigator.InvestigatorStudyRelation

@Mapper
abstract class InvestigatorStudyRelationMapper {
    abstract fun toDomain(command: InviteInvestigatorCommand): InvestigatorStudyRelation

    fun mapEmail(email: String): Email = Email(email)
}

private val converter = Mappers.getMapper(InvestigatorStudyRelationMapper::class.java)

fun InviteInvestigatorCommand.toDomain(): InvestigatorStudyRelation =
    converter.toDomain(this)
