package researchstack.backend.application.service.auth

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.port.incoming.auth.SignInCommand
import researchstack.backend.application.port.outgoing.auth.AuthServicePort
import researchstack.backend.domain.auth.Account
import researchstack.backend.domain.auth.Token
import researchstack.backend.domain.common.Email
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
internal class SignInServiceTest {
    private val authServicePort = mockk<AuthServicePort>()

    private val tokenService = mockk<TokenService>()

    private val signInService = SignInService(authServicePort, tokenService)

    private val email = Email("test@reserach-hub.test.com")
    private val password = "pw"

    @Test
    @Tag(POSITIVE_TEST)
    fun `signIn should work properly`() = runTest {
        val account = Account("id", email)
        val encodedJwt = "encoded-jwt-string"
        every { authServicePort.signIn(email, password) } returns Mono.just(account)
        every { tokenService.generateToken(account) } returns Mono.just(
            Token.generateToken(
                account.id,
                encodedJwt,
                1,
                500
            )
        )

        val response = signInService.signIn(SignInCommand(email.value, password))
        assertEquals(account, Account(response.id, response.email))
        assertEquals(encodedJwt, response.accessToken)
        assertTrue(response.refreshToken.isNotBlank())
    }
}
