package com.samsung.healthcare.account.adapter.web.filter

import com.samsung.healthcare.account.application.context.ContextHolder
import com.samsung.healthcare.account.application.port.input.GetAccountUseCase
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class JwtTokenAuthenticationFilter(
    private val getAccount: GetAccountUseCase
) : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val jwt = exchange.request.headers.getFirst(AUTHORIZATION)
            ?.substring("Bearer ".length) ?: return chain.filter(exchange)

        return getAccount.getAccountFromToken(jwt)
            .flatMap { account ->
                ContextHolder.setAccount(chain.filter(exchange), account)
            }
    }
}
