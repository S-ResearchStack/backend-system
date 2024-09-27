package researchstack.backend.adapter.incoming.decorator

import com.linecorp.armeria.common.HttpMethod
import com.linecorp.armeria.common.HttpRequest
import com.linecorp.armeria.server.ServiceRequestContext
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.backend.JwtUtil
import researchstack.backend.POSITIVE_TEST
import kotlin.test.assertEquals

internal class SamsungAccountJwtAuthorizerTest {
    private val jwtAuthorizer = SamsungAccountJwtAuthorizer(JwtUtil.reactiveJwtDecoder)

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
