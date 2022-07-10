package com.samsung.healthcare.dataqueryservice.application.port.output

data class QueryDataResult(
    val columns: List<String>,
    val data: List<Map<String, Any?>>,
)
