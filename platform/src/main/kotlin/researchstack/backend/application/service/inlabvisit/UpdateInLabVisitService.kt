package researchstack.backend.application.service.inlabvisit

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_WRITE
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESOURCE_IN_LAB_VISIT
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.inlabvisit.UpdateInLabVisitCommand
import researchstack.backend.application.port.incoming.inlabvisit.UpdateInLabVisitUseCase
import researchstack.backend.application.port.outgoing.inlabvisit.GetInLabVisitOutPort
import researchstack.backend.application.port.outgoing.inlabvisit.UpdateInLabVisitOutPort

@Service
class UpdateInLabVisitService(
    private val getInLabVisitOutPort: GetInLabVisitOutPort,
    private val updateInLabVisitOutPort: UpdateInLabVisitOutPort
) : UpdateInLabVisitUseCase {
    @Role(actions = [ACTION_WRITE], resources = [RESOURCE_IN_LAB_VISIT])
    override suspend fun updateInLabVisit(
        @Tenants studyId: String,
        inLabVisitId: String,
        command: UpdateInLabVisitCommand
    ) {
        val inLabVisit = getInLabVisitOutPort.getInLabVisit(inLabVisitId)
        val updated = inLabVisit.new(
            picId = command.picId,
            subjectNumber = command.subjectNumber,
            note = command.note,
            filePaths = command.filePaths,
            startTime = command.startTime,
            endTime = command.endTime
        )
        updateInLabVisitOutPort.updateInLabVisit(updated)
    }
}
