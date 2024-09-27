package researchstack.backend.adapter.exception

import com.linecorp.armeria.common.HttpMethod
import com.linecorp.armeria.common.HttpRequest
import com.linecorp.armeria.common.HttpStatus
import com.linecorp.armeria.server.ServiceRequestContext
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.exception.AlreadyExistsException
import researchstack.backend.application.exception.NotFoundException
import researchstack.backend.application.exception.UnauthorizedException
import java.nio.charset.StandardCharsets
import java.util.NoSuchElementException
import kotlin.test.assertEquals

internal class HttpExceptionHandlerTest {
    private val handler = HttpExceptionHandler(true)

    @Test
    @Tag(POSITIVE_TEST)
    fun `apply should work properly`() {
        val message = "test message"
        val req: HttpRequest = HttpRequest.builder()
            .method(HttpMethod.GET)
            .path("/")
            .build()
        val ctx = ServiceRequestContext.of(req)

        handler.onServiceException(ctx, AlreadyExistsException(message)).aggregate().join().let {
            assertEquals(HttpStatus.CONFLICT, it.status())
            assertEquals(message, it.content().toString(StandardCharsets.UTF_8))
        }
        handler.onServiceException(ctx, IllegalArgumentException(message)).aggregate().join().let {
            assertEquals(HttpStatus.BAD_REQUEST, it.status())
            assertEquals(message, it.content().toString(StandardCharsets.UTF_8))
        }
        handler.onServiceException(ctx, NotFoundException(message)).aggregate().join().let {
            assertEquals(HttpStatus.NOT_FOUND, it.status())
            assertEquals(message, it.content().toString(StandardCharsets.UTF_8))
        }
        handler.onServiceException(ctx, NoSuchElementException(message)).aggregate().join().let {
            assertEquals(HttpStatus.NOT_FOUND, it.status())
            assertEquals(message, it.content().toString(StandardCharsets.UTF_8))
        }
        handler.onServiceException(ctx, NotImplementedError(message)).aggregate().join().let {
            assertEquals(HttpStatus.NOT_IMPLEMENTED, it.status())
            assertEquals(message, it.content().toString(StandardCharsets.UTF_8))
        }
        handler.onServiceException(ctx, UnauthorizedException(message)).aggregate().join().let {
            assertEquals(HttpStatus.UNAUTHORIZED, it.status())
            assertEquals(message, it.content().toString(StandardCharsets.UTF_8))
        }
        handler.onServiceException(ctx, Exception(message)).aggregate().join().let {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, it.status())
            assertEquals(message, it.content().toString(StandardCharsets.UTF_8))
        }
    }
}
