package researchstack.backend.adapter.exception

import com.linecorp.armeria.common.HttpData
import com.linecorp.armeria.common.HttpResponse
import com.linecorp.armeria.common.HttpStatus
import com.linecorp.armeria.common.ResponseHeaders
import com.linecorp.armeria.server.ServerErrorHandler
import com.linecorp.armeria.server.ServiceRequestContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import researchstack.backend.application.exception.AlreadyExistsException
import researchstack.backend.application.exception.ForbiddenException
import researchstack.backend.application.exception.NotFoundException
import researchstack.backend.application.exception.UnauthorizedException
import java.nio.charset.StandardCharsets
import java.util.NoSuchElementException

@Component
class HttpExceptionHandler(
    @Value("#{environment.getProperty('debug') != null && environment.getProperty('debug') == 'true'}")
    private val isDebug: Boolean
) : ServerErrorHandler {
    override fun onServiceException(ctx: ServiceRequestContext, throwable: Throwable): HttpResponse {
        return HttpResponse.of(
            ResponseHeaders.of(
                when (throwable) {
                    is IllegalArgumentException ->
                        HttpStatus.BAD_REQUEST // 400
                    is UnauthorizedException ->
                        HttpStatus.UNAUTHORIZED // 401
                    is ForbiddenException ->
                        HttpStatus.FORBIDDEN // 403
                    is NotFoundException, is NoSuchElementException ->
                        HttpStatus.NOT_FOUND // 404
                    is AlreadyExistsException ->
                        HttpStatus.CONFLICT // 409
                    is NotImplementedError ->
                        HttpStatus.NOT_IMPLEMENTED // 501
                    else ->
                        HttpStatus.INTERNAL_SERVER_ERROR // 500
                },
                // TODO: Apply cors handler to exception responses
                "Access-Control-Allow-Origin",
                "*"
            ),
            HttpData.of(
                StandardCharsets.UTF_8,
                if (isDebug) throwable.message ?: "" else ""
            )
        )
    }
}
