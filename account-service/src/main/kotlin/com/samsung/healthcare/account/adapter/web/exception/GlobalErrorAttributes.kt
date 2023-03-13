package com.samsung.healthcare.account.adapter.web.exception

import com.samsung.healthcare.account.application.exception.AlreadyExistedEmailException
import com.samsung.healthcare.account.application.exception.ExpiredRefreshTokenException
import com.samsung.healthcare.account.application.exception.InternalServerException
import com.samsung.healthcare.account.application.exception.InvalidEmailVerificationTokenException
import com.samsung.healthcare.account.application.exception.InvalidResetTokenException
import com.samsung.healthcare.account.application.exception.InvalidTokenException
import com.samsung.healthcare.account.application.exception.SignInException
import com.samsung.healthcare.account.application.exception.UnauthorizedException
import com.samsung.healthcare.account.application.exception.UnknownAccountIdException
import com.samsung.healthcare.account.application.exception.UnknownEmailException
import com.samsung.healthcare.account.application.exception.UnknownRoleException
import com.samsung.healthcare.account.application.exception.UnverifiedEmailException
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
            AlreadyExistedEmailException::class to HttpStatus.CONFLICT,
            IllegalArgumentException::class to HttpStatus.BAD_REQUEST,
            InvalidResetTokenException::class to HttpStatus.NOT_FOUND,
            InvalidTokenException::class to HttpStatus.NOT_FOUND,
            InvalidEmailVerificationTokenException::class to HttpStatus.UNAUTHORIZED,
            ExpiredRefreshTokenException::class to HttpStatus.UNAUTHORIZED,
            UnknownEmailException::class to HttpStatus.NOT_FOUND,
            UnknownRoleException::class to HttpStatus.NOT_FOUND,
            UnknownAccountIdException::class to HttpStatus.NOT_FOUND,
            UnverifiedEmailException::class to HttpStatus.UNAUTHORIZED,
            IllegalAccessException::class to HttpStatus.FORBIDDEN,
            UnauthorizedException::class to HttpStatus.UNAUTHORIZED,
            SignInException::class to HttpStatus.UNAUTHORIZED,
            JwtException::class to HttpStatus.UNAUTHORIZED,
            InternalServerException::class to HttpStatus.INTERNAL_SERVER_ERROR,
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
