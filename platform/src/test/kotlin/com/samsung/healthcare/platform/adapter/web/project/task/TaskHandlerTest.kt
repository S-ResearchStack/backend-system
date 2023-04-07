package com.samsung.healthcare.platform.adapter.web.project.task

import com.google.firebase.ErrorCode
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.ninjasquad.springmockk.MockkBean
import com.samsung.healthcare.account.application.port.input.GetAccountUseCase
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.account.domain.Role
import com.samsung.healthcare.platform.NEGATIVE_TEST
import com.samsung.healthcare.platform.POSITIVE_TEST
import com.samsung.healthcare.platform.adapter.web.context.ContextHolder
import com.samsung.healthcare.platform.adapter.web.exception.ErrorResponse
import com.samsung.healthcare.platform.adapter.web.exception.ExceptionHandler
import com.samsung.healthcare.platform.adapter.web.filter.IdTokenFilterFunction
import com.samsung.healthcare.platform.adapter.web.filter.JwtAuthenticationFilterFunction
import com.samsung.healthcare.platform.adapter.web.filter.TenantHandlerFilterFunction
import com.samsung.healthcare.platform.adapter.web.security.SecurityConfig
import com.samsung.healthcare.platform.application.authorize.Authorizer
import com.samsung.healthcare.platform.application.exception.BadRequestException
import com.samsung.healthcare.platform.application.exception.GlobalErrorAttributes
import com.samsung.healthcare.platform.application.port.input.GetProjectQuery
import com.samsung.healthcare.platform.application.port.input.project.ExistUserProfileUseCase
import com.samsung.healthcare.platform.application.port.input.project.task.CreateTaskCommand
import com.samsung.healthcare.platform.application.port.input.project.task.CreateTaskResponse
import com.samsung.healthcare.platform.application.port.input.project.task.CreateTaskUseCase
import com.samsung.healthcare.platform.application.port.input.project.task.GetTaskCommand
import com.samsung.healthcare.platform.application.port.input.project.task.GetTaskUseCase
import com.samsung.healthcare.platform.application.port.input.project.task.UpdateTaskCommand
import com.samsung.healthcare.platform.application.port.input.project.task.UpdateTaskUseCase
import com.samsung.healthcare.platform.domain.Project
import com.samsung.healthcare.platform.domain.project.UserProfile
import com.samsung.healthcare.platform.enums.TaskStatus
import com.samsung.healthcare.platform.enums.TaskType
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import kotlinx.coroutines.flow.flowOf
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@WebFluxTest
@Import(
    TaskHandler::class,
    TaskRouter::class,
    TenantHandlerFilterFunction::class,
    IdTokenFilterFunction::class,
    JwtAuthenticationFilterFunction::class,
    SecurityConfig::class,
    ExceptionHandler::class,
    GlobalErrorAttributes::class,
)
internal class TaskHandlerTest {
    @MockkBean
    private lateinit var getAccountUseCase: GetAccountUseCase

    @MockkBean
    private lateinit var getTaskUseCase: GetTaskUseCase

    @MockkBean
    private lateinit var getProjectQuery: GetProjectQuery

    @MockkBean
    private lateinit var createTaskUseCase: CreateTaskUseCase

    @MockkBean
    private lateinit var updateTaskUseCase: UpdateTaskUseCase

    @MockkBean
    private lateinit var existUserProfileUseCase: ExistUserProfileUseCase

    @Autowired
    private lateinit var webTestClient: WebTestClient

    private val projectId = Project.ProjectId.from(1)
    private val jwt = ""
    val account = Account(
        "account-id",
        Email("cubist@test.com"),
        listOf(Role.ProjectRole.Researcher(projectId.value.toString()))
    )

    @BeforeEach
    fun setup() {
        coEvery {
            getProjectQuery.existsProject(any())
        } returns true
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

        val result = webTestClient.get()
            .uri("/api/projects/1/tasks?end_time=2022-10-21T00:00&status=DRAFT")
            .header("id-token", "test-token")
            .exchange()
            .expectBody(ErrorResponse::class.java)
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.UNAUTHORIZED)
        assertThat(result.responseBody?.message).isEqualTo("Please use proper authorization: Test invalid token")
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should throw forbidden if unregistered user`() {
        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance().verifyIdToken(any()) } returns mockk(relaxed = true)

        mockkObject(ContextHolder)
        coEvery { ContextHolder.getFirebaseToken().uid } returns "testUID"
        coEvery { existUserProfileUseCase.existsByUserId(UserProfile.UserId.from("testUID")) } returns false

        val result = webTestClient.get()
            .uri("/api/projects/1/tasks?end_time=2022-10-21T00:00&status=DRAFT")
            .header("id-token", "test-token")
            .exchange()
            .expectBody(ErrorResponse::class.java)
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.FORBIDDEN)
        assertThat(result.responseBody?.message).isEqualTo("This user(testUID) is not registered on this project")
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should return relevant tasks as body`() {
        mockkObject(Authorizer)
        every { getAccountUseCase.getAccountFromToken(jwt) } returns Mono.just(account)

        val testLocalDateTime = LocalDateTime.parse("2022-10-21T00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val getTaskCommand = GetTaskCommand(
            null,
            testLocalDateTime,
            null,
            "DRAFT",
            "SURVEY"
        )
        val testMap1 = mapOf("test1" to "shouldBeTask")
        val testMap2 = mapOf("test2" to "shouldAlsoBeTask")
        coEvery {
            getTaskUseCase.findByPeriodFromResearcher(projectId.toString(), getTaskCommand)
        } returns flowOf(testMap1, testMap2)

        val result = webTestClient.get()
            .uri("/api/projects/1/tasks?end_time=2022-10-21T00:00&status=DRAFT&type=SURVEY")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
            .exchange()
            .expectBodyList(Map::class.java)
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.OK)
        assertThat(result.responseBody?.size).isEqualTo(2)
        assertThat(result.responseBody).contains(testMap1, testMap2)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should return matching task as body`() {
        mockkObject(Authorizer)
        every { getAccountUseCase.getAccountFromToken(jwt) } returns Mono.just(account)

        val testMap = mapOf("testTask" to "task with id test")
        val taskId = "SAMPLE-TASK-ID"
        coEvery { getTaskUseCase.findById(projectId.toString(), taskId) } returns flowOf(testMap)

        val result = webTestClient.get()
            .uri("/api/projects/$projectId/tasks/$taskId")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
            .exchange()
            .expectBodyList(Map::class.java)
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.OK)
        assertThat(result.responseBody?.size).isEqualTo(1)
        assertThat(result.responseBody).contains(testMap)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should throw unauthorized when no token provided`() {
        val result = webTestClient.post()
            .uri("/api/projects/$projectId/tasks")
            .exchange()
            .expectBody(ErrorResponse::class.java)
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.UNAUTHORIZED)
        assertThat(result.responseBody?.message).isEqualTo("unauthorized")
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should return created when task created`() {
        mockkObject(Authorizer)
        every { getAccountUseCase.getAccountFromToken(jwt) } returns Mono.just(account)

        val createTaskResponse = CreateTaskResponse(1, "testCreate")
        val createTaskCommand = CreateTaskCommand(TaskType.SURVEY)
        coEvery { createTaskUseCase.createTask(projectId.toString(), createTaskCommand) } returns createTaskResponse

        val result = webTestClient.post()
            .uri("/api/projects/$projectId/tasks")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
            .bodyValue(createTaskCommand)
            .exchange()
            .expectBody(CreateTaskResponse::class.java)
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.CREATED)
        assertThat(result.responseBody).isEqualTo(createTaskResponse)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should return no content for update`() {
        mockkObject(Authorizer)
        every { getAccountUseCase.getAccountFromToken(jwt) } returns Mono.just(account)

        val updateTaskCommand = UpdateTaskCommand(
            title = "testUpdate",
            status = TaskStatus.DRAFT,
            type = TaskType.SURVEY,
            items = emptyList()
        )
        coJustRun {
            updateTaskUseCase.updateTask(projectId.toString(), "test", match { it.value == 1 }, updateTaskCommand)
        }

        val result = webTestClient.patch()
            .uri("/api/projects/$projectId/tasks/test?revision_id=1")
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
            .body(BodyInserters.fromValue(updateTaskCommand))
            .exchange()
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.NO_CONTENT)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `findByPeriod request should return bad request when endTime is earlier than startTime`() {
        mockkObject(Authorizer)
        every { getAccountUseCase.getAccountFromToken(jwt) } returns Mono.just(account)
        val result = getTaskWithParams("start_time=2022-10-21T00:00&end_time=2022-10-20T00:00")
        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `findByPeriod request should return bad request when status is not invalid`() {
        mockkObject(Authorizer)
        every { getAccountUseCase.getAccountFromToken(jwt) } returns Mono.just(account)
        coEvery {
            getTaskUseCase.findByPeriodFromResearcher(projectId.toString(), any())
        } throws BadRequestException()

        val result = getTaskWithParams("status=invalid-status")
        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `updateTask request should return bad request when revisionId is given`() {
        mockkObject(Authorizer)
        every { getAccountUseCase.getAccountFromToken(jwt) } returns Mono.just(account)
        val result = webTestClient.patch()
            .uri("/api/projects/$projectId/tasks/test?revision_id=")
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
            .exchange()
            .expectBody()
            .returnResult()
        assertThat(result.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    private fun getTaskWithParams(param: String) = webTestClient.get()
        .uri("/api/projects/$projectId/tasks?$param")
        .header(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
        .exchange()
        .expectBody()
        .returnResult()
}
