package com.samsung.healthcare.dataqueryservice.application.port.input

data class User(val userId: String, val profiles: List<Attribute>, val lastSyncTime: String)

data class Attribute(val key: String, val value: String?)

data class Pageable(val offset: Int, val limit: Int) {
    init {
        require(0 <= offset)
        require(limit in 1..100)
    }
}

enum class ParticipantListColumn {
    ID, EMAIL, AVG_HR, TOTAL_STEPS, LAST_SYNCED,
}

enum class Sort {
    ASC, DESC,
}

interface UserDataQuery {
    fun fetchUsers(
        projectId: String,
        userIds: List<String>,
        includeAttributes: List<String>,
        accountId: String,
    ): List<User>

    fun fetchUsers(
        projectId: String,
        orderByColumn: ParticipantListColumn,
        orderBySort: Sort,
        pageable: Pageable,
        includeAttributes: List<String>,
        accountId: String,
    ): List<User>

    fun fetchUsers(
        projectId: String,
        includeAttributes: List<String>,
        accountId: String,
    ): List<User>
}
