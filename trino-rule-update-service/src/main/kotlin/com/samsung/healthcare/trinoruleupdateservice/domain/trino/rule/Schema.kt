package com.samsung.healthcare.trinoruleupdateservice.domain.trino.rule

data class Schema(
    val user: String? = null,
    val role: String? = null,
    val group: String? = null,
    val catalog: String? = null,
    val schema: String? = null,
    val owner: Boolean = false,
)
