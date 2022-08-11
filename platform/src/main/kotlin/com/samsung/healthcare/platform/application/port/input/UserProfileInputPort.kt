package com.samsung.healthcare.platform.application.port.input

import com.samsung.healthcare.platform.domain.project.UserProfile

interface UserProfileInputPort {
    suspend fun registerUser(userProfile: UserProfile)
}
