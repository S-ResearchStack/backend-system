package researchstack.backend.adapter.incoming.decorator

import com.linecorp.armeria.common.HttpMethod
import com.linecorp.armeria.common.HttpRequest
import com.linecorp.armeria.server.ServiceRequestContext
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import researchstack.backend.JwtUtil
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.exception.UnauthorizedException
import kotlin.test.assertEquals

internal class JwtAuthorizerTest {
    private val jwtAuthorizer = JwtAuthorizer(JwtUtil.reactiveJwtDecoder)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `authorize throws UnauthorizedException when the token was omitted`() {
        val req: HttpRequest = HttpRequest.builder()
            .method(HttpMethod.GET)
            .path("/")
            .build()
        val ctx = ServiceRequestContext.of(req)

        val exception = assertThrows<UnauthorizedException> {
            jwtAuthorizer.authorize(ctx, req).toCompletableFuture().join()
        }
        assertEquals("no token passed", exception.message)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `authorize throws UnauthorizedException when it received invalid bearer string`() {
        val jwt = JwtUtil.generateJWT("test-id").serialize()
        val req: HttpRequest = HttpRequest.builder()
            .header("authorization", "Malformed $jwt")
            .method(HttpMethod.GET)
            .path("/")
            .build()
        val ctx = ServiceRequestContext.of(req)

        val exception = assertThrows<UnauthorizedException> {
            jwtAuthorizer.authorize(ctx, req).toCompletableFuture().join()
        }
        assertEquals("invalid bearer string", exception.message)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `authorize should work properly`() {
        val jwt = JwtUtil.generateJWT("test-id").serialize()
        val req: HttpRequest = HttpRequest.builder()
            .header("authorization", "Bearer $jwt")
            .method(HttpMethod.GET)
            .path("/")
            .build()
        val ctx = ServiceRequestContext.of(req)

        assertEquals(true, jwtAuthorizer.authorize(ctx, req).toCompletableFuture().join())
    }
}
