package com.samsung.healthcare.account.adapter.web.router

import com.samsung.healthcare.account.adapter.web.filter.JwtAuthenticationFilterFunction
import com.samsung.healthcare.account.adapter.web.handler.InvitationHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicates.contentType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class InvitationRouter {

    @Bean
    fun routeInvitation(
        handler: InvitationHandler,
        jwtAuthenticationFilterFunction: JwtAuthenticationFilterFunction
    ): RouterFunction<ServerResponse> =
        route()
            .POST(INVITATION_PATH, contentType(MediaType.APPLICATION_JSON), handler::inviteUser)
            .filter(jwtAuthenticationFilterFunction)
            .build()
}
