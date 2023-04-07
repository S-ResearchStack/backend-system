package com.samsung.healthcare.platform.application.port.input.project.task

import java.time.LocalDateTime

data class GetTaskCommand(
    val startTime: LocalDateTime?,
    val endTime: LocalDateTime?,
    val lastSyncTime: LocalDateTime?,
    val status: String?,
    val type: String?
) {
    init {
        if (startTime != null && endTime != null) {
            require(startTime.isBefore(endTime)) { "invalid period" }
        }
    }
}
