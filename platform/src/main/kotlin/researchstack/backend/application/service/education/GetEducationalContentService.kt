package researchstack.backend.application.service.education

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_READ
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESOURCE_EDUCATIONAL_CONTENT
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.education.GetEducationalContentUseCase
import researchstack.backend.application.port.outgoing.education.GetEducationalContentOutPort
import researchstack.backend.domain.education.EducationalContent
import researchstack.backend.enums.EducationalContentStatus

@Service
class GetEducationalContentService(
    private val getEducationalContentOutPort: GetEducationalContentOutPort
) : GetEducationalContentUseCase {
    @Role(actions = [ACTION_READ], resources = [RESOURCE_EDUCATIONAL_CONTENT])
    override suspend fun getEducationalContent(@Tenants studyId: String, contentId: String): EducationalContent {
        return getEducationalContentOutPort.getEducationalContent(contentId)
    }

    @Role(actions = [ACTION_READ], resources = [RESOURCE_EDUCATIONAL_CONTENT])
    override suspend fun getEducationalContentList(
        @Tenants studyId: String,
        status: EducationalContentStatus?
    ): List<EducationalContent> {
        return if (status != null) {
            getEducationalContentOutPort.getEducationalContentList(status)
        } else {
            getEducationalContentOutPort.getEducationalContentList()
        }
    }
}
