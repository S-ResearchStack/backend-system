package com.samsung.healthcare.dataqueryservice.adapter.web

import com.ninjasquad.springmockk.MockkBean
import com.samsung.healthcare.account.application.port.input.GetAccountUseCase
import com.samsung.healthcare.account.domain.Role.ProjectRole.PrincipalInvestigator
import com.samsung.healthcare.dataqueryservice.NEGATIVE_TEST
import com.samsung.healthcare.dataqueryservice.POSITIVE_TEST
import com.samsung.healthcare.dataqueryservice.adapter.web.exception.GlobalExceptionHandler
import com.samsung.healthcare.dataqueryservice.adapter.web.interceptor.JwtAuthenticationInterceptor
import com.samsung.healthcare.dataqueryservice.application.port.input.QueryDataResultSet
import com.samsung.healthcare.dataqueryservice.application.service.QueryDataService
import io.mockk.every
import io.trino.sql.parser.ParsingException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import java.sql.SQLDataException
import java.sql.SQLInvalidAuthorizationSpecException
import java.sql.SQLSyntaxErrorException
import java.time.Instant

@ExtendWith(SpringExtension::class)
@WebMvcTest(DataQueryController::class)
@AutoConfigureMockMvc(addFilters = false)
@Import(
    JwtAuthenticationInterceptor::class,
    GlobalExceptionHandler::class
)
internal class DataQueryControllerTest {
    @MockkBean
    private lateinit var queryDataService: QueryDataService

    @MockkBean
    private lateinit var getAccountService: GetAccountUseCase

    @MockkBean
    private lateinit var jwtDecoder: JwtDecoder

    @Autowired
    private lateinit var webClient: WebTestClient

    private val testRequest = TestRequest("SELECT * from tables")

    private val testJwt = Jwt(
        "token",
        Instant.now(),
        Instant.now().plusSeconds(86_400),
        mapOf(Pair("alg", "RS256")),
        mapOf(
            Pair("sub", "random-uuid"),
            Pair("email", "test@research-hub.test.com"),
            Pair("roles", listOf(PrincipalInvestigator("1").roleName))
        )
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `controller should work properly`() {
        every { queryDataService.execute(any(), any(), any()) } returns QueryDataResultSet(
            QueryDataResultSet.MetaData(
                emptyList(),
                0
            ),
            emptyList()
        )

        every { jwtDecoder.decode(any()) } returns testJwt

        val result = webClient.post().uri("/api/projects/project-id/sql")
            .contentType(MediaType.APPLICATION_JSON)
            .header(AUTHORIZATION, "Bearer some_token")
            .body(BodyInserters.fromValue(testRequest))
            .exchange()
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.OK)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `controller should throw UnauthorizedException when authorization is failed`() {
        val result = webClient.post().uri("/api/projects/project-id/sql")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(testRequest))
            .exchange()
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `controller should throw BadJwtException when JWT is invalid`() {

        every { jwtDecoder.decode(any()) } returns Jwt(
            "token",
            Instant.now(),
            Instant.now().plusSeconds(86_400),
            mapOf(Pair("alg", "RS256")),
            mapOf(
                Pair("subject", "random-uuid"),
                Pair("email", "test@research-hub.test.com"),
                Pair("roles", listOf(PrincipalInvestigator("1").roleName))
            )
        )

        val result = webClient.post().uri("/api/projects/project-id/sql")
            .contentType(MediaType.APPLICATION_JSON)
            .header(AUTHORIZATION, "Bearer some_token")
            .body(BodyInserters.fromValue(testRequest))
            .exchange()
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `controller should throw SQLInvalidAuthorizationSpecException when user has no permission for project`() {
        every { queryDataService.execute(any(), any(), any()) } throws SQLInvalidAuthorizationSpecException(
            "invalid-sql",
            "state-code"
        )
        every { jwtDecoder.decode(any()) } returns testJwt

        val result = webClient.post().uri("/api/projects/not-allowed/sql")
            .contentType(MediaType.APPLICATION_JSON)
            .header(AUTHORIZATION, "Bearer some_token")
            .body(BodyInserters.fromValue(testRequest))
            .exchange()
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `controller should throw SQLSyntaxErrorException when sql is invalid`() {
        every { queryDataService.execute(any(), any(), any()) } throws SQLSyntaxErrorException(
            "invalid-sql",
            "state-code"
        )
        every { jwtDecoder.decode(any()) } returns testJwt

        val result = webClient.post().uri("/api/projects/project-id/sql")
            .contentType(MediaType.APPLICATION_JSON)
            .header(AUTHORIZATION, "Bearer some_token")
            .body(BodyInserters.fromValue(TestRequest("select insert")))
            .exchange()
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `controller should throw SQLDataException when invalid data access attempted`() {
        every { queryDataService.execute(any(), any(), any()) } throws SQLDataException(
            "invalid-sql",
            "state-code"
        )
        every { jwtDecoder.decode(any()) } returns testJwt

        val result = webClient.post().uri("/api/projects/project-id/sql")
            .contentType(MediaType.APPLICATION_JSON)
            .header(AUTHORIZATION, "Bearer some_token")
            .body(BodyInserters.fromValue(TestRequest("select insert")))
            .exchange()
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `controller should return bad request when sql is not valid`() {
        every { queryDataService.execute(any(), any(), any()) } throws ParsingException(
            "invalid-sql"
        )
        every { jwtDecoder.decode(any()) } returns testJwt

        val result = webClient.post().uri("/api/projects/project-id/sql")
            .contentType(MediaType.APPLICATION_JSON)
            .header(AUTHORIZATION, "Bearer some_token")
            .body(BodyInserters.fromValue(TestRequest("select insert")))
            .exchange()
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `controller should return bad request when sql is blank`() {
        every { jwtDecoder.decode(any()) } returns testJwt

        val result = webClient.post().uri("/api/projects/project-id/sql")
            .contentType(MediaType.APPLICATION_JSON)
            .header(AUTHORIZATION, "Bearer some_token")
            .body(BodyInserters.fromValue(TestRequest("   ")))
            .exchange()
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `controller should return bad request when sql is not given`() {
        every { jwtDecoder.decode(any()) } returns testJwt

        val result = webClient.post().uri("/api/projects/project-id/sql")
            .contentType(MediaType.APPLICATION_JSON)
            .header(AUTHORIZATION, "Bearer some_token")
            .exchange()
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `controller should return bad request when sql query is not given`() {
        every { jwtDecoder.decode(any()) } returns testJwt

        val result = postRequestWithBody("""{"sql":}""")

        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `controller should return bad request when sql query is not valid`() {
        every { jwtDecoder.decode(any()) } returns testJwt

        val result = postRequestWithBody(
            """{
            "sql": "select * from item_results" "aaaaa"
            }"""
        )
        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `controller should return bad request when sql query is array type`() {
        every { jwtDecoder.decode(any()) } returns testJwt

        val result = postRequestWithBody(
            """{
            "sql":  ["Invalid_Array"]
            }"""
        )
        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `controller should return bad request when sql query is object type`() {
        every { jwtDecoder.decode(any()) } returns testJwt

        val result = postRequestWithBody(
            """{
            "sql":  { "Invalid_Object": "Invalid_String" }
            }"""
        )
        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `controller should return bad request when sql key is not given`() {
        every { jwtDecoder.decode(any()) } returns testJwt

        val result = postRequestWithBody(
            """{
            "":  "select * from item_results"
            }"""
        )
        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    private fun postRequestWithBody(obj: Any) = webClient.post().uri("/api/projects/project-id/sql")
        .contentType(MediaType.APPLICATION_JSON)
        .header(AUTHORIZATION, "Bearer some_token")
        .body(
            BodyInserters.fromValue(obj)
        )
        .exchange()
        .expectBody()
        .returnResult()

    private data class TestRequest(val sql: String)
}

internal fun WebTestClient.post(path: String, param: Any) =
    this.post().uri(path).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(param)).exchange()
