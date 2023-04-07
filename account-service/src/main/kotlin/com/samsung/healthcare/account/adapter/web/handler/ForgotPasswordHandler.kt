package com.samsung.healthcare.account.adapter.web.handler

import com.samsung.healthcare.account.application.port.input.ForgotPasswordUseCase
import com.samsung.healthcare.account.domain.Email
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Component
class ForgotPasswordHandler(
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
) {
    fun forgotPassword(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<ForgotPasswordRequest>()
            .switchIfEmpty { Mono.error(IllegalArgumentException()) }
            .flatMap {
                forgotPasswordUseCase.forgotPassword(Email(it.email))
            }
            .then(ServerResponse.ok().build())

    data class ForgotPasswordRequest(val email: String)
}
