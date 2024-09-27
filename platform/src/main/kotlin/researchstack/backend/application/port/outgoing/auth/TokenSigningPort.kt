package researchstack.backend.application.port.outgoing.auth

import reactor.core.publisher.Mono

interface TokenSigningPort {
    fun generateSignedJwt(jwtTokenCommand: JwtGenerationCommand): Mono<String>
}
