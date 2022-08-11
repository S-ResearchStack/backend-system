package com.samsung.healthcare.platform.adapter.web.context

import com.google.firebase.auth.FirebaseToken
import com.samsung.healthcare.platform.application.config.Constants.DEFAULT_TENANT_NAME
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.cast
import reactor.util.context.Context

object ContextHolder {
    private const val PROJECT_ID = "PROJECT_ID"

    fun setProjectId(id: String) =
        Context.of(PROJECT_ID, Mono.just(id))

    fun getProjectId(): Mono<String> =
        Mono.deferContextual {
            it.getOrEmpty<Mono<String>>(PROJECT_ID)
                .orElseGet { Mono.just(DEFAULT_TENANT_NAME) }
        }

    suspend fun getFirebaseToken(): FirebaseToken =
        ReactiveSecurityContextHolder.getContext()
            .map { context -> context.authentication.principal }
            .cast<FirebaseToken>()
            .awaitSingle()
}
