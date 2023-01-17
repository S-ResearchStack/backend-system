package com.samsung.healthcare.dataqueryservice.application.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.samsung.healthcare.dataqueryservice.application.port.input.Attribute
import com.samsung.healthcare.dataqueryservice.application.port.input.Pageable
import com.samsung.healthcare.dataqueryservice.application.port.input.ParticipantListColumn
import com.samsung.healthcare.dataqueryservice.application.port.input.Sort
import com.samsung.healthcare.dataqueryservice.application.port.input.User
import com.samsung.healthcare.dataqueryservice.application.port.input.UserDataQuery
import com.samsung.healthcare.dataqueryservice.application.port.output.QueryDataPort
import org.springframework.stereotype.Service
import java.sql.Timestamp

@Service
class UserDataQueryService(
    private val queryDataPort: QueryDataPort,
    private val objectMapper: ObjectMapper,
) : UserDataQuery {
    override fun fetchUsers(
        projectId: String,
        userIds: List<String>,
        includeAttributes: List<String>,
        accountId: String
    ): List<User> =
        queryDataPort.executeQuery(
            projectId, accountId, makeQueryToGetAttributesOfUsers(userIds.size), userIds
        ).data.map {
            it.toUser(includeAttributes)
        }

    override fun fetchUsers(
        projectId: String,
        orderByColumn: ParticipantListColumn,
        orderBySort: Sort,
        pageable: Pageable,
        includeAttributes: List<String>,
        accountId: String
    ): List<User> {
        println(makeGetUserQuery(pageable.offset, pageable.limit, orderByColumn, orderBySort))
        return queryDataPort.executeQuery(
            projectId,
            accountId,
            makeGetUserQuery(pageable.offset, pageable.limit, orderByColumn, orderBySort)
        )
            .data
            .map { it.toUser(includeAttributes) }
    }

    override fun fetchUsers(
        projectId: String,
        includeAttributes: List<String>,
        accountId: String,
    ): List<User> =
        queryDataPort.executeQuery(projectId, accountId, GET_USER_QUERY)
            .data
            .map { it.toUser(includeAttributes) }

    private fun Map<String, Any?>.toUser(includeAttributes: List<String>): User {
        val profile = objectMapper.readValue<Map<String, Any>>(this[PROFILE_COLUMN] as String)
            .filterKeys { includeAttributes.contains(it) }
        return User(
            this[USER_ID_COLUMN] as String,
            profile.map { (k, v) -> Attribute(k, v.toString()) },
            (this[LAST_SYNC_TIME_COLUMN] as Timestamp).toString()
        )
    }
}
