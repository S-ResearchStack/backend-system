package researchstack.backend.application.service.role

import org.casbin.jcasbin.main.Enforcer
import org.springframework.stereotype.Service
import researchstack.backend.application.port.incoming.study.AddPolicyUseCase

@Service
class AddPolicyService(
    private val enforcer: Enforcer
) : AddPolicyUseCase {
    // TODO: Need to check authority. Ex) @Actions(["read", "write"])
    override suspend fun addPolicy(ptype: String, vararg vargs: String) {
        require(
            when (ptype) {
                "p" ->
                    enforcer.addPolicy(*vargs)

                else ->
                    enforcer.addNamedGroupingPolicy(ptype, *vargs)
            }
        ) {
            "Failed to add policy. ptype: $ptype, vargs: $vargs"
        }
    }
}
