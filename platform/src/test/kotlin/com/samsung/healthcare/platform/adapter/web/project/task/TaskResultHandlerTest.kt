package com.samsung.healthcare.platform.adapter.web.project.task

import com.google.firebase.ErrorCode
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.ninjasquad.springmockk.MockkBean
import com.samsung.healthcare.platform.NEGATIVE_TEST
import com.samsung.healthcare.platform.POSITIVE_TEST
import com.samsung.healthcare.platform.adapter.web.context.ContextHolder
import com.samsung.healthcare.platform.adapter.web.exception.ErrorResponse
import com.samsung.healthcare.platform.adapter.web.exception.ExceptionHandler
import com.samsung.healthcare.platform.adapter.web.filter.IdTokenFilterFunction
import com.samsung.healthcare.platform.adapter.web.filter.TenantHandlerFilterFunction
import com.samsung.healthcare.platform.adapter.web.security.SecurityConfig
import com.samsung.healthcare.platform.application.exception.GlobalErrorAttributes
import com.samsung.healthcare.platform.application.port.input.GetProjectQuery
import com.samsung.healthcare.platform.application.port.input.project.ExistUserProfileUseCase
import com.samsung.healthcare.platform.application.port.input.project.UpdateUserProfileLastSyncedTimeUseCase
import com.samsung.healthcare.platform.application.port.input.project.task.UploadTaskResultCommand
import com.samsung.healthcare.platform.application.port.input.project.task.UploadTaskResultUseCase
import com.samsung.healthcare.platform.domain.project.UserProfile
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import java.time.LocalDateTime

@WebFluxTest
@Import(
    TaskResultHandler::class,
    TaskResultRouter::class,
    IdTokenFilterFunction::class,
    TenantHandlerFilterFunction::class,
    SecurityConfig::class,
    ExceptionHandler::class,
    GlobalErrorAttributes::class,
)
internal class TaskResultHandlerTest {
    @MockkBean
    private lateinit var uploadTaskResultUseCase: UploadTaskResultUseCase

    @MockkBean
    private lateinit var updateUserProfileLastSyncedTimeUseCase: UpdateUserProfileLastSyncedTimeUseCase

    @MockkBean
    private lateinit var getProjectQuery: GetProjectQuery

    @MockkBean
    private lateinit var existUserProfileUseCase: ExistUserProfileUseCase

    @Autowired
    private lateinit var webTestClient: WebTestClient

    private val projectId = 1

    @BeforeEach
    fun setup() {
        coEvery {
            getProjectQuery.existsProject(any())
        } returns true
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should return ok`() {
        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance().verifyIdToken(any()) } returns mockk(relaxed = true)
        val taskResult1 = UploadTaskResultCommand(
            1,
            "testTask",
            "user",
            LocalDateTime.now().minusHours(5),
            LocalDateTime.now(),
            emptyList()
        )
        val taskResult2 = UploadTaskResultCommand(
            1,
            "testTask",
            "user",
            LocalDateTime.now().minusHours(6),
            LocalDateTime.now().minusHours(2),
            emptyList()
        )
        val uploadCommandList = listOf(taskResult1, taskResult2)
        coJustRun { updateUserProfileLastSyncedTimeUseCase.updateLastSyncedTime(any()) }
        coJustRun { uploadTaskResultUseCase.uploadResults(uploadCommandList) }
        coEvery { existUserProfileUseCase.existsByUserId(any()) } returns true

        val result = webTestClient.patch()
            .uri("/api/projects/$projectId/tasks")
            .header("id-token", "testToken")
            .body(BodyInserters.fromValue(uploadCommandList))
            .exchange()
            .expectBody()
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.CREATED)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should throw unauthorized exception if id token is not provided`() {
        val result = webTestClient.patch()
            .uri("/api/projects/$projectId/tasks")
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

        val result = webTestClient.patch()
            .uri("/api/projects/$projectId/tasks")
            .header("id-token", "test-token")
            .exchange()
            .expectBody(ErrorResponse::class.java)
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.UNAUTHORIZED)
        assertThat(result.responseBody?.message).isEqualTo("Please use proper authorization: Test invalid token")
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should throw forbidden when userId is not registered`() {
        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance().verifyIdToken(any()) } returns mockk(relaxed = true)
        val taskResult1 = UploadTaskResultCommand(
            1,
            "testTask",
            "user1",
            LocalDateTime.now().minusHours(5),
            LocalDateTime.now(),
            emptyList()
        )
        val taskResult2 = UploadTaskResultCommand(
            1,
            "testTask",
            "user2",
            LocalDateTime.now().minusHours(6),
            LocalDateTime.now().minusHours(2),
            emptyList()
        )
        val uploadCommandList = listOf(taskResult1, taskResult2)
        coJustRun { updateUserProfileLastSyncedTimeUseCase.updateLastSyncedTime(any()) }
        coJustRun { uploadTaskResultUseCase.uploadResults(uploadCommandList) }
        mockkObject(ContextHolder)
        coEvery { ContextHolder.getFirebaseToken().uid } returns "testUID"
        coEvery { existUserProfileUseCase.existsByUserId(UserProfile.UserId.from("testUID")) } returns false

        val result = webTestClient.patch()
            .uri("/api/projects/1/tasks")
            .header("id-token", "testToken")
            .body(BodyInserters.fromValue(uploadCommandList))
            .exchange()
            .expectBody(ErrorResponse::class.java)
            .returnResult()

        assertThat(result.status).isEqualTo(HttpStatus.FORBIDDEN)
        assertThat(result.responseBody?.message).isEqualTo("This user(testUID) is not registered on this project")
    }
}
