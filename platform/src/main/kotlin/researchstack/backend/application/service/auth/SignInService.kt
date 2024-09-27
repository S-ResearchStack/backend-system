package researchstack.backend.application.service.auth

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service
import researchstack.backend.application.port.incoming.auth.SignInCommand
import researchstack.backend.application.port.incoming.auth.SignInResponse
import researchstack.backend.application.port.incoming.auth.SignInUseCase
import researchstack.backend.application.port.outgoing.auth.AuthServicePort
import researchstack.backend.domain.common.Email

@Service
class SignInService(
    private val authServicePort: AuthServicePort,
    private val tokenService: TokenService
) : SignInUseCase {
    override suspend fun signIn(command: SignInCommand): SignInResponse {
        return authServicePort.signIn(Email(command.email), command.password)
            .flatMap { account ->
                tokenService.generateToken(account)
                    .map {
                        SignInResponse(
                            account.id,
                            account.email,
                            it.accessToken,
                            it.refreshToken
                        )
                    }
            }
            .awaitSingle()
    }
}
