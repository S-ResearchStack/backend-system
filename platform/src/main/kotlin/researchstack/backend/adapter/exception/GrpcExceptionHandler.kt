package researchstack.backend.adapter.exception

import com.google.protobuf.InvalidProtocolBufferException
import com.linecorp.armeria.common.RequestContext
import com.linecorp.armeria.common.grpc.GrpcStatusFunction
import io.grpc.Metadata
import io.grpc.Status
import researchstack.backend.application.exception.AlreadyExistsException
import researchstack.backend.application.exception.NotFoundException
import researchstack.backend.application.exception.UnauthorizedException
import java.util.NoSuchElementException

class GrpcExceptionHandler : GrpcStatusFunction {
    override fun apply(ctx: RequestContext, throwable: Throwable, metadata: Metadata): Status {
        return when (throwable) {
            is AlreadyExistsException ->
                Status.ALREADY_EXISTS

            is IllegalArgumentException ->
                Status.INVALID_ARGUMENT

            is NotFoundException, is NoSuchElementException ->
                Status.NOT_FOUND

            is NotImplementedError ->
                Status.UNIMPLEMENTED

            is UnauthorizedException ->
                Status.UNAUTHENTICATED

            is InvalidProtocolBufferException ->
                Status.INVALID_ARGUMENT

            else ->
                Status.INTERNAL
        }.withDescription(throwable.message)
    }
}
