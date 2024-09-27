package researchstack.backend.adapter.incoming.rest.oidc

import com.linecorp.armeria.common.HttpResponse
import com.linecorp.armeria.common.QueryParams
import com.linecorp.armeria.server.annotation.Get
import com.linecorp.armeria.server.annotation.Post
import com.linecorp.armeria.server.annotation.RequestObject
import org.springframework.stereotype.Component
import researchstack.backend.application.port.outgoing.oidc.GoogleOidcPort
import researchstack.backend.util.validateGoogleAuthCode

@Component
class GoogleOidcRestController(
    private val googleOidcPort: GoogleOidcPort
) {
    @Get("/auth/google/token")
    suspend fun getAuthorizationCode(params: QueryParams): HttpResponse {
        return googleOidcPort.exchangeCodeForIdToken(validateGoogleAuthCode(params["code"]))
    }

    @Post("/auth/google/token/refresh")
    suspend fun refreshToken(@RequestObject command: RefreshIdTokenRequest): HttpResponse {
        return googleOidcPort.refreshIdToken(command.refreshToken)
    }

    data class RefreshIdTokenRequest(
        val refreshToken: String
    )
}
