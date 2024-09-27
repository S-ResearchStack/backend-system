package researchstack.backend.domain.auth

import java.time.Instant

data class Token(
    val accountId: String,
    val accessToken: String,
    val refreshToken: String,
    val expiredAt: Instant
) {

    companion object {
        private val CHAR_POOL = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        fun newToken(accountId: String, accessToken: String, refreshToken: String, expiredAt: Instant): Token =
            Token(accountId, accessToken, refreshToken, expiredAt)

        fun generateToken(
            accountId: String,
            accessToken: String,
            refreshTokenLifetime: Long,
            refreshTokenLength: Int
        ): Token =
            Token(
                accountId,
                accessToken,
                randomTokenString(refreshTokenLength),
                Instant.now().plusSeconds(refreshTokenLifetime)
            )

        private fun randomTokenString(length: Int): String =
            StringBuilder(length).apply {
                repeat(this.capacity()) {
                    append(CHAR_POOL.random())
                }
            }.toString()
    }
}
