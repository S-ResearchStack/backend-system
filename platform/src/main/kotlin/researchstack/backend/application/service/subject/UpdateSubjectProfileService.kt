package researchstack.backend.application.service.subject

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_WRITE
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.PUBLIC_TENANT
import researchstack.backend.adapter.role.Resources
import researchstack.backend.adapter.role.Role
import researchstack.backend.application.port.incoming.subject.UpdateSubjectProfileCommand
import researchstack.backend.application.port.incoming.subject.UpdateSubjectProfileUseCase
import researchstack.backend.application.port.outgoing.subject.UpdateSubjectProfileOutPort
import researchstack.backend.domain.subject.Subject.SubjectId

@Service
class UpdateSubjectProfileService(
    private val updateSubjectProfileOutPort: UpdateSubjectProfileOutPort
) : UpdateSubjectProfileUseCase {
    @Role(actions = [ACTION_WRITE], tenants = [PUBLIC_TENANT])
    override suspend fun updateSubjectProfile(@Resources id: SubjectId, command: UpdateSubjectProfileCommand) {
        val subjectProfile = command.toDomain(id.value)
        updateSubjectProfileOutPort.updateSubjectProfile(id.value, subjectProfile)
    }
}
