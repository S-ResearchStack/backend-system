package com.samsung.healthcare.account.application.port.output

import reactor.core.publisher.Mono

interface TokenSigningPort {
    fun generateSignedJWT(jwtTokenCommand: JwtGenerationCommand): Mono<String>
}
