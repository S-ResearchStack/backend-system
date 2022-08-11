package com.samsung.healthcare.platform.domain.project

class UserProfile(
    val userId: String,
    val profile: Map<String, Any> = emptyMap(),
)
