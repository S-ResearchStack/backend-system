package com.samsung.healthcare.account.adapter.web.router

import com.samsung.healthcare.account.adapter.web.handler.AssignRoleHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class AssignRoleRouter {
    @Bean
    fun routeRoleAssign(
        handler: AssignRoleHandler
    ): RouterFunction<ServerResponse> =
        RouterFunctions.route()
            .PUT(
                ASSIGN_ROLE_PATH,
                RequestPredicates.contentType(MediaType.APPLICATION_JSON),
                handler::assignRoles
            )
            .build()
}
