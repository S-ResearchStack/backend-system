package com.samsung.healthcare.cloudstorageservice.adapter.web.router

import com.samsung.healthcare.cloudstorageservice.adapter.web.filter.JwtAuthenticationFilterFunction
import com.samsung.healthcare.cloudstorageservice.adapter.web.handler.UploadObjectHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class UploadObjectRouter {
    @Bean
    fun routeUploadObject(
        handler: UploadObjectHandler,
        jwtAuthenticationFilterFunction: JwtAuthenticationFilterFunction,
    ): RouterFunction<ServerResponse> =
        RouterFunctions.route()
            .GET(
                UPLOAD_OBJECT_URL_PATH,
                handler::upload,
            )
            .filter(jwtAuthenticationFilterFunction)
            .build()
}
