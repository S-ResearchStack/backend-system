package com.samsung.healthcare.account.application.service

import com.samsung.healthcare.account.application.port.input.SignUpCommand
import com.samsung.healthcare.account.application.port.input.SignUpUseCase
import com.samsung.healthcare.account.application.port.output.AuthServicePort
import com.samsung.healthcare.account.domain.Role
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class SignUpService(
    private val authServicePort: AuthServicePort,
    private val mailService: MailService,
    private val registerRolesService: RegisterRolesService,
) : SignUpUseCase {
    override fun signUp(signUpCommand: SignUpCommand): Mono<Void> =
        authServicePort.registerNewUser(signUpCommand.email, signUpCommand.password)
            .flatMap { account ->
                Mono.zip(
                    authServicePort.updateAccountProfile(account.id, signUpCommand.profile),
                    authServicePort.countUsers()
                        .filter { it == 1 }
                        .flatMap {
                            registerRolesService.registerRoles(listOf(Role.TeamAdmin))
                                .then(authServicePort.assignRoles(account.id, listOf(Role.TeamAdmin)))
                        }
                        .then(Mono.just(true)),
                    authServicePort.generateEmailVerificationToken(account.id, signUpCommand.email)
                ).map { it.t3 }
            }
            .flatMap { token ->
                mailService.sendVerificationMail(signUpCommand.email, token)
            }
}
