package researchstack.backend.application.service.education

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_WRITE
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESOURCE_EDUCATIONAL_CONTENT
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.education.UpdateEducationalContentCommand
import researchstack.backend.application.port.incoming.education.UpdateEducationalContentUseCase
import researchstack.backend.application.port.outgoing.education.GetEducationalContentOutPort
import researchstack.backend.application.port.outgoing.education.UpdateEducationalContentOutPort
import researchstack.backend.application.service.mapper.toDomainContent

@Service
class UpdateEducationalContentService(
    private val getEducationalContentOutPort: GetEducationalContentOutPort,
    private val updateEducationalContentOutPort: UpdateEducationalContentOutPort
) : UpdateEducationalContentUseCase {
    @Role(actions = [ACTION_WRITE], resources = [RESOURCE_EDUCATIONAL_CONTENT])
    override suspend fun updateEducationalContent(
        @Tenants studyId: String,
        contentId: String,
        command: UpdateEducationalContentCommand
    ) {
        val educationalContent = getEducationalContentOutPort.getEducationalContent(contentId)
        val updated = educationalContent.new(
            title = command.title,
            type = command.type,
            status = command.status,
            category = command.category,
            publisherId = command.publisherId,
            modifierId = command.modifierId,
            publishedAt = command.publishedAt,
            modifiedAt = command.modifiedAt,
            content = command.toDomainContent()
        )
        updateEducationalContentOutPort.updateEducationalContent(updated)
    }
}
