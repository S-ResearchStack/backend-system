package researchstack.backend.application.service.education

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_WRITE
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESOURCE_EDUCATIONAL_CONTENT
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.education.DeleteEducationalContentUseCase
import researchstack.backend.application.port.outgoing.education.DeleteEducationalContentOutPort

@Service
class DeleteEducationalContentService(
    private val deleteEducationalContentOutPort: DeleteEducationalContentOutPort
) : DeleteEducationalContentUseCase {
    @Role(actions = [ACTION_WRITE], resources = [RESOURCE_EDUCATIONAL_CONTENT])
    override suspend fun deleteEducationalContent(@Tenants studyId: String, contentId: String) {
        deleteEducationalContentOutPort.deleteEducationalContent(contentId)
    }
}
