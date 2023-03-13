package com.samsung.healthcare.dataqueryservice.application.port.input

class QueryDataCommand(
    // FIXME How about using statement
    val sql: String,
) {
    init {
        require(sql.isNotBlank())
    }
}
