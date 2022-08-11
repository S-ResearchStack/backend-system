package com.samsung.healthcare.dataqueryservice.application.exception

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.sql.SQLException

@RestControllerAdvice
class GlobalExceptionHandler(
    @Value("#{environment.getProperty('debug') != null && environment.getProperty('debug') != 'false'}")
    private val isDebug: Boolean,
) {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SQLException::class)
    fun handleSQLException(e: SQLException) = ErrorResponse(
        message = "SQL Exception",
        reason = if (isDebug) e.message else null
    )

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception) = ErrorResponse(
        message = "Exception",
        reason = if (isDebug) e.message else null
    )
}
