package com.samsung.healthcare.dataqueryservice.application.service

import com.samsung.healthcare.dataqueryservice.application.exception.ForbiddenSqlStatementTypeException
import com.samsung.healthcare.dataqueryservice.application.port.input.QueryDataCommand
import com.samsung.healthcare.dataqueryservice.application.port.input.QueryDataResultSet
import com.samsung.healthcare.dataqueryservice.application.port.input.QueryDataUseCase
import com.samsung.healthcare.dataqueryservice.application.port.output.QueryDataPort
import io.trino.sql.parser.ParsingOptions
import io.trino.sql.parser.SqlParser
import io.trino.sql.tree.ShowCatalogs
import io.trino.sql.tree.ShowColumns
import io.trino.sql.tree.ShowSchemas
import io.trino.sql.tree.ShowSession
import io.trino.sql.tree.Statement
import org.springframework.stereotype.Service

@Service
class QueryDataService(
    private val queryDataPort: QueryDataPort
) : QueryDataUseCase {
    private val parser: SqlParser = SqlParser()
    override fun execute(
        projectId: String,
        accountId: String?,
        queryCommand: QueryDataCommand,
    ): QueryDataResultSet {
        checkStatementType(parser.createStatement(queryCommand.sql, ParsingOptions()))

        val result = queryDataPort.executeQuery(projectId, accountId, queryCommand.sql)
        return QueryDataResultSet(
            metadata = QueryDataResultSet.MetaData(
                result.columns,
                result.data.size
            ),
            data = result.data.toList(),
        )
    }

    private fun checkStatementType(statement: Statement) {
        when (statement) {
            is ShowCatalogs, is ShowColumns, is ShowSchemas, is ShowSession ->
                throw ForbiddenSqlStatementTypeException("statement object: $statement")
        }
    }
}
