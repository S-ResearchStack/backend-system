package com.samsung.healthcare.dataqueryservice.application.service

import com.samsung.healthcare.dataqueryservice.application.exception.TableNotFoundException
import com.samsung.healthcare.dataqueryservice.application.port.input.Column
import com.samsung.healthcare.dataqueryservice.application.port.input.Table
import com.samsung.healthcare.dataqueryservice.application.port.input.TableDataQuery
import com.samsung.healthcare.dataqueryservice.application.port.output.QueryDataPort
import io.trino.sql.tree.Identifier
import org.springframework.stereotype.Service
import java.sql.SQLException

@Service
class TableDataQueryService(
    private val queryDataPort: QueryDataPort,
) : TableDataQuery {
    override fun listAllTables(projectId: String, accountId: String): List<Table> {
        val result = queryDataPort.executeQuery(projectId, accountId, SHOW_TABLE_QUERY)
        return result.data.mapNotNull { data ->
            data.values.first() as? String
        }.map { Table(it) }
    }

    override fun columnsOfTable(projectId: String, tableName: String, accountId: String): List<Column> {
        require(!Identifier(tableName).isDelimited)

        return executeTableQuery(projectId, accountId, "DESC $tableName")
            .data.map { data ->
                Column(
                    data["Column"] as String,
                    data["Type"] as String
                )
            }
    }

    override fun countOfTable(projectId: String, tableName: String, accountId: String): Long {
        require(!Identifier(tableName).isDelimited)
        return executeTableQuery(projectId, accountId, "SELECT count(*) as total from $tableName")
            .data[0]["total"] as Long
    }

    private fun executeTableQuery(projectId: String, accountId: String, sql: String) =
        try {
            queryDataPort.executeQuery(projectId, accountId, sql)
        } catch (_: SQLException) {
            throw TableNotFoundException()
        }
}
