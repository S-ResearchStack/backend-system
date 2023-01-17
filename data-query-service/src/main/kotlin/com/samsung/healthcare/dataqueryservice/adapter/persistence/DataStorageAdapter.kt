package com.samsung.healthcare.dataqueryservice.adapter.persistence

import com.samsung.healthcare.dataqueryservice.adapter.persistence.common.get
import com.samsung.healthcare.dataqueryservice.application.config.ApplicationProperties
import com.samsung.healthcare.dataqueryservice.application.port.output.QueryDataPort
import com.samsung.healthcare.dataqueryservice.application.port.output.QueryDataResult
import org.springframework.stereotype.Component
import java.sql.Connection
import java.sql.DriverManager
import java.util.Properties

@Component
class DataStorageAdapter(
    private val config: ApplicationProperties,
) : QueryDataPort {

    override fun executeQuery(projectId: String, accountId: String?, sql: String): QueryDataResult =
        executeQuery(projectId, accountId, sql, emptyList())

    override fun executeQuery(projectId: String, accountId: String?, sql: String, params: List<Any>): QueryDataResult {
        require(projectId.isNotBlank())

        val data = mutableListOf<Map<String, Any?>>()
        val columns = mutableListOf<String>()

        dbConnection(projectId, accountId).use { conn ->
            val preparedStatement = conn.prepareStatement(sql).apply {
                params.forEachIndexed { index, any ->
                    setObject(index + 1, any)
                }
            }
            val resultSet = preparedStatement.executeQuery()
            val numCol: Int = resultSet.metaData.columnCount

            for (i in 1..numCol) {
                columns.add(resultSet.metaData.getColumnName(i))
            }
            while (resultSet.next()) {
                val datum = mutableMapOf<String, Any?>()
                for (i in 1..numCol) {
                    datum[resultSet.metaData.getColumnName(i)] = resultSet.get(i)
                }
                data.add(datum)
            }
        }

        return QueryDataResult(columns, data)
    }

    private fun dbConnection(projectId: String, accountId: String?): Connection {
        val properties = Properties()
        properties.setProperty("user", accountId ?: config.trino.user)

        return DriverManager.getConnection(
            "${config.trino.url}/${config.trino.catalog}/${config.db.prefix}${projectId}${config.db.postfix}",
            properties,
        )
    }
}
