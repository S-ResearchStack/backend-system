package researchstack.backend.application.service.studydata

import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_WRITE
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESOURCE_STUDY_DATA
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.studydata.UpdateSubjectUseCase
import researchstack.backend.application.port.outgoing.studydata.GetSubjectInfoOutPort
import researchstack.backend.application.port.outgoing.studydata.UpdateSubjectInfoOutPort
import researchstack.backend.enums.SubjectStatus

@Component
class UpdateStudyDataService(
    private val getSubjectInfoOutPort: GetSubjectInfoOutPort,
    private val updateSubjectInfoOutPort: UpdateSubjectInfoOutPort
) : UpdateSubjectUseCase {
    @Role(actions = [ACTION_WRITE], resources = [RESOURCE_STUDY_DATA])
    override suspend fun updateSubjectStatus(@Tenants studyId: String, subjectNumber: String, status: SubjectStatus) {
        val subjectInfo = getSubjectInfoOutPort.getSubjectInfo(
            studyId = studyId,
            subjectNumber = subjectNumber
        )
        updateSubjectInfoOutPort.updateSubjectStatus(
            studyId = studyId,
            subjectNumber = subjectNumber,
            status = status,
            subjectId = subjectInfo.subjectId
        )

        updateSubjectInfoOutPort.updateSubjectStatus(
            studyId = studyId,
            subjectNumber = subjectNumber,
            status = subjectInfo.status, // revert status
            subjectId = subjectInfo.subjectId
        )
    }
}
