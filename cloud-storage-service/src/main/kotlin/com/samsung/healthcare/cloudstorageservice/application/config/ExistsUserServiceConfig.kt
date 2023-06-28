package com.samsung.healthcare.cloudstorageservice.application.config

import com.samsung.healthcare.cloudstorageservice.application.port.output.ExistsUserPort
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

@Configuration
class ExistsUserServiceConfig {
    @Bean
    fun existsUserService(platformProperties: PlatformProperties): ExistsUserPort =
        object : ExistsUserPort {
            private val client = WebClient.create(platformProperties.url)

            override fun exists(idToken: String, projectId: String): Mono<Void> {
                return client.get()
                    .uri("/internal/api/projects/$projectId/users/exists")
                    .header("id-token", idToken)
                    .retrieve()
                    .bodyToMono()
            }
        }
}
