package com.samsung.healthcare.platform.application.port.input.project.task

import com.samsung.healthcare.platform.domain.project.task.RevisionId

interface UpdateTaskUseCase {
    suspend fun updateTask(
        id: String,
        revisionId: RevisionId,
        command: UpdateTaskCommand,
    )
}
