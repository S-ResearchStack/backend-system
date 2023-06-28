package com.samsung.healthcare.platform.application.port.input.project

import java.time.LocalDateTime

data class UpdateInLabVisitCommand(
    val userId: String,
    val checkedInBy: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val notes: String?,
)
