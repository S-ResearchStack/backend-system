package com.samsung.healthcare.account.adapter.web.exception

import com.samsung.healthcare.account.application.exception.InvalidResetTokenException
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.server.ServerWebInputException
import kotlin.reflect.KClass

@Component
class GlobalErrorAttributes : DefaultErrorAttributes() {

    companion object {
        const val STATUS: String = "status"
        const val MESSAGE: String = "message"

        private val errorStatusMap: Map<KClass<out RuntimeException>, HttpStatus> = mapOf(
            IllegalArgumentException::class to HttpStatus.BAD_REQUEST,
            InvalidResetTokenException::class to HttpStatus.NOT_FOUND,
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
