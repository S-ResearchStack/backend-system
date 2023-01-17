package com.samsung.healthcare.account.adapter.web.router

import com.samsung.healthcare.account.adapter.web.handler.VerifyEmailHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class VerifyEmailRouter {
    @Bean
    fun routeVerifyEmail(
        handler: VerifyEmailHandler,
    ): RouterFunction<ServerResponse> =
        RouterFunctions.route()
            .POST(
                VERIFY_EMAIL_PATH,
                RequestPredicates.contentType(MediaType.APPLICATION_JSON),
                handler::verifyEmail,
            )
            .POST(
                RESEND_VERIFICATION_EMAIL_PATH,
                RequestPredicates.contentType(MediaType.APPLICATION_JSON),
                handler::resendVerificationEmail,
            )
            .build()
}
