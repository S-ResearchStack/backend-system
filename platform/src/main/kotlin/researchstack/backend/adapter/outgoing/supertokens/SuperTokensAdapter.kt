package researchstack.backend.adapter.outgoing.supertokens

import feign.FeignException.NotFound
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import researchstack.backend.adapter.outgoing.supertokens.SuperTokensApi.SignRequest
import researchstack.backend.adapter.outgoing.supertokens.SuperTokensApi.Status.OK
import researchstack.backend.adapter.outgoing.supertokens.SuperTokensApi.User
import researchstack.backend.application.exception.AlreadyExistsException
import researchstack.backend.application.exception.InternalServerException
import researchstack.backend.application.exception.NotFoundException
import researchstack.backend.application.exception.UnauthorizedException
import researchstack.backend.application.port.outgoing.auth.AuthServicePort
import researchstack.backend.application.port.outgoing.auth.JwtGenerationCommand
import researchstack.backend.application.port.outgoing.auth.TokenSigningPort
import researchstack.backend.domain.auth.Account
import researchstack.backend.domain.common.Email

@Component
class SuperTokensAdapter(
    private val apiClient: SuperTokensApi
) : AuthServicePort, TokenSigningPort {
    override fun registerNewUser(email: Email, password: String): Mono<Account> {
        require(password.isNotBlank())

        return apiClient.signUp(SignRequest(email.value, password))
            .mapNotNull {
                if (it.status == SuperTokensApi.Status.EMAIL_ALREADY_EXISTS_ERROR) {
                    throw AlreadyExistsException()
                } else if (it.status != OK) throw InternalServerException("superTokens status: ${it.status}")
                it.user?.toAccount()
            }
    }

    override fun signIn(email: Email, password: String): Mono<Account> {
        require(password.isNotBlank())
        return apiClient.signIn(SignRequest(email.value, password))
            .onErrorMap(NotFound::class.java) { NotFoundException() }
            .onErrorMap { UnauthorizedException() }
            .mapNotNull {
                it.user ?: throw UnauthorizedException()
            }
            .map { user -> user.toAccount() }
    }

    override fun generateSignedJwt(jwtTokenCommand: JwtGenerationCommand): Mono<String> {
        require(0 < jwtTokenCommand.lifeTime)
        val email = Email(jwtTokenCommand.email)

        return apiClient.generateSignedJwt(
            SuperTokensApi.GenerateJwtRequest(
                payload = mapOf(
                    "sub" to jwtTokenCommand.subject,
                    "email" to email.value
                ),
                jwksDomain = jwtTokenCommand.issuer,
                validity = jwtTokenCommand.lifeTime
            )
        ).map { it.jwt }
    }

    private fun User.toAccount(): Account =
        Account(
            id,
            Email(email)
        )
}
