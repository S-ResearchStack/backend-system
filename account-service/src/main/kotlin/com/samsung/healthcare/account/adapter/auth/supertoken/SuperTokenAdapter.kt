package com.samsung.healthcare.account.adapter.auth.supertoken

import com.samsung.healthcare.account.adapter.auth.supertoken.SuperTokensApi.CreateRoleRequest
import com.samsung.healthcare.account.adapter.auth.supertoken.SuperTokensApi.GenerateJwtRequest
import com.samsung.healthcare.account.adapter.auth.supertoken.SuperTokensApi.MetadataUpdateRequest
import com.samsung.healthcare.account.adapter.auth.supertoken.SuperTokensApi.ResetPasswordRequest
import com.samsung.healthcare.account.adapter.auth.supertoken.SuperTokensApi.RoleBinding
import com.samsung.healthcare.account.adapter.auth.supertoken.SuperTokensApi.SignRequest
import com.samsung.healthcare.account.adapter.auth.supertoken.SuperTokensApi.Status.OK
import com.samsung.healthcare.account.adapter.auth.supertoken.SuperTokensApi.StatusResponse
import com.samsung.healthcare.account.adapter.auth.supertoken.SuperTokensApi.User
import com.samsung.healthcare.account.adapter.auth.supertoken.SuperTokensApi.UserId
import com.samsung.healthcare.account.application.exception.AlreadyExistedEmailException
import com.samsung.healthcare.account.application.exception.InvalidResetTokenException
import com.samsung.healthcare.account.application.exception.SignInException
import com.samsung.healthcare.account.application.exception.UnknownAccountIdException
import com.samsung.healthcare.account.application.exception.UnknownEmailException
import com.samsung.healthcare.account.application.port.output.AuthServicePort
import com.samsung.healthcare.account.application.port.output.JwtGenerationCommand
import com.samsung.healthcare.account.application.port.output.TokenSigningPort
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.account.domain.Role
import com.samsung.healthcare.account.domain.Role.ProjectRole
import com.samsung.healthcare.account.domain.RoleFactory
import feign.FeignException.NotFound
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class SuperTokenAdapter(
    private val apiClient: SuperTokensApi
) : AuthServicePort, TokenSigningPort {
    override fun registerNewUser(email: Email, password: String): Mono<Account> =
        apiClient.signUp(SignRequest(email.value, password))
            .mapNotNull {
                if (it.status == OK) it.user?.toAccount()
                else throw AlreadyExistedEmailException()
            }

    override fun generateResetToken(accountId: String): Mono<String> =
        apiClient.generateResetToken(UserId(accountId))
            .mapNotNull {
                if (it.status == OK) it.token
                else throw UnknownAccountIdException()
            }

    override fun resetPassword(resetToken: String, newPassword: String): Mono<String> =
        apiClient.resetPassword(ResetPasswordRequest(resetToken, newPassword))
            .mapNotNull {
                if (it.status == OK) it.userId
                else throw InvalidResetTokenException()
            }

    override fun assignRoles(email: Email, roles: Collection<Role>): Mono<Void> =
        apiClient.getAccountWithEmail(email.value)
            .mapNotNull {
                if (it.status == OK && it.user != null) it.user.toAccount()
                else throw UnknownEmailException()
            }.flatMap { assignRoles(it.id, roles) }

    override fun assignRoles(accountId: String, roles: Collection<Role>): Mono<Void> =
        handleRoleBinding(accountId, roles) { roleBinding ->
            apiClient.assignRoles(roleBinding)
        }

    override fun removeRolesFromAccount(accountId: String, roles: Collection<Role>): Mono<Void> =
        handleRoleBinding(accountId, roles) { roleBinding ->
            apiClient.removeUserRole(roleBinding)
        }

    private fun handleRoleBinding(
        accountId: String,
        roles: Collection<Role>,
        handlerFunction: (RoleBinding) -> Mono<StatusResponse>
    ): Mono<Void> =
        Flux.fromIterable(roles)
            .flatMap { handlerFunction(RoleBinding(accountId, it.roleName)) }
            // TODO handle exceptions
            // TODO haw to handle some fails
            .then()

    override fun createRoles(roles: Collection<Role>): Mono<Void> =
        Flux.fromIterable(roles)
            .flatMap { apiClient.createRoles(CreateRoleRequest(it.roleName)) }
            .then()

    override fun signIn(email: Email, password: String): Mono<Account> =
        apiClient.signIn(SignRequest(email.value, password))
            .onErrorMap(NotFound::class.java) { SignInException() }
            // TODO handle other exceptions
            .onErrorMap { SignInException() }
            .mapNotNull {
                it.user ?: throw SignInException()
            }.flatMap { user ->
                Mono.zip(listUserRoles(user.id), getUserMetaData(user.id)) { roles, metadata ->
                    Account(user.id, Email(user.email), roles, metadata)
                }
            }

    private fun getUserMetaData(id: String): Mono<Map<String, Any>> =
        apiClient.getMetaData(id)
            .map { it.metadata }

    override fun listUserRoles(id: String): Mono<List<Role>> =
        apiClient.listUserRoles(id)
            .map { response ->
                response.roles.map {
                    RoleFactory.createRole(it)
                }
            }

    override fun listUsers(): Mono<List<Account>> =
        apiClient.listUsers()
            .flatMapMany { resp -> Flux.fromIterable(resp.users.map { it.user }) }
            .flatMap { user ->
                Mono.zip(listUserRoles(user.id), getUserMetaData(user.id)) { roles, metadata ->
                    Account(user.id, Email(user.email), roles, metadata)
                }
            }.collectList()

    override fun retrieveUsersAssociatedWithRoles(projectRoles: List<ProjectRole>): Mono<List<Account>> =
        Flux.fromIterable(projectRoles)
            .flatMap { role -> apiClient.listUsersOfRole(role.roleName) }
            .map { usersResponse -> usersResponse.users }
            .onErrorReturn(NotFound::class.java, emptyList())
            .collectList()
            .flatMapMany { Flux.fromIterable(it.flatten().toHashSet()) }
            .flatMap { apiClient.getAccountWithId(it) }
            .mapNotNull {
                it.user?.let { user -> user }
            }.flatMap { user ->
                Mono.zip(listUserRoles(user.id), getUserMetaData(user.id)) { roles, metadata ->
                    Account(user.id, Email(user.email), roles, metadata)
                }
            }.collectList()

    override fun updateAccountProfile(accountId: String, profile: Map<String, Any>): Mono<Map<String, Any>> =
        apiClient.updateMetadata(MetadataUpdateRequest(accountId, profile))
            .onErrorMap(NotFound::class.java) { UnknownAccountIdException() }
            .mapNotNull { it.metadata }

    override fun generateSignedJWT(jwtTokenCommand: JwtGenerationCommand): Mono<String> =
        apiClient.generateSignedJwt(
            GenerateJwtRequest(
                payload = mapOf(
                    "sub" to jwtTokenCommand.subject,
                    "email" to jwtTokenCommand.email,
                    "roles" to jwtTokenCommand.roles.map { it.roleName }
                ),
                jwksDomain = jwtTokenCommand.issuer,
                validity = jwtTokenCommand.lifeTime
            )
        ).map { it.jwt }

    private fun User.toAccount(): Account =
        Account(
            id,
            Email(email),
            emptyList()
        )
}
