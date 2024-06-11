package com.samsung.healthcare.platform.application.port.input.project

import com.samsung.healthcare.platform.domain.project.InLabVisit
import java.time.LocalDateTime

data class UpdateInLabVisitCommand(
    val userId: String,
    val checkedInBy: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val notes: String?,
) {
    init {
        require(userId.length < InLabVisit.USER_ID_LENGTH)
        require(checkedInBy.length < InLabVisit.CHECKED_IN_BY_LENGTH)
    }
}
