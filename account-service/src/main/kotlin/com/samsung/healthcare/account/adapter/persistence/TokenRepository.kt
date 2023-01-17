package com.samsung.healthcare.account.adapter.persistence

import com.samsung.healthcare.account.adapter.persistence.entity.TokenEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface TokenRepository : ReactiveCrudRepository<TokenEntity, Int> {
    fun findByAccessTokenAndRefreshToken(accessToken: String, refreshToken: String): Mono<TokenEntity>
    fun deleteByRefreshToken(refreshToken: String): Mono<TokenEntity>
}
