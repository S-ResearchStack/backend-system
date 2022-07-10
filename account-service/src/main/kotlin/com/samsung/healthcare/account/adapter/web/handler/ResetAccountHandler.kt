package com.samsung.healthcare.account.adapter.web.handler

import com.samsung.healthcare.account.application.port.input.ResetPasswordCommand
import com.samsung.healthcare.account.application.port.input.ResetPasswordUseCase
import com.samsung.healthcare.account.application.port.input.UpdateAccountProfileCommand
import com.samsung.healthcare.account.application.port.input.UpdateAccountProfileUseCase
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

@Component
class ResetAccountHandler(
    private val resetPasswordUseCase: ResetPasswordUseCase,
    // Note handle not only password reset but also profile update.
    private val updateAccountProfileUseCase: UpdateAccountProfileUseCase
) {
    fun resetAccount(req: ServerRequest): Mono<ServerResponse> {
        return req.bodyToMono<ResetAccountRequest>()
            .flatMap { resetPasswordAndUpdateProfile(it) }
            .flatMap {
                ServerResponse.ok().bodyValue(it)
            }
    }

    private fun resetPasswordAndUpdateProfile(resetCommand: ResetAccountRequest): Mono<Map<String, Any>> =
        resetPasswordUseCase.resetPassword(
            ResetPasswordCommand(resetCommand.resetToken, resetCommand.password)
        ).flatMap { accountId ->
            if (resetCommand.profile.isEmpty()) Mono.just(emptyMap())
            else updateAccountProfileUseCase.updateProfile(
                UpdateAccountProfileCommand(
                    accountId,
                    resetCommand.profile
                )
            )
        }

    data class ResetAccountRequest(
        val resetToken: String,
        val password: String,
        val profile: Map<String, Any>
    )
}
