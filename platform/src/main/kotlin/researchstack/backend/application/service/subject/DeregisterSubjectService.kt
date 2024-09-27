package researchstack.backend.application.service.subject

import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_WRITE
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.PUBLIC_TENANT
import researchstack.backend.adapter.role.Resources
import researchstack.backend.adapter.role.Role
import researchstack.backend.application.port.incoming.subject.DeregisterSubjectUseCase
import researchstack.backend.application.port.outgoing.casbin.DeleteRoleOutPort
import researchstack.backend.application.port.outgoing.subject.DeregisterSubjectOutPort
import researchstack.backend.domain.subject.Subject.SubjectId

@Service
class DeregisterSubjectService(
    private val deregisterSubjectOutPort: DeregisterSubjectOutPort,
    private val deleteRoleOutPort: DeleteRoleOutPort
) : DeregisterSubjectUseCase {
    @Role(actions = [ACTION_WRITE], tenants = [PUBLIC_TENANT])
    override suspend fun deregisterSubject(@Resources id: SubjectId) {
        deregisterSubjectOutPort.deregisterSubject(id)
        deleteRoleOutPort.deleteRolesForMyself(id.value)
    }
}
