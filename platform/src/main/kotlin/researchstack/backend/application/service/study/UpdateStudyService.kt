package researchstack.backend.application.service.study

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_WRITE
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESOURCE_STUDY
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.study.UpdateStudyCommand
import researchstack.backend.application.port.incoming.study.UpdateStudyUseCase
import researchstack.backend.application.port.outgoing.study.UpdateStudyOutPort

@Service
class UpdateStudyService(
    private val updateStudyOutPort: UpdateStudyOutPort
) : UpdateStudyUseCase {

    @Role(actions = [ACTION_WRITE], resources = [RESOURCE_STUDY])
    override suspend fun updateStudy(@Tenants studyId: String, updateStudyCommand: UpdateStudyCommand) {
        val study = updateStudyCommand.toDomain(studyId)
        updateStudyOutPort.updateStudy(study)
    }
}
