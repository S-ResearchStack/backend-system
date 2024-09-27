package researchstack.backend.application.service.investigator

import org.springframework.stereotype.Service
import researchstack.backend.application.port.incoming.investigator.RegisterInvestigatorCommand
import researchstack.backend.application.port.incoming.investigator.RegisterInvestigatorUseCase
import researchstack.backend.application.port.outgoing.casbin.AddRoleOutPort
import researchstack.backend.application.port.outgoing.investigator.GetInvestigatorStudyRelationOutPort
import researchstack.backend.application.port.outgoing.investigator.RegisterInvestigatorOutPort
import researchstack.backend.application.service.mapper.toDomain
import researchstack.backend.domain.investigator.Investigator

@Service
class RegisterInvestigatorService(
    private val registerInvestigatorOutPort: RegisterInvestigatorOutPort,
    private val getRelationOutPort: GetInvestigatorStudyRelationOutPort,
    private val addRoleOutPort: AddRoleOutPort
) : RegisterInvestigatorUseCase {
    override suspend fun registerInvestigator(
        investigatorId: String,
        email: String,
        command: RegisterInvestigatorCommand
    ): Investigator {
        val registeredInvestigator =
            registerInvestigatorOutPort.registerInvestigator(command.toDomain(investigatorId, email))
        getRelationOutPort.getRelationsByEmail(email).forEach {
            addRoleOutPort.addRole(investigatorId, it.studyId, it.role)
        }
        addRoleOutPort.addRolesForMyself(investigatorId)
        return registeredInvestigator
    }
}
