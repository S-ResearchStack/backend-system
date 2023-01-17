package com.samsung.healthcare.dataqueryservice.adapter.web.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import com.netflix.graphql.dgs.context.DgsContext
import com.samsung.healthcare.dataqueryservice.adapter.web.graphql.DgsContextExtension.getAccount
import com.samsung.healthcare.dataqueryservice.adapter.web.graphql.DgsContextExtension.getProjectId
import com.samsung.healthcare.dataqueryservice.application.port.input.Column
import com.samsung.healthcare.dataqueryservice.application.port.input.Table
import com.samsung.healthcare.dataqueryservice.application.port.input.TableDataQuery
import graphql.schema.DataFetchingEnvironment

@DgsComponent
class TableDataFetcher(
    private val tableQuery: TableDataQuery
) {

    @DgsQuery
    fun tables(dfe: DataFetchingEnvironment, @InputArgument nameFilter: String?): List<Table> {
        val context = DgsContext.from(dfe)
        return if (nameFilter != null) {
            tableQuery.listAllTables(context.getProjectId(), context.getAccount())
                .filter { it.name.contains(nameFilter) }
        } else {
            tableQuery.listAllTables(context.getProjectId(), context.getAccount())
        }
    }

    @DgsData(parentType = "Table", field = "columns")
    fun columns(dfe: DgsDataFetchingEnvironment): List<Column> {
        val context = DgsContext.from(dfe)
        val table = dfe.getSource<Table>()
        return tableQuery.columnsOfTable(
            context.getProjectId(),
            table.name,
            context.getAccount()
        )
    }

    @DgsQuery
    fun count(dfe: DataFetchingEnvironment, @InputArgument tableName: String): Int {
        require(!tableName.isNullOrBlank())

        val context = DgsContext.from(dfe)
        return tableQuery.countOfTable(context.getProjectId(), tableName, context.getAccount())
            // TODO use https://github.com/graphql-java/graphql-java-extended-scalars for LONG
            .toInt()
    }
}
