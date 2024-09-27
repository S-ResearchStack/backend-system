package researchstack.backend.application.service.auth

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.port.outgoing.auth.TokenSigningPort
import researchstack.backend.config.TokenProperties
import researchstack.backend.domain.auth.Account
import researchstack.backend.domain.auth.Token
import researchstack.backend.domain.common.Email
import java.time.Instant
import java.util.*

internal class TokenServiceTest {
    private val properties = TokenProperties(
        TokenProperties.AccessToken(1, "research-hub.com"),
        TokenProperties.RefreshToken(1, 100)
    )

    private val tokenSigningPort = mockk<TokenSigningPort>()

    private val tokenService = TokenService(properties, tokenSigningPort)

    private val accountId = UUID.randomUUID().toString()
    private val token = Token(
        accountId,
        "access-token",
        "refresh-token",
        Instant.now().plusSeconds(1000L)
    )
    private val email = Email("test@reserach-hub.test.com")
    private val account = Account(accountId, email)

    @Test
    @Tag(POSITIVE_TEST)
    fun `generateToken should work properly`() {
        every { tokenSigningPort.generateSignedJwt(any()) } returns Mono.just(token.accessToken)

        StepVerifier.create(
            tokenService.generateToken(account)
        ).expectNextMatches { token ->
            token.accountId == accountId &&
                token.accessToken == token.accessToken &&
                token.refreshToken == token.refreshToken &&
                token.expiredAt == token.expiredAt
        }.verifyComplete()
    }
}
