package com.samsung.healthcare.account.adapter.web.router

import com.samsung.healthcare.account.adapter.web.handler.SignInHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class SignInRouter {

    @Bean
    fun routeSignIn(
        handler: SignInHandler
    ): RouterFunction<ServerResponse> =
        RouterFunctions.route()
            .POST(
                SIGN_IN_PATH,
                RequestPredicates.contentType(MediaType.APPLICATION_JSON),
                handler::signIn
            )
            .build()
}
