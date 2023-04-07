package com.samsung.healthcare.account.application.service

import com.samsung.healthcare.account.application.accesscontrol.Requires
import com.samsung.healthcare.account.application.exception.AlreadyExistedEmailException
import com.samsung.healthcare.account.application.port.input.AccountServicePort
import com.samsung.healthcare.account.application.port.output.AuthServicePort
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.AssignRoleAuthority
import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.account.domain.Role
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.UUID

@Service
class AccountService(
    private val authServicePort: AuthServicePort,
    private val mailService: MailService
) : AccountServicePort {

    @Requires([AssignRoleAuthority::class])
    override fun inviteUser(email: Email, roles: Collection<Role>): Mono<Void> =
        // TODO change method that generate random password
        authServicePort.registerNewUser(email, UUID.randomUUID().toString())
            .flatMap { account ->
                Mono.zip(
                    assignRolesForNewUser(account, roles).then(Mono.just(true)),
                    authServicePort.generateEmailVerificationToken(account.id, account.email)
                        .flatMap { token -> authServicePort.verifyEmail(token) }.then(Mono.just(true)),
                    authServicePort.generateResetToken(account.id),
                ).map { it.t3 }
            }.flatMap { resetToken ->
                mailService.sendInvitationMail(email, resetToken)
            }.onErrorResume(AlreadyExistedEmailException::class.java) {
                authServicePort.assignRoles(email, roles)
            }

    private fun assignRolesForNewUser(account: Account, roles: Collection<Role>): Mono<Void> =
        if (roles.isEmpty()) Mono.empty()
        else assignRoles(account.id, roles)

    @Requires([AssignRoleAuthority::class])
    override fun assignRoles(accountId: String, roles: Collection<Role>): Mono<Void> =
        if (roles.isEmpty()) Mono.error(IllegalArgumentException())
        else authServicePort.assignRoles(accountId, roles)

    @Requires([AssignRoleAuthority::class])
    override fun removeRolesFromAccount(accountId: String, roles: Collection<Role>): Mono<Void> =
        if (roles.isEmpty()) Mono.error(IllegalArgumentException())
        else authServicePort.removeRolesFromAccount(accountId, roles)
}
