package com.samsung.healthcare.account.adapter.persistence.entity

import com.samsung.healthcare.account.domain.Token
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("tokens")
data class TokenEntity(
    @Id
    val id: Int?,
    val accountId: String,
    val accessToken: String,
    val refreshToken: String,
    val expiredAt: Long,
) {
    fun toDomain(): Token = Token.newToken(accountId, accessToken, refreshToken, Instant.ofEpochSecond(expiredAt))
}

fun Token.toEntity(): TokenEntity =
    TokenEntity(null, accountId, accessToken, refreshToken, expiredAt.epochSecond)
