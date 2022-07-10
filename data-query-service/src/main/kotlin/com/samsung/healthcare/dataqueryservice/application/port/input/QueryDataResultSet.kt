package com.samsung.healthcare.dataqueryservice.application.port.input

data class QueryDataResultSet(
    val metadata: MetaData,
    val data: List<Map<String, Any?>>,
) {
    data class MetaData(
        val columns: List<String>,
        val count: Int,
    )
}
