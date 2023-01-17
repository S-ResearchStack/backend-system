package com.samsung.healthcare.platform.adapter.web.project

import com.google.firebase.ErrorCode
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.ninjasquad.springmockk.MockkBean
import com.samsung.healthcare.platform.NEGATIVE_TEST
import com.samsung.healthcare.platform.POSITIVE_TEST
import com.samsung.healthcare.platform.adapter.web.exception.ErrorResponse
import com.samsung.healthcare.platform.adapter.web.exception.ExceptionHandler
import com.samsung.healthcare.platform.adapter.web.filter.IdTokenFilterFunction
import com.samsung.healthcare.platform.adapter.web.filter.TenantHandlerFilterFunction
import com.samsung.healthcare.platform.adapter.web.security.SecurityConfig
import com.samsung.healthcare.platform.application.exception.GlobalErrorAttributes
import com.samsung.healthcare.platform.application.port.input.CreateUserCommand
import com.samsung.healthcare.platform.application.port.input.project.UserProfileInputPort
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

@WebFluxTest
@Import(
    UserProfileHandler::class,
    UserProfileRouter::class,
    IdTokenFilterFunction::class,
    TenantHandlerFilterFunction::class,
    SecurityConfig::class,
    ExceptionHandler::class,
    GlobalErrorAttributes::class
)
internal class UserProfileHandlerTest {
    @MockkBean
    private lateinit var userProfileInputPort: UserProfileInputPort

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Test
    @Tag(POSITIVE_TEST)
    fun `should return ok`() {
        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance().verifyIdToken(any()) } returns mockk(relaxed = true)
        val createUserCommand = CreateUserCommand("testUID", emptyMap())
        coJustRun { userProfileInputPort.registerUser(createUserCommand) }

        val result = webTestClient.post()
            .uri("/api/projects/1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .header("id-token", "testToken")
            .body(BodyInserters.fromValue(createUserCommand))
            .exchange()
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.OK)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should throw unauthorized exception if id token is not provided`() {
        val result = webTestClient.post()
            .uri("/api/projects/1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .exchange()
            .expectBody(ErrorResponse::class.java)
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.UNAUTHORIZED)
        assertThat(result.responseBody?.message).isEqualTo("You must provide id-token")
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should throw unauthorized if token cannot be validated`() {
        mockkStatic(FirebaseAuth::class)
        every {
            FirebaseAuth.getInstance().verifyIdToken(any())
        } throws FirebaseAuthException(
            FirebaseException(ErrorCode.INVALID_ARGUMENT, "Test invalid token", Throwable())
        )

        val result = webTestClient.post()
            .uri("/api/projects/1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .header("id-token", "testToken")
            .exchange()
            .expectBody(ErrorResponse::class.java)
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.UNAUTHORIZED)
        assertThat(result.responseBody?.message).isEqualTo("Please use proper authorization: Test invalid token")
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return bad request if user id is not provided`() {
        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance().verifyIdToken(any()) } returns mockk(relaxed = true)

        val result = webTestClient.post()
            .uri("/api/projects/1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .header("id-token", "testToken")
            .body(BodyInserters.fromValue(emptyMap<String, Any>()))
            .exchange()
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }
}
