package com.samsung.healthcare.account.adapter.web.handler

import com.samsung.healthcare.account.application.port.input.SignUpCommand
import com.samsung.healthcare.account.application.port.input.SignUpUseCase
import com.samsung.healthcare.account.domain.Email
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Component
class SignUpHandler(
    private val signUpUseCase: SignUpUseCase,
) {
    fun signUp(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<SignUpRequest>()
            .switchIfEmpty { Mono.error(IllegalArgumentException()) }
            .flatMap {
                signUpUseCase.signUp(
                    SignUpCommand(Email(it.email), it.password, it.profile)
                )
            }
            .then(ServerResponse.ok().build())

    data class SignUpRequest(val email: String, val password: String, val profile: Map<String, Any>)
}
