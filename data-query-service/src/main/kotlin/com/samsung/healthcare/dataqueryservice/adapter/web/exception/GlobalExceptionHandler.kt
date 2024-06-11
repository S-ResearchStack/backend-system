package com.samsung.healthcare.dataqueryservice.adapter.web.exception

import com.samsung.healthcare.dataqueryservice.application.exception.ForbiddenSqlStatementTypeException
import com.samsung.healthcare.dataqueryservice.application.exception.UnauthorizedException
import io.trino.sql.parser.ParsingException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageConversionException
import org.springframework.security.oauth2.jwt.BadJwtException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.sql.SQLDataException
import java.sql.SQLException
import java.sql.SQLInvalidAuthorizationSpecException
import java.sql.SQLNonTransientException
import java.sql.SQLSyntaxErrorException

@RestControllerAdvice
class GlobalExceptionHandler(
    @Value("#{environment.getProperty('debug') != null && environment.getProperty('debug') != 'false'}")
    private val isDebug: Boolean,
) {
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(e: UnauthorizedException) = ErrorResponse(
        message = "Unauthorized.",
        reason = if (isDebug) e.message else null
    )

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadJwtException::class)
    fun handleBadJwtException(e: BadJwtException) = ErrorResponse(
        message = "Bad JWT Exception.",
        reason = if (isDebug) e.message else null
    )

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenSqlStatementTypeException::class)
    fun handleForbiddenSqlStatementException(e: ForbiddenSqlStatementTypeException) = ErrorResponse(
        message = "Forbidden SQL statement type.",
        reason = if (isDebug) e.message else null
    )

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ParsingException::class)
    fun handleParsingException(e: ParsingException) = ErrorResponse(
        message = "Invalid SQL statement.",
        reason = if (isDebug) e.message else null
    )

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(SQLInvalidAuthorizationSpecException::class)
    fun handleSQLInvalidAuthorizationSpecException(e: SQLInvalidAuthorizationSpecException) = ErrorResponse(
        message = "Permission denied.",
        code = e.sqlState,
        reason = if (isDebug) e.message else null
    )

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SQLDataException::class)
    fun handleSQLDataException(e: SQLDataException) = ErrorResponse(
        message = "Invalid data access attempted.",
        code = e.sqlState,
        reason = if (isDebug) e.message else null
    )

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SQLSyntaxErrorException::class)
    fun handleSQLSyntaxErrorException(e: SQLSyntaxErrorException) = ErrorResponse(
        message = "Invalid SQL syntax.",
        code = e.sqlState,
        reason = if (isDebug) e.message else null
    )

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SQLNonTransientException::class)
    fun handleSQLNonTransientException(e: SQLNonTransientException) = ErrorResponse(
        message = "SQL Non-Transient Exception occurred.",
        code = e.sqlState,
        reason = if (isDebug) e.message else null
    )

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SQLException::class)
    fun handleSQLException(e: SQLException) = ErrorResponse(
        message = "SQL Exception",
        reason = if (isDebug) e.message else null
    )

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageConversionException::class)
    fun handleHttpMessageConversionException(e: HttpMessageConversionException) = ErrorResponse(
        message = "HttpMessageConversionException",
        reason = if (isDebug) e.message else null
    )

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException) = ErrorResponse(
        message = "IllegalArgumentException",
        reason = if (isDebug) e.message else null
    )

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException::class)
    fun handleException(e: RuntimeException): ErrorResponse =
        ErrorResponse(
            message = "Internal server error occurred. Please try again later.",
            reason = if (isDebug) e.message else null
        )
}
