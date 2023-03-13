package com.samsung.healthcare.account.adapter.web.router

import com.samsung.healthcare.account.adapter.web.filter.JwtAuthenticationFilterFunction
import com.samsung.healthcare.account.adapter.web.handler.CreateRoleHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class CreateRoleRouter {

    @Bean
    fun routeCreateRole(
        handler: CreateRoleHandler,
        jwtAuthenticationFilterFunction: JwtAuthenticationFilterFunction
    ): RouterFunction<ServerResponse> =
        RouterFunctions.route()
            .PUT(
                CREATE_ROLE_PATH,
                RequestPredicates.contentType(MediaType.APPLICATION_JSON),
                handler::createProjectRoles
            )
            .build()
}
