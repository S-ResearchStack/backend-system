package com.samsung.healthcare.dataqueryservice.adapter.persistence

import com.samsung.healthcare.account.domain.Role
import com.samsung.healthcare.dataqueryservice.adapter.persistence.common.get
import com.samsung.healthcare.dataqueryservice.application.config.ApplicationProperties
import com.samsung.healthcare.dataqueryservice.application.context.AuthContext
import com.samsung.healthcare.dataqueryservice.application.port.output.QueryDataPort
import com.samsung.healthcare.dataqueryservice.application.port.output.QueryDataResult
import org.springframework.stereotype.Component
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.util.Properties

@Component
class DataStorageAdapter(
    private val config: ApplicationProperties,
) : QueryDataPort {

    override fun executeQuery(projectId: String, accountId: String?, sql: String): QueryDataResult =
        executeQuery(projectId, accountId, sql, emptyList())

    override fun executeQuery(projectId: String, accountId: String?, sql: String, params: List<Any>): QueryDataResult {
        require(projectId.isNotBlank())

        return dbConnection(projectId, accountId).use { conn ->
            conn.prepareStatement(sql).use { preparedStatement ->
                preparedStatement.apply {
                    params.forEachIndexed { index, any ->
                        setObject(index + 1, any)
                    }
                }
                preparedStatement.executeQuery().use { resultSet ->
                    resultSet.toQueryDataResult()
                }
            }
        }
    }

    private fun ResultSet.toQueryDataResult(): QueryDataResult {
        val data = mutableListOf<Map<String, Any?>>()
        val columns = (1..metaData.columnCount).map {
            metaData.getColumnName(it)
        }

        while (next()) {
            val datum = mutableMapOf<String, Any?>()
            for (i in 1..metaData.columnCount) {
                datum[metaData.getColumnName(i)] = get(i)
            }
            data.add(datum)
        }
        return QueryDataResult(columns, data)
    }

    private fun dbConnection(projectId: String, accountId: String?): Connection {
        val properties = Properties()
        properties.setProperty("user", accountId ?: config.trino.user)

        val role = AuthContext.getValue("project:$projectId")
        requireNotNull(role)

        val url = if (role == Role.DATA_SCIENTIST)
            "${config.trino.url}/${config.trino.deIdentifiedCatalog}/" +
                "${config.db.prefix}${projectId}${config.db.postfix}"
        else
            "${config.trino.url}/${config.trino.originalCatalog}/${config.db.prefix}${projectId}${config.db.postfix}"

        return DriverManager.getConnection(url, properties)
    }
}
