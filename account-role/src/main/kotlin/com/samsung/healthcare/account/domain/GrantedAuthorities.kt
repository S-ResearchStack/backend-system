package com.samsung.healthcare.account.domain

import org.springframework.security.core.GrantedAuthority

interface GlobalAuthority : GrantedAuthority

object CreateStudyAuthority : GlobalAuthority {
    override fun getAuthority(): String = "create-study"
}

object CreateRoleAuthority : GlobalAuthority {
    override fun getAuthority(): String = "create-role"
}

interface ProjectAuthority : GrantedAuthority

data class AssignRoleAuthority(val projectId: String) : ProjectAuthority {
    override fun getAuthority(): String = "assign-role:$projectId"
}

data class ReadStudyOverviewAuthority(val projectId: String) : ProjectAuthority {
    override fun getAuthority(): String = "read-study-overview:$projectId"
}

data class ReadParticipantDataAuthority(val projectId: String) : ProjectAuthority {
    override fun getAuthority(): String = "read-participant-data:$projectId"
}

data class ReadDeIdentifiedParticipantDataAuthority(val projectId: String) : ProjectAuthority {
    override fun getAuthority(): String = "read-de-identified-participant-data:$projectId"
}

data class AccessInLabVisitAuthority(val projectId: String) : ProjectAuthority {
    override fun getAuthority(): String = "access-in-lab-visit:$projectId"
}

data class AccessTaskAuthority(val projectId: String) : ProjectAuthority {
    override fun getAuthority(): String = "access-task:$projectId"
}

data class AccessDocumentAuthority(val projectId: String) : ProjectAuthority {
    override fun getAuthority(): String = "access-document:$projectId"
}

data class ReadAggSensorDataAuthority(val projectId: String) : ProjectAuthority {
    override fun getAuthority(): String = "read-aggregated-sensor-data:$projectId"
}

data class QueryRawDataAuthority(val projectId: String) : ProjectAuthority {
    override fun getAuthority(): String = "query-raw-data:$projectId"
}

data class QueryDeIdentifiedDataAuthority(val projectId: String) : ProjectAuthority {
    override fun getAuthority(): String = "query-de-identified-data:$projectId"
}

data class ReadProjectMemberAuthority(val projectId: String) : ProjectAuthority {
    override fun getAuthority(): String = "read-project-member:$projectId"
}

data class AccessProjectMemberAuthority(val projectId: String) : ProjectAuthority {
    override fun getAuthority(): String = "access-project-member:$projectId"
}
