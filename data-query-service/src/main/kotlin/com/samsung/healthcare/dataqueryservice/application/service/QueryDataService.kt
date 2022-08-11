package com.samsung.healthcare.dataqueryservice.application.service

import com.samsung.healthcare.dataqueryservice.application.port.input.QueryDataUseCase
import com.samsung.healthcare.dataqueryservice.application.port.input.QueryDataCommand
import com.samsung.healthcare.dataqueryservice.application.port.input.QueryDataResultSet
import com.samsung.healthcare.dataqueryservice.application.port.output.QueryDataPort
import org.springframework.stereotype.Service

@Service
class QueryDataService(
    private val queryDataPort: QueryDataPort,
) : QueryDataUseCase {
    override fun execute(
        projectId: String,
        queryCommand: QueryDataCommand,
    ): QueryDataResultSet {
        val result = queryDataPort.executeQuery(projectId, queryCommand.sql)

        return QueryDataResultSet(
            metadata = QueryDataResultSet.MetaData(
                result.columns,
                result.data.size
            ),
            data = result.data.toList(),
        )
    }
}
