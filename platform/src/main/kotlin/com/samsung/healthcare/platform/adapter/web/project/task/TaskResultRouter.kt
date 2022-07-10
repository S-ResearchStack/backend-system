package com.samsung.healthcare.platform.adapter.web.project.task

import com.samsung.healthcare.platform.adapter.web.filter.IdTokenFilterFunction
import com.samsung.healthcare.platform.adapter.web.filter.TenantHandlerFilterFunction
import org.apache.logging.log4j.util.Strings
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class TaskResultRouter(
    private val handler: TaskResultHandler
) {
    @Bean("routeTaskResult")
    fun router(
        tenantHandlerFilterFunction: TenantHandlerFilterFunction,
        idTokenFilterFunction: IdTokenFilterFunction,
    ): RouterFunction<ServerResponse> = coRouter {
        "/api/projects/{projectId}/tasks".nest {
            PATCH(Strings.EMPTY, handler::uploadTaskResults)
        }
    }
        .filter(idTokenFilterFunction)
        .filter(tenantHandlerFilterFunction)
}
