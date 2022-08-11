package com.samsung.healthcare.platform.application.port.input

import com.samsung.healthcare.platform.domain.User.UserId

interface SaveHealthDataUseCase {
    suspend fun saveHealthData(userId: UserId, command: SaveHealthDataCommand)
}
