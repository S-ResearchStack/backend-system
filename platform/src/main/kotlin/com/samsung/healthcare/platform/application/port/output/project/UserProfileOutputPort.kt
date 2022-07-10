package com.samsung.healthcare.platform.application.port.output.project

import com.samsung.healthcare.platform.domain.project.UserProfile

interface UserProfileOutputPort {
    suspend fun create(userProfile: UserProfile)
    suspend fun updateLastSyncedAt(userId: UserProfile.UserId)
}
