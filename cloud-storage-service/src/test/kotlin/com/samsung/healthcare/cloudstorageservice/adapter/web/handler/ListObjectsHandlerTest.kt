package com.samsung.healthcare.cloudstorageservice.adapter.web.handler

import com.ninjasquad.springmockk.MockkBean
import com.samsung.healthcare.account.application.port.input.GetAccountUseCase
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.cloudstorageservice.NEGATIVE_TEST
import com.samsung.healthcare.cloudstorageservice.POSITIVE_TEST
import com.samsung.healthcare.cloudstorageservice.adapter.web.config.SecurityConfig
import com.samsung.healthcare.cloudstorageservice.adapter.web.exception.GlobalErrorAttributes
import com.samsung.healthcare.cloudstorageservice.adapter.web.exception.GlobalExceptionHandler
import com.samsung.healthcare.cloudstorageservice.adapter.web.filter.JwtAuthenticationFilterFunction
import com.samsung.healthcare.cloudstorageservice.adapter.web.filter.ProjectIdFilterFunction
import com.samsung.healthcare.cloudstorageservice.adapter.web.router.LIST_OBJECTS_PATH
import com.samsung.healthcare.cloudstorageservice.adapter.web.router.ListObjectsRouter
import com.samsung.healthcare.cloudstorageservice.application.port.input.ListObjectsUseCase
import com.samsung.healthcare.cloudstorageservice.domain.ObjectInfo
import io.mockk.every
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

@WebFluxTest
@Import(
    ListObjectsHandler::class,
    ListObjectsRouter::class,
    GlobalExceptionHandler::class,
    GlobalErrorAttributes::class,
    JwtAuthenticationFilterFunction::class,
    ProjectIdFilterFunction::class,
    SecurityConfig::class,
)
internal class ListObjectsHandlerTest {
    @MockkBean
    private lateinit var listObjectsUseCase: ListObjectsUseCase

    @MockkBean
    private lateinit var getAccountService: GetAccountUseCase

    @Autowired
    private lateinit var webClient: WebTestClient

    private val jwt =
        "eyJhb...6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkw...MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"

    val account = Account(
        "account-id",
        Email("cubist@test.com"),
        emptyList(),
    )

    @BeforeEach
    fun beforeEach() {
        every { getAccountService.getAccountFromToken(jwt) } returns Mono.just(account)
        webClient = webClient.mutate()
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
            .build()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `listObjects should work properly`() {
        val path = "test-path"
        every { listObjectsUseCase.listObjects("1", path) } returns
            Mono.just(listOf<ObjectInfo>())

        val result = webClient.get()
            .uri("$LIST_OBJECTS_PATH?path=$path".replace("{projectId}", "1"))
            .exchange()
            .expectBody()
            .returnResult()

        Assertions.assertThat(result.status).isEqualTo(HttpStatus.OK)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `listObjects throws BadRequestException when the path was not set`() {
        val result = webClient.get()
            .uri(LIST_OBJECTS_PATH.replace("{projectId}", "1"))
            .exchange()
            .expectBody()
            .returnResult()

        Assertions.assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
        Assertions.assertThat(String(result.responseBody!!)).contains("no path")
    }
}
