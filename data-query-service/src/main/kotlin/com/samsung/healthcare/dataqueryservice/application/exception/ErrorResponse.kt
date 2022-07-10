package com.samsung.healthcare.dataqueryservice.application.exception

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ErrorResponse(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    // TODO: Decide code types
    val code: String = "TODO",
    val message: String,
    val reason: String?,
)
