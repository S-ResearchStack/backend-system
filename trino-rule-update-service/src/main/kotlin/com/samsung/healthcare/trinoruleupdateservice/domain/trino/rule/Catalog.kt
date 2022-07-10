package com.samsung.healthcare.trinoruleupdateservice.domain.trino.rule

data class Catalog(
    val user: String? = null,
    val role: String? = null,
    val group: String? = null,
    val catalog: String? = null,
    val allow: String? = null,
)
