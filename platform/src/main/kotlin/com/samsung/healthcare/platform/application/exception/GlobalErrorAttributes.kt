package com.samsung.healthcare.platform.application.exception

import kotlin.reflect.KClass
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest

@Component
class GlobalErrorAttributes : DefaultErrorAttributes() {

    companion object {
        const val STATUS: String = "status"
        const val MESSAGE: String = "message"

        private val errorStatusMap: Map<KClass<out RuntimeException>, HttpStatus> = mapOf(
            BadRequestException::class to HttpStatus.BAD_REQUEST,
            ForbiddenException::class to HttpStatus.FORBIDDEN,
            InternalServerException::class to HttpStatus.INTERNAL_SERVER_ERROR,
            NotFoundException::class to HttpStatus.NOT_FOUND,
            UnauthorizedException::class to HttpStatus.UNAUTHORIZED,
            NotImplementedException::class to HttpStatus.NOT_IMPLEMENTED
        )
    }

    override fun getErrorAttributes(request: ServerRequest, options: ErrorAttributeOptions): Map<String, Any> =
        getError(request).let { error ->
            super.getErrorAttributes(request, options).also { errorAttributes ->
                errorAttributes[STATUS] = errorStatusMap[error::class] ?: HttpStatus.INTERNAL_SERVER_ERROR
                errorAttributes[MESSAGE] = error.message
            }
        }
}
