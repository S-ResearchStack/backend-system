package com.samsung.healthcare.platform.domain.project

import java.time.LocalDateTime

/**
 * Represents profiles of registered users.
 *
 * @property userId [UserId] associated with a user.
 * @property profile Demographic information provided by the user. Entries may vary per user as different projects could request different personal information fields.
 * @property lastSyncedAt Indicates the most recent time data associated with this user, such as [HealthData][com.samsung.healthcare.platform.domain.project.healthdata.HealthData] or [TaskResult][com.samsung.healthcare.platform.domain.project.task.TaskResult].
 */
class UserProfile(
    val userId: UserId,
    val profile: Map<String, Any> = emptyMap(),
    val lastSyncedAt: LocalDateTime,
) {
    companion object {
        const val USER_PROFILE_LENGTH = 320
        fun newUserProfile(userId: String, profile: Map<String, Any>): UserProfile =
            UserProfile(UserId.from(userId), profile, LocalDateTime.now())
    }

    /**
     * Represents the UserId associated with a user.
     *
     * The UserId is generated from the String username provided by a user at registration.
     *
     * @property value The username input by a registering user.
     */
    data class UserId private constructor(val value: String) {
        companion object {
            fun from(value: String?): UserId {
                requireNotNull(value)
                return UserId(value)
            }
        }
    }
}
