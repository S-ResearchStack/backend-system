package com.samsung.healthcare.platform.adapter.web.project

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
class InLabVisitRouter(
    private val handler: InLabVisitHandler,
) {
    @Bean("routeInLabVisit")
    fun router(
        tenantHandlerFilterFunction: TenantHandlerFilterFunction,
        jwtAuthenticationFilterFunction: JwtAuthenticationFilterFunction,
    ): RouterFunction<ServerResponse> =
        coRouter {
            "/api/projects/{projectId}/in-lab-visits".nest {
                POST(Strings.EMPTY, contentType(MediaType.APPLICATION_JSON), handler::createInLabVisit)
                PATCH("{inLabVisitId}", contentType(MediaType.APPLICATION_JSON), handler::updateInLabVisit)
                GET(Strings.EMPTY, handler::listInLabVisits)
                GET("{inLabVisitId}", handler::getInLabVisit)
            }
        }
            .filter(tenantHandlerFilterFunction)
            .filter(jwtAuthenticationFilterFunction)
}
