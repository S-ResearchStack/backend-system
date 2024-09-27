package researchstack.backend.adapter.incoming.decorator

import com.linecorp.armeria.common.HttpMethod
import com.linecorp.armeria.common.HttpRequest
import com.linecorp.armeria.server.ServiceRequestContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import researchstack.backend.JwtUtil
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.config.OidcGoogleProperties
import java.util.concurrent.CompletionException

internal class GoogleJwtAuthorizerTest {
    private val oidcGoogleProperties = OidcGoogleProperties("", JwtUtil.clientId, "", "")

    private val googleJwtAuthorizer = GoogleJwtAuthorizer(JwtUtil.reactiveJwtDecoder, oidcGoogleProperties)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `validate should throw CompletionException when the audience field was null`() {
        val jwt = JwtUtil.generateJWT("test-id").serialize()
        val req: HttpRequest = HttpRequest.builder()
            .header("authorization", "Bearer $jwt")
            .method(HttpMethod.GET)
            .path("/")
            .build()
        val ctx = ServiceRequestContext.of(req)

        val exception = assertThrows<CompletionException> {
            googleJwtAuthorizer.authorize(ctx, req).toCompletableFuture().join()
        }
        assertEquals("token has invalid client id", exception.cause?.message)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `validate should throw CompletionException when clientId was not matched`() {
        val jwt = JwtUtil.generateJWT("test-id", "wrong-client-id").serialize()
        val req: HttpRequest = HttpRequest.builder()
            .header("authorization", "Bearer $jwt")
            .method(HttpMethod.GET)
            .path("/")
            .build()
        val ctx = ServiceRequestContext.of(req)

        val exception = assertThrows<CompletionException> {
            googleJwtAuthorizer.authorize(ctx, req).toCompletableFuture().join()
        }
        assertEquals("token has invalid client id", exception.cause?.message)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `validate should work properly`() {
        val jwt = JwtUtil.generateJWT("test-id", JwtUtil.clientId).serialize()
        val req: HttpRequest = HttpRequest.builder()
            .header("authorization", "Bearer $jwt")
            .method(HttpMethod.GET)
            .path("/")
            .build()
        val ctx = ServiceRequestContext.of(req)

        assertEquals(true, googleJwtAuthorizer.authorize(ctx, req).toCompletableFuture().join())
    }
}
