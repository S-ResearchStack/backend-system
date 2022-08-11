package com.samsung.healthcare.platform.adapter.web

import com.samsung.healthcare.platform.adapter.web.filter.IdTokenFilterFunction
import com.samsung.healthcare.platform.adapter.web.filter.TenantHandlerFilterFunction
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class HealthDataRouter(
    private val handler: HealthDataHandler,
) {
    @Bean("routeHealthData")
    fun router(
        idTokenFilterFunction: IdTokenFilterFunction,
        tenantHandlerFilterFunction: TenantHandlerFilterFunction
    ): RouterFunction<ServerResponse> = coRouter {
        "/api/projects/{projectId}/health-data".nest {
            POST("", contentType(MediaType.APPLICATION_JSON), handler::createHealthData)
            GET("", handler::findByPeriod)
        }
    }
        .filter(idTokenFilterFunction)
        .filter(tenantHandlerFilterFunction)
}
