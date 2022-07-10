package com.samsung.healthcare.platform.logger

import com.samsung.healthcare.platform.logger.decorator.LoggingWebExchange
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.util.UUID

@Component
@Profile("local | dev")
class LoggingWebFilter : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return chain.filter(
            LoggingWebExchange(
                exchange,
                generateRequestId()
            )
        )
    }

    private fun generateRequestId(): String =
        UUID.randomUUID().toString().substring(0, 8)
}
