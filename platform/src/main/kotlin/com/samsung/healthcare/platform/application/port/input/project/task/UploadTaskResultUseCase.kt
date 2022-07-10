package com.samsung.healthcare.platform.application.port.input.project.task

interface UploadTaskResultUseCase {
    suspend fun uploadResults(
        commands: List<UploadTaskResultCommand>,
    )
}
