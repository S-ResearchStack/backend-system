package com.samsung.healthcare.platform.application.port.input.project.task

data class CreateTaskResponse(
    val revisionId: Int?,
    val id: String,
) {
    init {
        requireNotNull(revisionId)
    }
}
