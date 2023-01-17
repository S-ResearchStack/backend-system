package com.samsung.healthcare.account.adapter.web.handler

import com.samsung.healthcare.account.application.port.input.TokenRefreshCommand
import com.samsung.healthcare.account.application.port.input.TokenRefreshUsecase
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

@Component
class TokenRefreshHandler(
    private val tokenRefreshService: TokenRefreshUsecase,
) {

    fun refreshToken(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<TokenRefreshCommand>()
            .flatMap { tokenRefreshService.refreshToken(it) }
            .flatMap { ServerResponse.ok().bodyValue(it) }
}
