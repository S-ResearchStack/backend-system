package com.samsung.healthcare.dataqueryservice.adapter.web.interceptor

import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.account.domain.Role
import com.samsung.healthcare.account.domain.RoleFactory
import com.samsung.healthcare.dataqueryservice.application.context.AuthContext
import com.samsung.healthcare.dataqueryservice.application.exception.UnauthorizedException
import org.springframework.http.HttpHeaders
import org.springframework.security.oauth2.jwt.BadJwtException
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationInterceptor(
    private val jwtDecoder: JwtDecoder,
) : HandlerInterceptor {
    private val bearerPrefix = "Bearer "
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val bearerString = request.getHeader(HttpHeaders.AUTHORIZATION) ?: throw UnauthorizedException()
        if (!bearerString.startsWith(bearerPrefix)) throw UnauthorizedException()

        AuthContext.setValue(
            AuthContext.ACCOUNT_ID_KEY_NAME,
            getAccountFromToken(bearerString.substring(bearerPrefix.length)).id
        )
        return true
    }

    private fun getAccountFromToken(jwt: String): Account {
        return kotlin.runCatching {
            val decodedJwt = jwtDecoder.decode(jwt)
            Account(
                id = decodedJwt.subject,
                email = decodedJwt.getEmail(),
                roles = decodedJwt.getRoles()
            )
        }.getOrElse { throw BadJwtException("invalid token") }
    }

    private fun Jwt.getEmail(): Email =
        Email(this.claims["email"] as String)

    @Suppress("UNCHECKED_CAST")
    private fun Jwt.getRoles(): List<Role> =
        (this.claims["roles"] as List<String>)
            .map { RoleFactory.createRole(it) }
}
