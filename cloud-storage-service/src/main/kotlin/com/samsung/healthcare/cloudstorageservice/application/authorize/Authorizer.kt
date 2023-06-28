package com.samsung.healthcare.cloudstorageservice.application.authorize

import com.samsung.healthcare.account.application.context.ContextHolder
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.cloudstorageservice.application.exception.ForbiddenException
import org.springframework.security.core.GrantedAuthority
import reactor.core.publisher.Mono

object Authorizer {
    fun getAccount(authority: GrantedAuthority): Mono<Account> =
        ContextHolder.getAccount()
            .filter {
                it.hasPermission(authority)
            }
            .switchIfEmpty(
                Mono.error(ForbiddenException("You don't have permission. (${authority.authority})"))
            )
}
