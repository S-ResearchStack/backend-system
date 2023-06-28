package com.samsung.healthcare.cloudstorageservice.adapter.web.filter

import com.samsung.healthcare.cloudstorageservice.adapter.web.common.getProjectId
import com.samsung.healthcare.cloudstorageservice.application.exception.UnauthorizedException
import com.samsung.healthcare.cloudstorageservice.application.port.output.ExistsUserPort
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.server.HandlerFilterFunction
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class IdTokenFilterFunction(
    private val existsUserPort: ExistsUserPort,
) : HandlerFilterFunction<ServerResponse, ServerResponse> {
    override fun filter(request: ServerRequest, next: HandlerFunction<ServerResponse>): Mono<ServerResponse> {
        val idToken: String? = request.headers().firstHeader("id-token")

        if (!StringUtils.hasText(idToken)) throw UnauthorizedException("You must provide id-token")

        return existsUserPort.exists(idToken!!, request.getProjectId())
            .onErrorMap { ex ->
                ex.printStackTrace()
                when (ex) {
                    is WebClientResponseException ->
                        UnauthorizedException("Please use proper authorization: ${ex.message}")
                    else ->
                        ex
                }
            }
            .then(next.handle(request))
    }
}
