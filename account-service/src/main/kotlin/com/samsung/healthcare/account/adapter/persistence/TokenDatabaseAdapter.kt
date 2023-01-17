package com.samsung.healthcare.account.adapter.persistence

import com.samsung.healthcare.account.adapter.persistence.entity.toEntity
import com.samsung.healthcare.account.application.port.output.TokenStoragePort
import com.samsung.healthcare.account.domain.Token
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class TokenDatabaseAdapter(
    private val tokenRepository: TokenRepository,
) : TokenStoragePort {
    override fun save(token: Token): Mono<Void> {
        return tokenRepository.save(token.toEntity()).then()
    }

    override fun findByAccessTokenAndRefreshToken(accessToken: String, refreshToken: String): Mono<Token> {
        return tokenRepository.findByAccessTokenAndRefreshToken(accessToken, refreshToken).map { it.toDomain() }
    }

    override fun deleteByRefreshToken(refreshToken: String): Mono<Void> {
        return tokenRepository.deleteByRefreshToken(refreshToken).then()
    }
}
