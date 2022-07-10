package com.samsung.healthcare.platform.adapter.web

import com.samsung.healthcare.platform.adapter.web.filter.JwtAuthenticationFilterFunction
import org.apache.logging.log4j.util.Strings
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class ProjectRouter(
    private val handler: ProjectHandler,
) {
    @Bean("routeProject")
    fun router(jwtAuthenticationFilterFunction: JwtAuthenticationFilterFunction): RouterFunction<ServerResponse> =
        coRouter {
            "/api/projects".nest {
                POST(Strings.EMPTY, contentType(MediaType.APPLICATION_JSON), handler::createProject)
                GET(Strings.EMPTY, handler::listProjects)
                GET("{projectId}", handler::findProjectById)
            }
        }.filter(jwtAuthenticationFilterFunction)
}
