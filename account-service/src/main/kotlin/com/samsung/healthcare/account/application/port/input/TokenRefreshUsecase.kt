package com.samsung.healthcare.account.application.port.input

import reactor.core.publisher.Mono

interface TokenRefreshUsecase {
    fun refreshToken(tokenRefreshCommand: TokenRefreshCommand): Mono<TokenRefreshResponse>
}
