package com.samsung.healthcare.platform.adapter.web.project.task

import com.samsung.healthcare.platform.adapter.web.filter.IdTokenFilterFunction
import com.samsung.healthcare.platform.adapter.web.filter.JwtAuthenticationFilterFunction
import com.samsung.healthcare.platform.adapter.web.filter.TenantHandlerFilterFunction
import org.apache.logging.log4j.util.Strings
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class TaskRouter(
    private val handler: TaskHandler,
) {
    @Bean("routeTask")
    fun router(
        tenantHandlerFilterFunction: TenantHandlerFilterFunction,
        idTokenFilterFunction: IdTokenFilterFunction,
        jwtAuthenticationFilterFunction: JwtAuthenticationFilterFunction,
    ): RouterFunction<ServerResponse> = coRouter {
        "/api/projects/{projectId}/tasks".nest {
            GET(Strings.EMPTY, handler::findByPeriod)
            GET("{taskId}", handler::findById)
            POST(Strings.EMPTY, handler::createTask)
            PATCH("{taskId}", contentType(MediaType.APPLICATION_JSON), handler::updateTask)
        }
    }
        .filter(tenantHandlerFilterFunction)
        .filter { request, next ->
            if (request.headers().firstHeader("id-token") != null) {
                idTokenFilterFunction.filter(request, next)
            } else {
                jwtAuthenticationFilterFunction.filter(request, next)
            }
        }
}
