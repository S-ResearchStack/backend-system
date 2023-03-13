package com.samsung.healthcare.account.adapter.web.handler

import com.samsung.healthcare.account.application.port.input.AccountServicePort
import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.account.domain.RoleFactory
import org.springframework.http.HttpStatus.MULTI_STATUS
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Component
class InvitationHandler(
    private val accountService: AccountServicePort
) {

    fun inviteUser(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<List<InvitationRequest>>()
            .filter { it.isNotEmpty() }
            .switchIfEmpty { Mono.error(IllegalArgumentException()) }
            .flatMapMany { Flux.fromIterable(it) }
            .flatMap { invitation ->
                inviteUser(invitation)
            }.collectList()
            .flatMap { results ->
                val failed = results.filter { it.result.not() }
                if (failed.isEmpty()) ServerResponse.ok().build()
                else ServerResponse.status(MULTI_STATUS).body(
                    BodyInserters.fromValue(failed)
                )
            }

    private fun inviteUser(invitation: InvitationRequest) =
        accountService.inviteUser(
            Email(invitation.email),
            invitation.roles.map { RoleFactory.createRole(it) }
        ).then(Mono.just(InvitationResult(invitation.email)))
            .onErrorResume {
                // TODO handle invitation exception
                Mono.just(InvitationResult(invitation.email, false))
            }

    internal data class InvitationRequest(
        val email: String,
        val roles: List<String>
    )

    internal data class InvitationResult(val email: String, val result: Boolean = true, val message: String = "")
}
