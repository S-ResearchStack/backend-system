package com.samsung.healthcare.platform.domain

import java.time.LocalDateTime

data class User(
    val id: UserId,
    val sub: String,
    val provider: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun newUser(id: UserId, sub: String, provider: String): User =
            User(id, sub, provider, LocalDateTime.now())

        fun getHash(email: String): String {
            // TODO: Implement user email hashing logic
            return email
        }
    }

    override fun toString(): String {
        return "User[$id]"
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is User) return false

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    data class UserId private constructor(val value: String) {
        companion object {
            fun from(value: String?): UserId {
                requireNotNull(value)
                return UserId(value)
            }
        }
    }
}
