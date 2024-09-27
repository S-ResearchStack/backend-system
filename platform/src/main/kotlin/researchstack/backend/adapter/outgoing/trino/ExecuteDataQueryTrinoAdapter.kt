package researchstack.backend.adapter.outgoing.trino

import io.trino.sql.SqlFormatter
import io.trino.sql.parser.SqlParser
import io.trino.sql.tree.QualifiedName
import io.trino.sql.tree.Query
import io.trino.sql.tree.QuerySpecification
import io.trino.sql.tree.ShowCatalogs
import io.trino.sql.tree.ShowColumns
import io.trino.sql.tree.ShowSchemas
import io.trino.sql.tree.ShowSession
import io.trino.sql.tree.Statement
import io.trino.sql.tree.Table
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.trino.repository.TrinoRepository
import researchstack.backend.application.exception.ForbiddenException
import researchstack.backend.application.port.outgoing.data.ExecuteDataQueryOutPort
import researchstack.backend.domain.data.QueryResult
import researchstack.backend.enums.DataFieldType
import java.util.*

@Component
class ExecuteDataQueryTrinoAdapter(
    private val trinoRepository: TrinoRepository
) : ExecuteDataQueryOutPort {
    private val sqlParser: SqlParser = SqlParser()

    override suspend fun validate(query: String): String {
        when (val statement = sqlParser.createStatement(query)) {
            is ShowCatalogs, is ShowColumns, is ShowSchemas, is ShowSession ->
                throw ForbiddenException("statement: $statement")
        }
        return query
    }

    override suspend fun execute(databaseName: String, query: String): QueryResult {
        val trinoQuery = replaceTableName(databaseName, query)
        val columns = trinoRepository.getMetaData(trinoQuery).map {
            it.key to DataFieldType.entries.first { type -> type.value == it.value }.name
        }.toMap()
        val data = trinoRepository.execute(trinoQuery)
        return QueryResult(columns, data)
    }

    private fun replaceTableName(databaseName: String, query: String): String {
        val statement: Statement = sqlParser.createStatement(query)
        if (statement is Query) {
            val querySpec = statement.queryBody as QuerySpecification
            val from = querySpec.from.orElse(null)
            if (from is Table) {
                val tableName = from.name as QualifiedName

                if (tableName.prefix.isEmpty) {
                    val newTableName = QualifiedName.of(databaseName, tableName.parts[0])

                    val newTable = Table(newTableName)
                    val newQuerySpec = QuerySpecification(
                        querySpec.select,
                        Optional.of(newTable),
                        querySpec.where,
                        querySpec.groupBy,
                        querySpec.having,
                        querySpec.windows,
                        querySpec.orderBy,
                        querySpec.offset,
                        querySpec.limit
                    )
                    return SqlFormatter.formatSql(
                        Query(
                            statement.functions,
                            statement.with,
                            newQuerySpec,
                            statement.orderBy,
                            statement.offset,
                            statement.limit
                        )
                    )
                }
            }
        }
        return SqlFormatter.formatSql(statement)
    }
}
