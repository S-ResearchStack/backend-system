package researchstack.backend.application.service.permission

import org.springframework.stereotype.Service
import researchstack.backend.adapter.role.Actions
import researchstack.backend.adapter.role.Resources
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants

@Service
class CheckPermissionService {
    @Role()
    fun checkPermission(@Tenants studyId: String, @Resources roles: List<String>, @Actions actions: String) = true

    @Role()
    fun checkPermission(@Tenants studyId: String, @Resources roles: String, @Actions actions: String) = true
}
