package com.samsung.healthcare.platform.domain.project

import java.time.LocalDateTime

data class InLabVisit(
    val id: Int?,
    val userId: String,
    val checkedInBy: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val notes: String?,
    val filesPath: String?,
) {
    companion object {
        const val USER_ID_LENGTH = 320
        const val CHECKED_IN_BY_LENGTH = 320
    }
}
