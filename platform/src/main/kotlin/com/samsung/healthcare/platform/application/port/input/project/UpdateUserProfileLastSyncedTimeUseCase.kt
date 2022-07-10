package com.samsung.healthcare.platform.application.port.input.project

import com.samsung.healthcare.platform.domain.project.UserProfile

interface UpdateUserProfileLastSyncedTimeUseCase {
    suspend fun updateLastSyncedTime(userId: UserProfile.UserId)
}
