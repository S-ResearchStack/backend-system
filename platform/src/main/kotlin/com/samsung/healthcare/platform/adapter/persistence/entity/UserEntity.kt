package com.samsung.healthcare.platform.adapter.persistence.entity

import com.samsung.healthcare.platform.adapter.persistence.entity.common.BaseIdEntity
import com.samsung.healthcare.platform.domain.User
import com.samsung.healthcare.platform.domain.User.UserId
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("users")
data class UserEntity(
    val id: String,
    val sub: String,
    val provider: String,
    val createdAt: LocalDateTime,
    val deletedAt: LocalDateTime? = null,
) : BaseIdEntity<String?>(id) {
    companion object {
        fun fromDomain(user: User): UserEntity =
            UserEntity(
                id = user.id.value,
                sub = user.sub,
                provider = user.provider,
                createdAt = user.createdAt
            )
    }

    fun toDomain(): User =
        User(UserId.from(this.id), this.sub, this.provider, this.createdAt)
}
