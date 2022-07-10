package com.samsung.healthcare.account.adapter.web.router

import com.samsung.healthcare.account.adapter.web.handler.ResetAccountHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class ResetAccountRouter {

    @Bean
    fun routeResetPassword(
        handler: ResetAccountHandler
    ): RouterFunction<ServerResponse> =
        RouterFunctions.route()
            .POST(
                RESET_PASSWORD_PATH,
                RequestPredicates.contentType(MediaType.APPLICATION_JSON),
                handler::resetAccount
            )
            .build()
}
