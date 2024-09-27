package researchstack.backend.application.service.auth

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.port.incoming.auth.SignUpCommand
import researchstack.backend.application.port.outgoing.auth.AuthServicePort
import researchstack.backend.domain.auth.Account
import researchstack.backend.domain.common.Email
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class SignUpServiceTest {
    private val authServicePort = mockk<AuthServicePort>()

    private val signUpService = SignUpService(authServicePort)

    private val email = Email("test@reserach-hub.test.com")
    private val password = "pw"

    @Test
    @Tag(POSITIVE_TEST)
    fun `signUp should work properly`() = runTest {
        val account = Account("id", email)
        every { authServicePort.registerNewUser(email, password) } returns Mono.just(account)

        val response = signUpService.signUp(SignUpCommand(email.value, password))
        assertEquals(account, Account(response.id, response.email))
    }
}
