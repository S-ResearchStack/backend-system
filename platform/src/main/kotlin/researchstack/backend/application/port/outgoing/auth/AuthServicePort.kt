package researchstack.backend.application.port.outgoing.auth

import reactor.core.publisher.Mono
import researchstack.backend.domain.auth.Account
import researchstack.backend.domain.common.Email

interface AuthServicePort {
    fun registerNewUser(email: Email, password: String): Mono<Account>

    fun signIn(email: Email, password: String): Mono<Account>
}
