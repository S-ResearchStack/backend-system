package com.samsung.healthcare.platform.application.exception

import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.server.ServerWebInputException
import java.time.format.DateTimeParseException
import kotlin.reflect.KClass

@Component
class GlobalErrorAttributes : DefaultErrorAttributes() {

    companion object {
        const val STATUS: String = "status"
        const val MESSAGE: String = "message"

        private val errorStatusMap: Map<KClass<out RuntimeException>, HttpStatus> = mapOf(
            DateTimeParseException::class to HttpStatus.BAD_REQUEST,
            BadRequestException::class to HttpStatus.BAD_REQUEST,
            ForbiddenException::class to HttpStatus.FORBIDDEN,
            InternalServerException::class to HttpStatus.INTERNAL_SERVER_ERROR,
            NotFoundException::class to HttpStatus.NOT_FOUND,
            UnauthorizedException::class to HttpStatus.UNAUTHORIZED,
            NotImplementedException::class to HttpStatus.NOT_IMPLEMENTED,
            UserAlreadyExistsException::class to HttpStatus.CONFLICT
        )
    }

    override fun getErrorAttributes(request: ServerRequest, options: ErrorAttributeOptions): Map<String, Any> =
        getError(request).let { error ->
            super.getErrorAttributes(request, options).also { errorAttributes ->
                errorAttributes[STATUS] = errorStatusMap[error::class] ?: run {
                    if (error is ServerWebInputException) error.status
                    else HttpStatus.INTERNAL_SERVER_ERROR
                }
                errorAttributes[MESSAGE] = error.message
            }
        }
}
