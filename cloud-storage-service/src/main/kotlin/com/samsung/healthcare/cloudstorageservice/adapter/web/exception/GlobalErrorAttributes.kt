package com.samsung.healthcare.cloudstorageservice.adapter.web.exception

import com.samsung.healthcare.cloudstorageservice.application.exception.BadRequestException
import com.samsung.healthcare.cloudstorageservice.application.exception.ForbiddenException
import com.samsung.healthcare.cloudstorageservice.application.exception.NotFoundException
import com.samsung.healthcare.cloudstorageservice.application.exception.UnauthorizedException
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.jwt.JwtException
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.server.ResponseStatusException
import kotlin.reflect.KClass

@Component
class GlobalErrorAttributes : DefaultErrorAttributes() {

    companion object {
        const val STATUS: String = "status"
        const val MESSAGE: String = "message"

        private val errorStatusMap: Map<KClass<out Exception>, HttpStatus> = mapOf(
            BadRequestException::class to HttpStatus.BAD_REQUEST,
            IllegalArgumentException::class to HttpStatus.BAD_REQUEST,
            IllegalAccessException::class to HttpStatus.FORBIDDEN,
            JwtException::class to HttpStatus.UNAUTHORIZED,
            ForbiddenException::class to HttpStatus.FORBIDDEN,
            NotFoundException::class to HttpStatus.NOT_FOUND,
            UnauthorizedException::class to HttpStatus.UNAUTHORIZED,
        )
    }

    override fun getErrorAttributes(request: ServerRequest, options: ErrorAttributeOptions): Map<String, Any> =
        getError(request).let { error ->
            super.getErrorAttributes(request, options).also { errorAttributes ->
                errorAttributes[STATUS] = errorStatusMap[error::class] ?: run {
                    if (error is ResponseStatusException) error.status
                    else HttpStatus.INTERNAL_SERVER_ERROR
                }
                errorAttributes[MESSAGE] = error.message
            }
        }
}
