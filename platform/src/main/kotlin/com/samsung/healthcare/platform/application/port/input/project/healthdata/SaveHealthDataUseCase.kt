package com.samsung.healthcare.platform.application.port.input.project.healthdata

import com.samsung.healthcare.platform.domain.project.UserProfile.UserId

/**
 * Saves HealthData.
 *
 * @see [com.samsung.healthcare.platform.adapter.web.project.healthdata.HealthDataHandler]
 */
interface SaveHealthDataUseCase {
    suspend fun saveHealthData(userId: UserId, command: SaveHealthDataCommand)
}
