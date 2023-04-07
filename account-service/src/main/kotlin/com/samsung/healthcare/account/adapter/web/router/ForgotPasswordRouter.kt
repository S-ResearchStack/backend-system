package com.samsung.healthcare.account.adapter.web.router

import com.samsung.healthcare.account.adapter.web.handler.ForgotPasswordHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class ForgotPasswordRouter {
    @Bean
    fun routeForgotPassword(
        handler: ForgotPasswordHandler,
    ): RouterFunction<ServerResponse> =
        RouterFunctions.route()
            .POST(
                FORGOT_PASSWORD_PATH,
                RequestPredicates.contentType(MediaType.APPLICATION_JSON),
                handler::forgotPassword,
            )
            .build()
}
