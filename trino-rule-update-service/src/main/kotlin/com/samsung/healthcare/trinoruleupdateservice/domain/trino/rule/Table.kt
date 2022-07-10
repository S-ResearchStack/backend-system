package com.samsung.healthcare.trinoruleupdateservice.domain.trino.rule

data class Table(
    val user: String? = null,
    val role: String? = null,
    val group: String? = null,
    val catalog: String? = null,
    val schema: String? = null,
    val table: String? = null,
    val privileges: List<String>? = null,
    // TODO: add 'columns' field
    // TODO: add 'filter' field
    // TODO: add 'filterEnvironment' field
)
