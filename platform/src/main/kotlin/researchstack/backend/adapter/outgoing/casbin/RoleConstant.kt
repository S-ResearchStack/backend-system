package researchstack.backend.adapter.outgoing.casbin

object RoleConstant {
    const val RESOURCE_INVESTIGATOR = "investigator"
    const val RESOURCE_EDUCATIONAL_CONTENT = "educationalContent"
    const val RESOURCE_FILE = "file"
    const val RESOURCE_HEALTH_DATA = "healthData"
    const val RESOURCE_IN_LAB_VISIT = "inLabVisit"
    const val RESOURCE_STUDY = "study"
    const val RESOURCE_PARTICIPATION_REQUIREMENT = "participationRequirement"
    const val RESOURCE_DASHBOARD = "dashboard"
    const val RESOURCE_CHART = "chart"
    const val RESOURCE_STUDY_DATA = "studyData"
    const val RESOURCE_TASK_SPEC = "taskSpec"
    const val RESOURCE_TASK_RESULT = "taskResult"

    const val ACTION_READ = "read"
    const val ACTION_WRITE = "write"

    const val ADMIN_USER_GROUP = "studyAdmin"
    const val MANAGER_USER_GROUP = "studyManager"
    const val RESEARCHER_USER_GROUP = "studyResearcher"
    const val PARTICIPANT_USER_GROUP = "studyParticipant"

    const val ADMIN_READ_RESOURCE_GROUP = "adminReadResources"
    const val ADMIN_WRITE_RESOURCE_GROUP = "adminWriteResources"
    const val MANAGER_READ_RESOURCE_GROUP = "managerReadResources"
    const val MANAGER_WRITE_RESOURCE_GROUP = "managerWriteResources"
    const val RESEARCHER_READ_RESOURCE_GROUP = "researcherReadResources"
    const val RESEARCHER_WRITE_RESOURCE_GROUP = "researcherWriteResources"
    const val PARTICIPANT_READ_RESOURCE_GROUP = "participantReadResources"
    const val PARTICIPANT_WRITE_RESOURCE_GROUP = "participantWriteResources"

    const val PARTICIPANT_GROUP_PREFIX = "participantGroup_"
    const val PARTICIPANT_PREFIX = "participant_"

    const val PUBLIC_TENANT = "________"
}
