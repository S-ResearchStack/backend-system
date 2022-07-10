package com.samsung.healthcare.platform.application.port.input.project.task

import java.time.LocalDateTime

data class UploadTaskResultCommand(
    val revisionId: Int,
    val taskId: String,
    val userId: String,
    val startedAt: LocalDateTime,
    val submittedAt: LocalDateTime,
    val itemResults: List<UpdateItemResultCommand>
) {
    data class UpdateItemResultCommand(
        val itemName: String,
        val result: String,
    )
}
