package com.samsung.healthcare.platform.application.port.input.project

import com.samsung.healthcare.platform.domain.project.InLabVisit.Companion.CHECKED_IN_BY_LENGTH
import com.samsung.healthcare.platform.domain.project.InLabVisit.Companion.USER_ID_LENGTH
import java.time.LocalDateTime

data class CreateInLabVisitCommand(
    val userId: String,
    val checkedInBy: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val notes: String?,
) {
    init {
        require(userId.length < USER_ID_LENGTH)
        require(checkedInBy.length < CHECKED_IN_BY_LENGTH)
    }
}
