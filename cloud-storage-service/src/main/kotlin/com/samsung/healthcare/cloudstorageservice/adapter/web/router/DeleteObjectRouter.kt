package com.samsung.healthcare.cloudstorageservice.adapter.web.router

import com.samsung.healthcare.cloudstorageservice.adapter.web.filter.JwtAuthenticationFilterFunction
import com.samsung.healthcare.cloudstorageservice.adapter.web.filter.ProjectIdFilterFunction
import com.samsung.healthcare.cloudstorageservice.adapter.web.handler.DeleteObjectHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class DeleteObjectRouter {
    @Bean
    fun routeDeleteObject(
        handler: DeleteObjectHandler,
        jwtAuthenticationFilterFunction: JwtAuthenticationFilterFunction,
        projectIdFilterFunction: ProjectIdFilterFunction,
    ): RouterFunction<ServerResponse> =
        RouterFunctions.route()
            .DELETE(
                DELETE_OBJECT_PATH,
                handler::delete,
            )
            .filter(projectIdFilterFunction)
            .filter(jwtAuthenticationFilterFunction)
            .build()
}
