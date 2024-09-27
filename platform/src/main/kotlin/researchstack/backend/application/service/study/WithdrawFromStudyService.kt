package researchstack.backend.application.service.study

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_WRITE
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.PARTICIPANT_PREFIX
import researchstack.backend.application.port.incoming.study.WithdrawFromStudyUseCase
import researchstack.backend.application.port.outgoing.casbin.DeleteRoleOutPort
import researchstack.backend.application.port.outgoing.study.DeleteSubjectNumberOutPort
import researchstack.backend.application.port.outgoing.study.DeleteSubjectStudyRelationOutPort
import researchstack.backend.application.port.outgoing.studydata.DeleteSubjectInfoOutPort
import researchstack.backend.application.service.permission.CheckPermissionService

@Service
class WithdrawFromStudyService(
    private val deleteSubjectNumberOutPort: DeleteSubjectNumberOutPort,
    private val deleteSubjectStudyRelationOutPort: DeleteSubjectStudyRelationOutPort,
    private val deleteSubjectInfoOutPort: DeleteSubjectInfoOutPort,
    private val deleteRoleOutPort: DeleteRoleOutPort,
    private val checkPermissionService: CheckPermissionService
) : WithdrawFromStudyUseCase {
    // TODO: enhance the Casbin string formatter
    override suspend fun withdrawFromStudy(subjectId: String, studyId: String) {
        checkPermissionService.checkPermission(studyId, "$PARTICIPANT_PREFIX$subjectId", ACTION_WRITE)

        deleteRoleOutPort.deleteParticipantRolesFromStudy(subjectId, studyId)
        deleteSubjectNumberOutPort.deleteSubjectNumber(studyId = studyId, subjectId = subjectId)
        deleteSubjectStudyRelationOutPort.deleteSubjectStudyRelation(subjectId = subjectId, studyId = studyId)
        deleteSubjectInfoOutPort.deleteSubjectInfo(studyId = studyId, subjectId = subjectId)
    }
}
