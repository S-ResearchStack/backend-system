package com.samsung.healthcare.account.application.port.input

import com.samsung.healthcare.account.domain.Account
import org.springframework.security.oauth2.jwt.JwtException
import reactor.core.publisher.Mono

interface GetAccountUseCase {
    @Throws(JwtException::class)
    fun getAccountFromToken(token: String): Mono<Account>
}
