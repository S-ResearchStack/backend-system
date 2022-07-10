package com.samsung.healthcare.platform.application.port.input.project.task

import kotlinx.coroutines.flow.Flow

interface GetTaskUseCase {
    suspend fun findByPeriod(command: GetTaskCommand): Flow<Map<String, Any?>>
    suspend fun findById(id: String): Flow<Map<String, Any?>>
}
