package researchstack.backend.application.service.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import researchstack.backend.application.port.incoming.investigator.RegisterInvestigatorCommand
import researchstack.backend.application.port.incoming.investigator.UpdateInvestigatorCommand
import researchstack.backend.domain.common.Email
import researchstack.backend.domain.investigator.Investigator

@Mapper
abstract class InvestigatorMapper {
    @Mapping(target = "roles", ignore = true)
    abstract fun toDomain(command: RegisterInvestigatorCommand, id: String, email: String): Investigator

    @Mapping(target = "roles", ignore = true)
    abstract fun toDomain(command: UpdateInvestigatorCommand, id: String, email: String): Investigator

    fun mapEmail(email: String): Email = Email(email)
}

private val converter = Mappers.getMapper(InvestigatorMapper::class.java)

fun RegisterInvestigatorCommand.toDomain(investigatorId: String, email: String): Investigator =
    converter.toDomain(this, investigatorId, email)

fun UpdateInvestigatorCommand.toDomain(investigatorId: String, email: String): Investigator =
    converter.toDomain(this, investigatorId, email)
