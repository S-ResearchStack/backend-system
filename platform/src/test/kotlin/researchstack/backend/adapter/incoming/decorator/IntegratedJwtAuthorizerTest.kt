package researchstack.backend.adapter.incoming.decorator

import com.linecorp.armeria.common.HttpMethod
import com.linecorp.armeria.common.HttpRequest
import com.linecorp.armeria.server.ServiceRequestContext
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import researchstack.backend.JwtUtil
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.exception.UnauthorizedException
import researchstack.backend.config.OidcGoogleProperties
import kotlin.test.assertEquals

internal class IntegratedJwtAuthorizerTest {
    private val oidcGoogleProperties = OidcGoogleProperties("", JwtUtil.clientId, "", "")
    private val googleJwtAuthorizer = GoogleJwtAuthorizer(JwtUtil.reactiveJwtDecoder, oidcGoogleProperties)
    private val superTokensJwtAuthorizer = SuperTokensJwtAuthorizer(JwtUtil.reactiveJwtDecoder)
    private val cognitoJwtAuthorizer = CognitoJwtAuthorizer(JwtUtil.reactiveJwtDecoder)
    private val samsungAccountJwtAuthorizer = SamsungAccountJwtAuthorizer(JwtUtil.reactiveJwtDecoder)
    private val integratedJwtAuthorizer = IntegratedJwtAuthorizer(
        cognitoJwtAuthorizer,
        googleJwtAuthorizer,
        samsungAccountJwtAuthorizer,
        superTokensJwtAuthorizer
    )

    @Test
    @Tag(NEGATIVE_TEST)
    fun `authorize throws UnauthorizedException when the jwt-issuer header was not set`() {
        val req: HttpRequest = HttpRequest.builder()
            .method(HttpMethod.GET)
            .path("/")
            .build()
        val ctx = ServiceRequestContext.of(req)

        val exception = assertThrows<UnauthorizedException> {
            integratedJwtAuthorizer.authorize(ctx, req).toCompletableFuture().join()
        }
        assertEquals("please set jwt-issuer header", exception.message)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `authorize throws UnauthorizedException when it received unsupported jwt-issuer type`() {
        val jwt = JwtUtil.generateJWT("test-id", JwtUtil.clientId).serialize()
        val req: HttpRequest = HttpRequest.builder()
            .header("authorization", "Bearer $jwt")
            .header("jwt-issuer", "unsupported")
            .method(HttpMethod.GET)
            .path("/")
            .build()
        val ctx = ServiceRequestContext.of(req)

        val exception = assertThrows<UnauthorizedException> {
            integratedJwtAuthorizer.authorize(ctx, req).toCompletableFuture().join()
        }
        assertEquals("unsupported jwt-issuer type", exception.message)
    }

    @ParameterizedTest
    @Tag(POSITIVE_TEST)
    @ValueSource(strings = ["cognito", "google", "samsung-account", "super-tokens"])
    fun `authorize should work properly`(jwkType: String) {
        val jwt = JwtUtil.generateJWT("test-id", JwtUtil.clientId).serialize()
        val req: HttpRequest = HttpRequest.builder()
            .header("authorization", "Bearer $jwt")
            .header("jwt-issuer", jwkType)
            .method(HttpMethod.GET)
            .path("/")
            .build()
        val ctx = ServiceRequestContext.of(req)

        assertEquals(true, integratedJwtAuthorizer.authorize(ctx, req).toCompletableFuture().join())
    }
}
