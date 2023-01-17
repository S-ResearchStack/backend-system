package com.samsung.healthcare.account.adapter.web.router

import com.samsung.healthcare.account.adapter.web.handler.TokenRefreshHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class TokenRefreshRouter {

    @Bean
    fun routeTokenRefresh(
        handler: TokenRefreshHandler
    ): RouterFunction<ServerResponse> =
        route()
            .POST(
                REFRESH_TOKEN_PATH,
                RequestPredicates.contentType(MediaType.APPLICATION_JSON),
                handler::refreshToken
            )
            .build()
}
