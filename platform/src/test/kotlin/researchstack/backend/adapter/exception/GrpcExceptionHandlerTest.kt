package researchstack.backend.adapter.exception

import com.google.protobuf.InvalidProtocolBufferException
import com.linecorp.armeria.common.HttpMethod
import com.linecorp.armeria.common.HttpRequest
import com.linecorp.armeria.server.ServiceRequestContext
import io.grpc.Status
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.exception.AlreadyExistsException
import researchstack.backend.application.exception.NotFoundException
import researchstack.backend.application.exception.UnauthorizedException
import java.util.NoSuchElementException
import kotlin.test.assertEquals

internal class GrpcExceptionHandlerTest {
    private val handler = GrpcExceptionHandler()

    @Test
    @Tag(POSITIVE_TEST)
    fun `apply should work properly`() {
        val message = "test message"
        val req: HttpRequest = HttpRequest.builder()
            .method(HttpMethod.GET)
            .path("/")
            .build()
        val ctx = ServiceRequestContext.of(req)

        handler.apply(ctx, AlreadyExistsException(message), io.grpc.Metadata()).let {
            assertEquals(Status.ALREADY_EXISTS, it.code.toStatus())
            assertEquals(message, it.description)
        }
        handler.apply(ctx, IllegalArgumentException(message), io.grpc.Metadata()).let {
            assertEquals(Status.INVALID_ARGUMENT, it.code.toStatus())
            assertEquals(message, it.description)
        }
        handler.apply(ctx, NotFoundException(message), io.grpc.Metadata()).let {
            assertEquals(Status.NOT_FOUND, it.code.toStatus())
            assertEquals(message, it.description)
        }
        handler.apply(ctx, NoSuchElementException(message), io.grpc.Metadata()).let {
            assertEquals(Status.NOT_FOUND, it.code.toStatus())
            assertEquals(message, it.description)
        }
        handler.apply(ctx, NotImplementedError(message), io.grpc.Metadata()).let {
            assertEquals(Status.UNIMPLEMENTED, it.code.toStatus())
            assertEquals(message, it.description)
        }
        handler.apply(ctx, UnauthorizedException(message), io.grpc.Metadata()).let {
            assertEquals(Status.UNAUTHENTICATED, it.code.toStatus())
            assertEquals(message, it.description)
        }
        handler.apply(ctx, InvalidProtocolBufferException(message), io.grpc.Metadata()).let {
            assertEquals(Status.INVALID_ARGUMENT, it.code.toStatus())
            assertEquals(message, it.description)
        }
        handler.apply(ctx, Exception(message), io.grpc.Metadata()).let {
            assertEquals(Status.INTERNAL, it.code.toStatus())
            assertEquals(message, it.description)
        }
    }
}
