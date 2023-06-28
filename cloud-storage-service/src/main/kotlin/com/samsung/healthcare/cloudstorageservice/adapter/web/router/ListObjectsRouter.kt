package com.samsung.healthcare.cloudstorageservice.adapter.web.router

import com.samsung.healthcare.cloudstorageservice.adapter.web.filter.JwtAuthenticationFilterFunction
import com.samsung.healthcare.cloudstorageservice.adapter.web.handler.ListObjectsHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class ListObjectsRouter {
    @Bean
    fun routeListObjects(
        handler: ListObjectsHandler,
        jwtAuthenticationFilterFunction: JwtAuthenticationFilterFunction,
    ): RouterFunction<ServerResponse> =
        RouterFunctions.route()
            .GET(
                LIST_OBJECTS_PATH,
                handler::listObjects,
            )
            .filter(jwtAuthenticationFilterFunction)
            .build()
}
