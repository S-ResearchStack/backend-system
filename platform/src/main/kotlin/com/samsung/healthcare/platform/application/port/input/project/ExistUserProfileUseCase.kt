package com.samsung.healthcare.platform.application.port.input.project

import com.samsung.healthcare.platform.domain.project.UserProfile

interface ExistUserProfileUseCase {
    suspend fun existsByUserId(userId: UserProfile.UserId): Boolean
}
