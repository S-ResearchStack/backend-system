package com.samsung.healthcare.account.adapter.auth.supertoken

import com.samsung.healthcare.account.adapter.auth.supertoken.SuperTokensApi.CreateRoleRequest
import com.samsung.healthcare.account.adapter.auth.supertoken.SuperTokensApi.GenerateJwtRequest
import com.samsung.healthcare.account.adapter.auth.supertoken.SuperTokensApi.MetadataUpdateRequest
import com.samsung.healthcare.account.adapter.auth.supertoken.SuperTokensApi.ResetPasswordRequest
import com.samsung.healthcare.account.adapter.auth.supertoken.SuperTokensApi.RoleBinding
import com.samsung.healthcare.account.adapter.auth.supertoken.SuperTokensApi.SignRequest
import com.samsung.healthcare.account.adapter.auth.supertoken.SuperTokensApi.Status.EMAIL_ALREADY_EXISTS_ERROR
import com.samsung.healthcare.account.adapter.auth.supertoken.SuperTokensApi.Status.EMAIL_ALREADY_VERIFIED_ERROR
import com.samsung.healthcare.account.adapter.auth.supertoken.SuperTokensApi.Status.EMAIL_VERIFICATION_INVALID_TOKEN_ERROR
import com.samsung.healthcare.account.adapter.auth.supertoken.SuperTokensApi.Status.OK
import com.samsung.healthcare.account.adapter.auth.supertoken.SuperTokensApi.Status.UNKNOWN_ROLE_ERROR
import com.samsung.healthcare.account.adapter.auth.supertoken.SuperTokensApi.StatusResponse
import com.samsung.healthcare.account.adapter.auth.supertoken.SuperTokensApi.User
import com.samsung.healthcare.account.adapter.auth.supertoken.SuperTokensApi.UserId
import com.samsung.healthcare.account.application.exception.AlreadyExistedEmailException
import com.samsung.healthcare.account.application.exception.InternalServerException
import com.samsung.healthcare.account.application.exception.InvalidEmailVerificationTokenException
import com.samsung.healthcare.account.application.exception.InvalidResetTokenException
import com.samsung.healthcare.account.application.exception.SignInException
import com.samsung.healthcare.account.application.exception.UnknownAccountIdException
import com.samsung.healthcare.account.application.exception.UnknownEmailException
import com.samsung.healthcare.account.application.exception.UnknownRoleException
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
    override fun registerNewUser(email: Email, password: String): Mono<Account> {
        require(password.isNotBlank())
        return apiClient.signUp(SignRequest(email.value, password))
            .mapNotNull {
                if (it.status == OK) it.user?.toAccount()
                else if (it.status == EMAIL_ALREADY_EXISTS_ERROR) throw AlreadyExistedEmailException()
                else throw InternalServerException()
            }
    }

    override fun generateResetToken(accountId: String): Mono<String> {
        require(accountId.isNotBlank())

        return apiClient.generateResetToken(UserId(accountId))
            .mapNotNull {
                if (it.status == OK) it.token
                else throw UnknownAccountIdException()
            }
    }

    override fun resetPassword(resetToken: String, newPassword: String): Mono<String> {
        require(resetToken.isNotBlank())
        require(newPassword.isNotBlank())

        return apiClient.resetPassword(ResetPasswordRequest(resetToken, newPassword))
            .mapNotNull {
                if (it.status == OK) it.userId
                else throw InvalidResetTokenException()
            }
    }

    override fun assignRoles(email: Email, roles: Collection<Role>): Mono<Void> {
        require(roles.isNotEmpty())

        return apiClient.getAccountWithEmail(email.value)
            .mapNotNull {
                if (it.status == OK && it.user != null) it.user.toAccount()
                else throw UnknownEmailException()
            }.flatMap { assignRoles(it.id, roles) }
    }

    override fun assignRoles(accountId: String, roles: Collection<Role>): Mono<Void> {
        require(accountId.isNotBlank())

        return handleRoleBinding(accountId, roles) { roleBinding ->
            apiClient.assignRoles(roleBinding)
                .mapNotNull {
                    if (it.status == OK) it
                    else if (it.status == UNKNOWN_ROLE_ERROR)
                        throw UnknownRoleException("unknown role: ${roleBinding.role}")
                    else throw InternalServerException()
                }
        }
    }

    override fun removeRolesFromAccount(accountId: String, roles: Collection<Role>): Mono<Void> {
        require(accountId.isNotBlank())

        return handleRoleBinding(accountId, roles) { roleBinding ->
            apiClient.removeUserRole(roleBinding)
        }
    }

    private fun handleRoleBinding(
        accountId: String,
        roles: Collection<Role>,
        handlerFunction: (RoleBinding) -> Mono<StatusResponse>
    ): Mono<Void> {
        require(roles.isNotEmpty())
        return Flux.fromIterable(roles)
            .flatMap { handlerFunction(RoleBinding(accountId, it.roleName)) }
            // TODO handle exceptions
            // TODO haw to handle some fails
            .then()
    }

    override fun createRoles(roles: Collection<Role>): Mono<Void> {
        require(roles.isNotEmpty())

        return Flux.fromIterable(roles)
            .flatMap { apiClient.createRoles(CreateRoleRequest(it.roleName)) }
            .then()
    }

    override fun signIn(email: Email, password: String): Mono<Account> {
        require(password.isNotBlank())

        return apiClient.signIn(SignRequest(email.value, password))
            .onErrorMap(NotFound::class.java) { SignInException() }
            // TODO handle other exceptions
            .onErrorMap { SignInException() }
            .mapNotNull {
                it.user ?: throw SignInException()
            }
            .flatMap { user -> getAccount(user.id, user.email) }
    }

    private fun getUserMetaData(id: String): Mono<Map<String, Any>> =
        apiClient.getMetaData(id)
            .map { it.metadata }

    private fun getAccount(id: String, email: String): Mono<Account> =
        Mono.zip(listUserRoles(id), getUserMetaData(id)) { roles, metadata ->
            Account(id, Email(email), roles, metadata)
        }

    override fun listUserRoles(id: String): Mono<List<Role>> {
        require(id.isNotBlank())

        return apiClient.listUserRoles(id)
            .map { response ->
                response.roles.map {
                    RoleFactory.createRole(it)
                }
            }
    }

    override fun getAccount(id: String): Mono<Account> {
        require(id.isNotBlank())

        return apiClient.getAccountWithId(id)
            .mapNotNull {
                if (it.status == OK && it.user != null) it.user
                else throw UnknownAccountIdException()
            }
            .flatMap { user -> getAccount(user.id, user.email) }
    }

    override fun listUsers(): Mono<List<Account>> =
        apiClient.listUsers()
            .flatMapMany { resp -> Flux.fromIterable(resp.users.map { it.user }) }
            .flatMap { user -> getAccount(user.id, user.email) }
            .collectList()

    override fun retrieveUsersAssociatedWithRoles(projectRoles: List<ProjectRole>): Mono<List<Account>> {
        require(projectRoles.isNotEmpty())

        return Flux.fromIterable(projectRoles)
            .flatMap { role -> apiClient.listUsersOfRole(role.roleName) }
            .map { usersResponse -> usersResponse.users }
            .onErrorReturn(NotFound::class.java, emptyList())
            .collectList()
            .flatMapMany { Flux.fromIterable(it.flatten().toHashSet()) }
            .flatMap { apiClient.getAccountWithId(it) }
            .mapNotNull {
                it.user?.let { user -> user }
            }
            .flatMap { user -> getAccount(user.id, user.email) }
            .collectList()
    }

    override fun updateAccountProfile(accountId: String, profile: Map<String, Any>): Mono<Map<String, Any>> {
        require(accountId.isNotBlank())

        return apiClient.updateMetadata(MetadataUpdateRequest(accountId, profile))
            .onErrorMap(NotFound::class.java) { UnknownAccountIdException() }
            .mapNotNull { it.metadata }
    }

    override fun generateEmailVerificationToken(accountId: String, email: Email): Mono<String> =
        apiClient.generateEmailVerificationToken(
            SuperTokensApi.GenerateEmailVerificationTokenRequest(accountId, email.value)
        )
            .mapNotNull {
                if (it.status == OK) it.token
                else if (it.status == EMAIL_ALREADY_VERIFIED_ERROR)
                    throw AlreadyExistedEmailException("email already verified")
                else throw InternalServerException()
            }

    override fun generateEmailVerificationToken(email: Email): Mono<String> =
        apiClient.getAccountWithEmail(email.value)
            .mapNotNull {
                if (it.status == OK && it.user != null) it.user.id
                else throw UnknownEmailException()
            }
            .flatMap { accountId ->
                generateEmailVerificationToken(accountId, email)
            }

    override fun verifyEmail(emailVerificationToken: String): Mono<Account> =
        apiClient.verifyEmail(SuperTokensApi.VerifyEmailRequest(emailVerificationToken))
            .mapNotNull {
                when (it.status) {
                    OK -> it
                    EMAIL_VERIFICATION_INVALID_TOKEN_ERROR -> throw InvalidEmailVerificationTokenException()
                    else -> throw InternalServerException()
                }
            }
            .flatMap {
                require(!it.userId.isNullOrEmpty())
                require(!it.email.isNullOrEmpty())
                getAccount(it.userId, it.email)
            }

    override fun isVerifiedEmail(accountId: String, email: Email): Mono<Boolean> =
        apiClient.isVerifiedEmail(accountId, email.value)
            .mapNotNull {
                if (it.status == OK) it.isVerified
                else throw InternalServerException()
            }

    override fun countUsers(): Mono<Int> =
        apiClient.countUsers()
            .mapNotNull {
                if (it.status == OK) it.count
                else throw InternalServerException()
            }

    override fun generateSignedJWT(jwtTokenCommand: JwtGenerationCommand): Mono<String> {
        require(0 < jwtTokenCommand.lifeTime)
        val email = Email(jwtTokenCommand.email)

        return apiClient.generateSignedJwt(
            GenerateJwtRequest(
                payload = mapOf(
                    "sub" to jwtTokenCommand.subject,
                    "email" to email.value,
                    "roles" to jwtTokenCommand.roles.map { it.roleName }
                ),
                jwksDomain = jwtTokenCommand.issuer,
                validity = jwtTokenCommand.lifeTime
            )
        ).map { it.jwt }
    }

    private fun User.toAccount(): Account =
        Account(
            id,
            Email(email),
            emptyList()
        )
}
