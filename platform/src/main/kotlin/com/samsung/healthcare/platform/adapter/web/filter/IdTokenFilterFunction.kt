package com.samsung.healthcare.platform.adapter.web.filter

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.samsung.healthcare.platform.application.exception.UnauthorizedException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.reactive.function.server.HandlerFilterFunction
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class IdTokenFilterFunction : HandlerFilterFunction<ServerResponse, ServerResponse> {
    override fun filter(request: ServerRequest, next: HandlerFunction<ServerResponse>): Mono<ServerResponse> {
        // TODO: By using user-agent, assert that it is called from the mobile application

        val idToken: String? = request.headers().firstHeader("id-token")

        if (!StringUtils.hasText(idToken)) throw UnauthorizedException("You must provide id-token")

        try {
            return next.handle(request)
                .contextWrite(
                    ReactiveSecurityContextHolder.withAuthentication(
                        UsernamePasswordAuthenticationToken(
                            FirebaseAuth.getInstance().verifyIdToken(idToken),
                            idToken
                        )
                    )
                )
        } catch (e: FirebaseAuthException) {
            throw UnauthorizedException("Please use proper authorization: ${e.message}")
        }
    }
}
