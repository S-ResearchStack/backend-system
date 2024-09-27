package researchstack.backend.application.service.auth

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import researchstack.backend.application.port.outgoing.auth.JwtGenerationCommand
import researchstack.backend.application.port.outgoing.auth.TokenSigningPort
import researchstack.backend.config.TokenProperties
import researchstack.backend.domain.auth.Account
import researchstack.backend.domain.auth.Token

@Service
class TokenService(
    private val tokenProperties: TokenProperties,
    private val tokenSigningPort: TokenSigningPort
) {
    fun generateToken(account: Account): Mono<Token> {
        return tokenSigningPort.generateSignedJwt(
            JwtGenerationCommand(
                issuer = tokenProperties.accessToken.issuer,
                subject = account.id,
                email = account.email.value,
                lifeTime = tokenProperties.accessToken.lifetime
            )
        ).map { accessToken ->
            Token.generateToken(
                account.id,
                accessToken,
                tokenProperties.refreshToken.lifetime,
                tokenProperties.refreshToken.length
            )
        }
    }
}
