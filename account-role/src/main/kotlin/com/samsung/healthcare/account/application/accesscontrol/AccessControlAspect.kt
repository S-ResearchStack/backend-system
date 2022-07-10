package com.samsung.healthcare.account.application.accesscontrol

import com.samsung.healthcare.account.application.context.ContextHolder
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.GlobalAuthority
import com.samsung.healthcare.account.domain.ProjectAuthority
import com.samsung.healthcare.account.domain.Role.ProjectRole
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.security.core.GrantedAuthority
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor

@Aspect
class AccessControlAspect {

    @Around("@annotation(requires)")
    fun withAuthorize(joinPoint: ProceedingJoinPoint, requires: Requires): Any {
        return ContextHolder.getAccount()
            .switchIfEmpty { Mono.error(IllegalAccessException()) }
            .map { account -> checkRoles(account, requires, joinPoint) }
            .onErrorMap { ex -> ex.printStackTrace(); IllegalAccessException() }
            .then(proceed(joinPoint))
    }

    private fun proceed(joinPoint: ProceedingJoinPoint): Mono<*> {
        return Mono.defer {
            joinPoint.proceed() as Mono<*>
        }
    }

    private fun checkRoles(account: Account, requires: Requires, joinPoint: ProceedingJoinPoint) {
        checkGlobalAuthorities(account, requires)
        checkProjectAuthorities(account, requires, joinPoint)
    }

    private fun checkProjectAuthorities(account: Account, requires: Requires, joinPoint: ProceedingJoinPoint) {
        val projectIds = joinPoint.args.filterIsInstance(Collection::class.java)
            .flatten()
            .filterIsInstance(ProjectRole::class.java)
            .map { it.projectId }

        val hasAllPermission = requires.authorities
            .filter { it.isSubclassOf(ProjectAuthority::class) }
            .flatMap { kClass ->
                projectIds.map {
                    kClass.primaryConstructor!!.call(it)
                }
            }.all { authority -> account.hasPermission(authority as GrantedAuthority) }

        if (!hasAllPermission) throw IllegalAccessException()
    }

    private fun checkGlobalAuthorities(account: Account, requires: Requires) {
        val hasAllPermission = requires.authorities
            .filter { it.isSubclassOf(GlobalAuthority::class) }
            .all {
                account.hasPermission(it.objectInstance as GrantedAuthority)
            }
        if (!hasAllPermission) throw IllegalAccessException()
    }
}
