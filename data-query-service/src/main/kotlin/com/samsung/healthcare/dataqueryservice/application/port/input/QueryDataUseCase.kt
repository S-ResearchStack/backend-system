package com.samsung.healthcare.dataqueryservice.application.port.input

interface QueryDataUseCase {
    fun execute(
        projectId: String,
        accountId: String?,
        queryCommand: QueryDataCommand,
    ): QueryDataResultSet
}
