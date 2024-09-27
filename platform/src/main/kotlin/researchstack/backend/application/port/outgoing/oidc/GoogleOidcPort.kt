package researchstack.backend.application.port.outgoing.oidc

import com.linecorp.armeria.common.HttpResponse

interface GoogleOidcPort {
    suspend fun exchangeCodeForIdToken(code: String): HttpResponse

    suspend fun refreshIdToken(refreshToken: String): HttpResponse
}
