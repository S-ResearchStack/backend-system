package com.samsung.healthcare.platform.adapter.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class UserRouter(
    private val handler: UserHandler,
) {
    @Bean("routeUser")
    fun router(): RouterFunction<ServerResponse> = coRouter {
        "/api/users".nest {
            GET("", handler::getUsers)
        }
    }
}
