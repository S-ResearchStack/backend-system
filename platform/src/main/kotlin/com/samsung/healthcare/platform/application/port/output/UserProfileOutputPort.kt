package com.samsung.healthcare.platform.application.port.output

import com.samsung.healthcare.platform.domain.project.UserProfile

interface UserProfileOutputPort {
    suspend fun create(userProfile: UserProfile)
}
