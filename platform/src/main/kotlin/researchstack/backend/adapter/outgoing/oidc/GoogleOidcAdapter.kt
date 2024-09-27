package researchstack.backend.adapter.outgoing.oidc

import com.linecorp.armeria.client.WebClient
import com.linecorp.armeria.common.HttpResponse
import com.linecorp.armeria.common.QueryParams
import org.springframework.stereotype.Component
import researchstack.backend.application.port.outgoing.oidc.GoogleOidcPort
import researchstack.backend.config.OidcGoogleProperties

@Component
class GoogleOidcAdapter(
    private val oidcGoogleProperties: OidcGoogleProperties
) : GoogleOidcPort {
    val webClient = WebClient.of(oidcGoogleProperties.oauth2Url)

    override suspend fun exchangeCodeForIdToken(code: String): HttpResponse {
        val queryParams = QueryParams.builder()
            .add("client_id", oidcGoogleProperties.clientId)
            .add("client_secret", oidcGoogleProperties.clientSecret)
            .add("code", code)
            .add("grant_type", "authorization_code")
            .add("redirect_uri", "postmessage")
            .build()

        return webClient.post(PathConstant.GOOGLE_OIDC_EXCHANGE_CODE_FOR_ID_TOKEN_PATH, queryParams, "")
    }

    override suspend fun refreshIdToken(refreshToken: String): HttpResponse {
        val queryParams = QueryParams.builder()
            .add("client_id", oidcGoogleProperties.clientId)
            .add("client_secret", oidcGoogleProperties.clientSecret)
            .add("grant_type", "refresh_token")
            .add("refresh_token", refreshToken)
            .build()

        return webClient.post(PathConstant.GOOGLE_OIDC_REFRESH_ID_TOKEN_PATH, queryParams, "")
    }
}
