package com.samsung.healthcare.account.domain

import java.time.Instant

data class Token(
    val accountId: String,
    val accessToken: String,
    val refreshToken: String,
    val expiredAt: Instant
) {

    companion object {
        private val CHAR_POOL = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        // TODO config
        private const val REFRESH_TOKEN_LIFE_TIME = 14 * 24 * 60 * 60L

        private const val TOKEN_LENGTH = 500

        fun newToken(accountId: String, accessToken: String, refreshToken: String, expiredAt: Instant): Token =
            Token(accountId, accessToken, refreshToken, expiredAt)

        fun generateToken(accountId: String, accessToken: String): Token =
            // TODO config refresh-token lifetime
            Token(accountId, accessToken, randomTokenString(), Instant.now().plusSeconds(REFRESH_TOKEN_LIFE_TIME))

        private fun randomTokenString(): String =
            // TODO config refresh-token length
            StringBuilder(TOKEN_LENGTH).apply {
                repeat(this.capacity()) {
                    append(CHAR_POOL.random())
                }
            }.toString()
    }
}
