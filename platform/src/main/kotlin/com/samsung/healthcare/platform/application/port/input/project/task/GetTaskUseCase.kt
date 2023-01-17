package com.samsung.healthcare.platform.application.port.input.project.task

import kotlinx.coroutines.flow.Flow

interface GetTaskUseCase {
    suspend fun findByPeriodFromResearcher(projectId: String, command: GetTaskCommand): Flow<Map<String, Any?>>
    suspend fun findByPeriodFromParticipant(command: GetTaskCommand): Flow<Map<String, Any?>>
    suspend fun findById(projectId: String, taskId: String): Flow<Map<String, Any?>>
}
