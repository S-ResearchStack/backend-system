package com.samsung.healthcare.account.adapter.web.router

import com.samsung.healthcare.account.adapter.web.handler.RemoveUserRolesHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class RemoveUserRolesRouter {
    @Bean
    fun routeRemoveUserRoles(
        handler: RemoveUserRolesHandler
    ): RouterFunction<ServerResponse> =
        RouterFunctions.route()
            .POST(
                REMOVE_USER_ROLE_PATH,
                RequestPredicates.contentType(MediaType.APPLICATION_JSON),
                handler::removeUserRoles
            )
            .build()
}
