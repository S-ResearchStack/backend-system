package researchstack.backend.application.service.study

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_WRITE
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESOURCE_STUDY
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.study.DeleteStudyUseCase
import researchstack.backend.application.port.outgoing.casbin.DeleteStudyPolicyOutPort
import researchstack.backend.application.port.outgoing.investigator.DeleteInvestigatorStudyRelationOutPort
import researchstack.backend.application.port.outgoing.study.DeleteStudyOutPort

@Service
class DeleteStudyService(
    private val deleteStudyOutPort: DeleteStudyOutPort,
    private val deleteStudyPolicyOutPort: DeleteStudyPolicyOutPort,
    private val deleteRelationOutPort: DeleteInvestigatorStudyRelationOutPort
) : DeleteStudyUseCase {
    @Role(actions = [ACTION_WRITE], resources = [RESOURCE_STUDY])
    override suspend fun deleteStudy(@Tenants studyId: String) {
        deleteStudyOutPort.deleteStudy(studyId)
        deleteStudyPolicyOutPort.deleteStudyPolicies(studyId)
        deleteRelationOutPort.deleteByStudyId(studyId)
    }
}
