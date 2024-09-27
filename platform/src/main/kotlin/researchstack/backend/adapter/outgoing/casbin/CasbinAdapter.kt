package researchstack.backend.adapter.outgoing.casbin

import org.casbin.jcasbin.main.Enforcer
import org.casbin.jcasbin.model.Model
import org.casbin.jcasbin.persist.file_adapter.FileAdapter
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_READ
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_WRITE
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ADMIN_READ_RESOURCE_GROUP
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ADMIN_USER_GROUP
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ADMIN_WRITE_RESOURCE_GROUP
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.MANAGER_READ_RESOURCE_GROUP
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.MANAGER_USER_GROUP
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.MANAGER_WRITE_RESOURCE_GROUP
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.PARTICIPANT_GROUP_PREFIX
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.PARTICIPANT_PREFIX
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.PARTICIPANT_READ_RESOURCE_GROUP
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.PARTICIPANT_USER_GROUP
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.PARTICIPANT_WRITE_RESOURCE_GROUP
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.PUBLIC_TENANT
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESEARCHER_READ_RESOURCE_GROUP
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESEARCHER_USER_GROUP
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESEARCHER_WRITE_RESOURCE_GROUP
import researchstack.backend.application.exception.AlreadyExistsException
import researchstack.backend.application.port.outgoing.casbin.AddRoleOutPort
import researchstack.backend.application.port.outgoing.casbin.CreateStudyPolicyOutPort
import researchstack.backend.application.port.outgoing.casbin.DeleteRoleOutPort
import researchstack.backend.application.port.outgoing.casbin.DeleteStudyPolicyOutPort
import researchstack.backend.application.port.outgoing.casbin.GetRoleOutPort
import researchstack.backend.application.port.outgoing.casbin.UpdateRoleOutPort
import researchstack.backend.config.CasbinProperties

@Component
class CasbinAdapter(
    casbinProperties: CasbinProperties,
    private val enforcer: Enforcer
) : CreateStudyPolicyOutPort,
    DeleteStudyPolicyOutPort,
    AddRoleOutPort,
    GetRoleOutPort,
    UpdateRoleOutPort,
    DeleteRoleOutPort {
    init {
        if (ClassPathResource(casbinProperties.policy).exists()) {
            val m = Model()
            m.loadModelFromText(enforcer.model.saveModelToText())
            val fromEnforcer = Enforcer(m, FileAdapter(ClassPathResource(casbinProperties.policy).inputStream))
            fromEnforcer.policy.iterator().forEach {
                enforcer.addPolicy(it)
            }
            fromEnforcer.groupingPolicy.iterator().forEach {
                enforcer.addGroupingPolicy(it)
            }
            fromEnforcer.getNamedGroupingPolicy("g2").iterator().forEach {
                enforcer.addNamedGroupingPolicy("g2", it)
            }
            fromEnforcer.getNamedGroupingPolicy("g3").iterator().forEach {
                enforcer.addNamedGroupingPolicy("g3", it)
            }
        }
    }

    override suspend fun createStudyPolicies(userId: String, studyId: String) {
        require(userId.isNotBlank()) { ExceptionMessage.EMPTY_USER_ID }
        require(studyId.isNotBlank()) { ExceptionMessage.EMPTY_STUDY_ID }

        enforcer.addPolicy(ADMIN_USER_GROUP, studyId, ADMIN_READ_RESOURCE_GROUP, ACTION_READ)
        enforcer.addPolicy(ADMIN_USER_GROUP, studyId, ADMIN_WRITE_RESOURCE_GROUP, ACTION_WRITE)
        enforcer.addPolicy(ADMIN_USER_GROUP, studyId, PARTICIPANT_GROUP_PREFIX + studyId, ACTION_READ)
        enforcer.addPolicy(ADMIN_USER_GROUP, studyId, PARTICIPANT_GROUP_PREFIX + studyId, ACTION_WRITE)
        enforcer.addPolicy(MANAGER_USER_GROUP, studyId, MANAGER_READ_RESOURCE_GROUP, ACTION_READ)
        enforcer.addPolicy(MANAGER_USER_GROUP, studyId, MANAGER_WRITE_RESOURCE_GROUP, ACTION_WRITE)
        enforcer.addPolicy(MANAGER_USER_GROUP, studyId, PARTICIPANT_GROUP_PREFIX + studyId, ACTION_READ)
        enforcer.addPolicy(MANAGER_USER_GROUP, studyId, PARTICIPANT_GROUP_PREFIX + studyId, ACTION_WRITE)
        enforcer.addPolicy(RESEARCHER_USER_GROUP, studyId, RESEARCHER_READ_RESOURCE_GROUP, ACTION_READ)
        enforcer.addPolicy(RESEARCHER_USER_GROUP, studyId, RESEARCHER_WRITE_RESOURCE_GROUP, ACTION_WRITE)
        enforcer.addPolicy(PARTICIPANT_USER_GROUP, studyId, PARTICIPANT_READ_RESOURCE_GROUP, ACTION_READ)
        enforcer.addPolicy(PARTICIPANT_USER_GROUP, studyId, PARTICIPANT_WRITE_RESOURCE_GROUP, ACTION_WRITE)
        enforcer.addNamedGroupingPolicy("g", userId, ADMIN_USER_GROUP, studyId)
    }

    override suspend fun deleteStudyPolicies(studyId: String) {
        require(studyId.isNotBlank()) { ExceptionMessage.EMPTY_STUDY_ID }

        enforcer.policy.filter {
            it[1] == studyId
        }.forEach {
            enforcer.removePolicy(it)
        }
        enforcer.groupingPolicy.filter {
            it[2] == studyId
        }.forEach {
            enforcer.removeGroupingPolicy(it)
        }
        enforcer.getNamedGroupingPolicy("g3").filter {
            it[1] == PARTICIPANT_GROUP_PREFIX + studyId
        }.forEach {
            enforcer.removeNamedGroupingPolicy("g3", it)
        }
    }

    override suspend fun addRole(userId: String, studyId: String, role: String): Boolean {
        return enforcer.addNamedGroupingPolicy("g", userId, role, studyId)
    }

    override suspend fun addParticipantRole(userId: String, studyId: String): Boolean {
        enforcer.getRolesForUserInDomain(userId, studyId).let {
            if (it.isNotEmpty()) {
                throw AlreadyExistsException("subject $userId already has roles $it")
            }
        }
        enforcer.addNamedGroupingPolicy("g3", PARTICIPANT_PREFIX + userId, PARTICIPANT_GROUP_PREFIX + studyId)
        enforcer.addPolicy(userId, studyId, PARTICIPANT_PREFIX + userId, ACTION_READ)
        enforcer.addPolicy(userId, studyId, PARTICIPANT_PREFIX + userId, ACTION_WRITE)
        return addRole(userId, studyId, PARTICIPANT_USER_GROUP)
    }

    // TODO: roles does not have a prefix as studyId anymore
    override suspend fun getRoles(userId: String): List<String> {
        return enforcer.groupingPolicy.filter {
            it[0] == userId
        }.map {
            "${it[2]}$ROLE_DELIMITER${it[1]}"
        }
    }

    override suspend fun getRolesInStudy(userId: String, studyId: String): List<String> {
        return enforcer.getRolesForUserInDomain(userId, studyId)
    }

    override suspend fun updateRole(userId: String, studyId: String, role: String): String {
        val roles = enforcer.getRolesForUserInDomain(userId, studyId)
        if (roles.isNotEmpty()) {
            roles.forEach {
                enforcer.removeNamedGroupingPolicy("g", userId, it, studyId)
            }
        }

        val result = enforcer.addNamedGroupingPolicy("g", userId, role, studyId)
        if (!result) {
            throw AlreadyExistsException("The role already exists.")
        }

        val updatedRoleGroup = enforcer.getRolesForUserInDomain(userId, studyId).first()
        return updatedRoleGroup
    }

    override suspend fun deleteRolesFromStudy(userId: String, studyId: String) {
        val roles = getRolesInStudy(userId, studyId)
        roles.forEach { role -> enforcer.deleteRoleForUserInDomain(userId, role, studyId) }
    }

    override suspend fun deleteParticipantRolesFromStudy(userId: String, studyId: String) {
        deleteRolesFromStudy(userId, studyId)
        enforcer.removeNamedGroupingPolicy("g3", PARTICIPANT_PREFIX + userId, PARTICIPANT_GROUP_PREFIX + studyId)
        enforcer.removePolicy(userId, studyId, PARTICIPANT_PREFIX + userId, ACTION_READ)
        enforcer.removePolicy(userId, studyId, PARTICIPANT_PREFIX + userId, ACTION_WRITE)
    }

    override suspend fun deleteRolesForMyself(userId: String) {
        enforcer.removePolicy(userId, PUBLIC_TENANT, userId, ACTION_READ)
        enforcer.removePolicy(userId, PUBLIC_TENANT, userId, ACTION_WRITE)
    }

    override suspend fun addRolesForMyself(userId: String) {
        enforcer.addPolicy(userId, PUBLIC_TENANT, userId, ACTION_READ)
        enforcer.addPolicy(userId, PUBLIC_TENANT, userId, ACTION_WRITE)
    }

    companion object {
        private const val ROLE_DELIMITER = '_'
    }
}
