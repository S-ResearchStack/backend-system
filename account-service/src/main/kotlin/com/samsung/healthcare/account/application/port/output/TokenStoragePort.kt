package com.samsung.healthcare.account.application.port.output

import com.samsung.healthcare.account.domain.Token
import reactor.core.publisher.Mono

interface TokenStoragePort {
    fun save(token: Token): Mono<Void>

    fun findByAccessTokenAndRefreshToken(accessToken: String, refreshToken: String): Mono<Token>

    fun deleteByRefreshToken(refreshToken: String): Mono<Void>
}
