package com.samsung.healthcare.dataqueryservice.application.port.output

interface QueryDataPort {
    fun executeQuery(projectId: String, accountId: String?, sql: String): QueryDataResult
}
