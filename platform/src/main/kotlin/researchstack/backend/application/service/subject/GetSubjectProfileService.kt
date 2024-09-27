package researchstack.backend.application.service.subject

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_READ
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.PARTICIPANT_PREFIX
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.PUBLIC_TENANT
import researchstack.backend.adapter.role.Resources
import researchstack.backend.adapter.role.Role
import researchstack.backend.application.port.incoming.subject.GetSubjectUseCase
import researchstack.backend.application.port.outgoing.studydata.GetSubjectInfoOutPort
import researchstack.backend.application.port.outgoing.subject.GetSubjectProfileOutPort
import researchstack.backend.application.service.permission.CheckPermissionService
import researchstack.backend.domain.subject.Subject
import researchstack.backend.domain.subject.Subject.SubjectId
import researchstack.backend.domain.subject.SubjectStatusInfo

@Service
class GetSubjectProfileService(
    private val getSubjectInfoOutPort: GetSubjectInfoOutPort,
    private val getSubjectProfileOutPort: GetSubjectProfileOutPort,
    private val checkPermissionService: CheckPermissionService
) : GetSubjectUseCase {
    @Role(actions = [ACTION_READ], tenants = [PUBLIC_TENANT])
    override suspend fun getSubjectProfile(@Resources id: SubjectId): Subject {
        return getSubjectProfileOutPort.getSubjectProfile(id)
    }

    // TODO: enhance the Casbin string formatter
    override suspend fun getSubjectStatus(subjectId: SubjectId, studyId: String): SubjectStatusInfo {
        checkPermissionService.checkPermission(studyId, "$PARTICIPANT_PREFIX${subjectId.value}", ACTION_READ)

        val subjectNumber = getSubjectProfileOutPort.getSubjectNumber(studyId, subjectId.value)
        val subjectInfo = getSubjectInfoOutPort.getSubjectInfo(studyId, subjectNumber)
        return SubjectStatusInfo(subjectInfo.status)
    }
}
