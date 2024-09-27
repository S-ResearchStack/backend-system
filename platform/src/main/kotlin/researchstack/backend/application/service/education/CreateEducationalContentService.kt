package researchstack.backend.application.service.education

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_WRITE
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESOURCE_EDUCATIONAL_CONTENT
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.education.CreateEducationalContentCommand
import researchstack.backend.application.port.incoming.education.CreateEducationalContentResponse
import researchstack.backend.application.port.incoming.education.CreateEducationalContentUseCase
import researchstack.backend.application.port.outgoing.education.CreateEducationalContentOutPort
import researchstack.backend.application.service.mapper.toDomain

@Service
class CreateEducationalContentService(
    private val createEducationalContentOutPort: CreateEducationalContentOutPort
) : CreateEducationalContentUseCase {
    @Role(actions = [ACTION_WRITE], resources = [RESOURCE_EDUCATIONAL_CONTENT])
    override suspend fun createEducationalContent(
        @Tenants studyId: String,
        investigatorId: String,
        command: CreateEducationalContentCommand
    ): CreateEducationalContentResponse {
        val educationalContent =
            createEducationalContentOutPort.createEducationalContent(command.toDomain(investigatorId))
        return CreateEducationalContentResponse(educationalContent.id)
    }
}
