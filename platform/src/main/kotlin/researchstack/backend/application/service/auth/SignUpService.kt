package researchstack.backend.application.service.auth

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service
import researchstack.backend.application.port.incoming.auth.SignUpCommand
import researchstack.backend.application.port.incoming.auth.SignUpResponse
import researchstack.backend.application.port.incoming.auth.SignUpUseCase
import researchstack.backend.application.port.outgoing.auth.AuthServicePort
import researchstack.backend.domain.common.Email

@Service
class SignUpService(
    private val authServicePort: AuthServicePort
) : SignUpUseCase {
    override suspend fun signUp(command: SignUpCommand): SignUpResponse {
        return authServicePort.registerNewUser(Email(command.email), command.password)
            .map {
                SignUpResponse(it.id, it.email)
            }
            .awaitSingle()
    }
}
