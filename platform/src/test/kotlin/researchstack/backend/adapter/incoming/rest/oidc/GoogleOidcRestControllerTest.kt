package researchstack.backend.adapter.incoming.rest.oidc

import com.linecorp.armeria.common.HttpResponse
import com.linecorp.armeria.common.HttpStatus
import com.linecorp.armeria.common.QueryParams
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.reactive.server.WebTestClient
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.port.outgoing.oidc.GoogleOidcPort
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class GoogleOidcRestControllerTest {
    private val googleOidcPort = mockk<GoogleOidcPort>()
    private val googleOidcHttpController = GoogleOidcRestController(googleOidcPort)

    @Autowired
    private lateinit var webClient: WebTestClient

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getAuthorizationCode throws IllegalArgumentException when code was null or empty`() = runTest {
        coEvery {
            googleOidcPort.exchangeCodeForIdToken("test-code")
        } returns HttpResponse.builder().status(HttpStatus.OK).build()

        assertThrows<IllegalArgumentException> {
            googleOidcHttpController.getAuthorizationCode(
                QueryParams.builder().build()
            )
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getAuthorizationCode returns 400 bad request when it received 400 from exchangeCodeForIdToken of googleOidcPort`() =
        runTest {
            coEvery {
                googleOidcPort.exchangeCodeForIdToken("test-code")
            } returns HttpResponse.builder().status(HttpStatus.BAD_REQUEST).build()

            val ret = googleOidcHttpController.getAuthorizationCode(
                QueryParams.builder().add("code", "test-code").build()
            )

            assertEquals(HttpStatus.BAD_REQUEST, ret.aggregate().join().status())
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getAuthorizationCode should work properly`() = runTest {
        coEvery {
            googleOidcPort.exchangeCodeForIdToken("test-code")
        } returns HttpResponse.builder().status(HttpStatus.OK).build()

        val ret = googleOidcHttpController.getAuthorizationCode(
            QueryParams.builder().add("code", "test-code").build()
        )

        assertEquals(HttpStatus.OK, ret.aggregate().join().status())
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `refreshToken returns 400 bad request when it received 400 from refreshIdToken of googleOidcPort`() = runTest {
        coEvery {
            googleOidcPort.refreshIdToken("token")
        } returns HttpResponse.builder().status(HttpStatus.BAD_REQUEST).build()

        val ret = googleOidcHttpController.refreshToken(
            GoogleOidcRestController.RefreshIdTokenRequest("token")
        )

        assertEquals(HttpStatus.BAD_REQUEST, ret.aggregate().join().status())
    }
}
