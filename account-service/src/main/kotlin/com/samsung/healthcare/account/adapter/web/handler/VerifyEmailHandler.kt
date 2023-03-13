package com.samsung.healthcare.account.adapter.web.handler

import com.samsung.healthcare.account.application.port.input.VerifyEmailUseCase
import com.samsung.healthcare.account.domain.Email
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Component
class VerifyEmailHandler(
    private val verifyEmailUseCase: VerifyEmailUseCase,
) {
    fun verifyEmail(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<VerifyEmailRequest>()
            .switchIfEmpty { Mono.error(IllegalArgumentException()) }
            .flatMap {
                verifyEmailUseCase.verifyEmail(it.token)
            }
            .map { resp ->
                VerifyEmailResponse(
                    resp.account.id,
                    resp.account.email.value,
                    resp.jwt,
                    resp.refreshToken,
                    resp.account.roles.map { it.roleName },
                    resp.account.profiles
                )
            }
            .flatMap { ServerResponse.ok().bodyValue(it) }

    fun resendVerificationEmail(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<ResendVerificationEmailRequest>()
            .switchIfEmpty { Mono.error(IllegalArgumentException()) }
            .flatMap {
                verifyEmailUseCase.resendVerificationEmail(Email(it.email))
            }
            .flatMap { ServerResponse.ok().build() }

    data class VerifyEmailRequest(val token: String)

    data class VerifyEmailResponse(
        val id: String,
        val email: String,
        val jwt: String,
        val refreshToken: String,
        val roles: List<String>,
        val profile: Map<String, Any>
    )

    data class ResendVerificationEmailRequest(val email: String)
}
