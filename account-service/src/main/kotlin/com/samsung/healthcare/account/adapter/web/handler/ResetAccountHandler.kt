package com.samsung.healthcare.account.adapter.web.handler

import com.samsung.healthcare.account.application.port.input.ResetPasswordCommand
import com.samsung.healthcare.account.application.port.input.ResetPasswordUseCase
import com.samsung.healthcare.account.application.port.input.SignInCommand
import com.samsung.healthcare.account.application.port.input.SignInUseCase
import com.samsung.healthcare.account.application.port.input.UpdateAccountProfileCommand
import com.samsung.healthcare.account.application.port.input.UpdateAccountProfileUseCase
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Component
class ResetAccountHandler(
    private val resetPasswordUseCase: ResetPasswordUseCase,
    // Note handle not only password reset but also profile update.
    private val updateAccountProfileUseCase: UpdateAccountProfileUseCase,
    private val signInUseCase: SignInUseCase,
) {
    fun resetAccount(req: ServerRequest): Mono<ServerResponse> {
        return req.bodyToMono<ResetAccountRequest>()
            .switchIfEmpty { Mono.error(IllegalArgumentException()) }
            .flatMap { resetPasswordAndUpdateProfile(it) }
            .flatMap {
                ServerResponse.ok().bodyValue(it)
            }
    }

    private fun resetPasswordAndUpdateProfile(resetCommand: ResetAccountRequest): Mono<ResetAccountResponse> =
        resetPasswordUseCase.resetPassword(
            ResetPasswordCommand(resetCommand.resetToken, resetCommand.password)
        ).flatMap { account ->
            if (resetCommand.profile.isEmpty()) Mono.just(emptyMap())
            else {
                updateAccountProfileUseCase.updateProfile(
                    UpdateAccountProfileCommand(
                        account.id,
                        resetCommand.profile
                    )
                )
            }.flatMap {
                signInUseCase.signIn(SignInCommand(account.email, resetCommand.password))
            }.map { resp ->
                ResetAccountResponse(
                    resp.account.id,
                    resp.account.email.value,
                    resp.jwt,
                    resp.refreshToken,
                    resp.account.roles.map { it.roleName },
                    resp.account.profiles
                )
            }
        }

    data class ResetAccountRequest(
        val resetToken: String,
        val password: String,
        val profile: Map<String, Any> = emptyMap()
    ) {
        init {
            require(resetToken.isNotBlank())
            require(password.isNotBlank())
        }
    }

    data class ResetAccountResponse(
        val id: String,
        val email: String,
        val jwt: String,
        val refreshToken: String,
        val roles: List<String>,
        val profile: Map<String, Any> = emptyMap(),
    )
}
