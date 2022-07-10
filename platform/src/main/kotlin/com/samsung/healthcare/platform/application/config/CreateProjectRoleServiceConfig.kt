package com.samsung.healthcare.platform.application.config

import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.platform.application.port.output.project.CreateProjectRolePort
import com.samsung.healthcare.platform.domain.Project.ProjectId
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

@Configuration
class CreateProjectRoleServiceConfig {
    @Bean
    fun createProjectRoleService(appProps: ApplicationProperties): CreateProjectRolePort =
        object : CreateProjectRolePort {
            private val client = WebClient.create(appProps.accountService.url)

            override fun createProjectRoles(account: Account, projectId: ProjectId): Mono<Void> =
                client.put()
                    .uri("/internal/account-service/roles")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(
                        mapOf(
                            "accountId" to account.id,
                            "projectId" to projectId.value.toString()
                        )
                    ).retrieve()
                    .bodyToMono()
        }
}
