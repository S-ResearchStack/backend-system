package com.samsung.healthcare.cloudstorageservice.adapter.web.router

import com.samsung.healthcare.cloudstorageservice.adapter.web.filter.JwtAuthenticationFilterFunction
import com.samsung.healthcare.cloudstorageservice.adapter.web.filter.ProjectIdFilterFunction
import com.samsung.healthcare.cloudstorageservice.adapter.web.handler.DownloadObjectHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class DownloadObjectRouter {
    @Bean
    fun routeDownloadObject(
        handler: DownloadObjectHandler,
        jwtAuthenticationFilterFunction: JwtAuthenticationFilterFunction,
        projectIdFilterFunction: ProjectIdFilterFunction,
    ): RouterFunction<ServerResponse> =
        RouterFunctions.route()
            .GET(
                DOWNLOAD_OBJECT_PATH,
                handler::download,
            )
            .GET(
                DOWNLOAD_OBJECT_URL_PATH,
                handler::getDownloadUrl,
            )
            .filter(projectIdFilterFunction)
            .filter(jwtAuthenticationFilterFunction)
            .build()
}
