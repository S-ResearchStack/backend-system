package researchstack.backend.adapter.incoming.grpc.user

import com.linecorp.armeria.common.HttpStatus
import com.linecorp.armeria.server.ServiceRequestContext
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.SubjectTestUtil
import researchstack.backend.SubjectTestUtil.Companion.subjectDomain
import researchstack.backend.adapter.incoming.rest.subject.SubjectRestController
import researchstack.backend.application.port.incoming.subject.DeregisterSubjectUseCase
import researchstack.backend.application.port.incoming.subject.GetSubjectUseCase
import researchstack.backend.application.port.incoming.subject.RegisterSubjectUseCase
import researchstack.backend.application.port.incoming.subject.UpdateSubjectProfileUseCase
import researchstack.backend.config.getUserId
import researchstack.backend.domain.subject.Subject
import researchstack.backend.enums.SubjectStatus

@ExperimentalCoroutinesApi
internal class SubjectRestControllerTest {
    private val registerSubjectUseCase = mockk<RegisterSubjectUseCase>()
    private val deregisterSubjectUseCase = mockk<DeregisterSubjectUseCase>()
    private val getUserProfileUseCase = mockk<GetSubjectUseCase>()
    private val updateSubjectProfileUseCase = mockk<UpdateSubjectProfileUseCase>()
    private val serviceRequestContext = mockk<ServiceRequestContext>()
    private val controller = SubjectRestController(
        registerSubjectUseCase,
        deregisterSubjectUseCase,
        getUserProfileUseCase,
        updateSubjectProfileUseCase
    )
    val userId = "test-subject-id"
    val studyId = "test-study-id"
    private val registerUserCommand = SubjectTestUtil.createRegisterSubjectCommand(null)
    private val updateUserCommand = SubjectTestUtil.createUpdateSubjectProfileCommand()

    @BeforeEach
    fun setup() {
        mockkStatic(ServiceRequestContext::class)
        coEvery {
            ServiceRequestContext.current()
        } returns serviceRequestContext
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `registerUser should work properly`() = runTest {
        coEvery { ServiceRequestContext.current().getUserId() } returns userId
        coEvery { registerSubjectUseCase.registerSubject(registerUserCommand) } returns Unit

        assertDoesNotThrow {
            controller.registerSubject(registerUserCommand)
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `registerUser should throw IllegalArgumentException if userId is empty`(userId: String) = runTest {
        coEvery { ServiceRequestContext.current().getUserId() } returns userId

        assertThrows<IllegalArgumentException> {
            controller.registerSubject(registerUserCommand)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `deregisterUser should work properly`() = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        coEvery {
            deregisterSubjectUseCase.deregisterSubject(Subject.SubjectId.from(userId))
        } returns Unit

        assertDoesNotThrow {
            controller.deregisterSubject()
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `deregisterUser should throw IllegalArgumentException if userId is empty`(userId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        assertThrows<IllegalArgumentException> {
            controller.deregisterSubject()
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `updateUserProfile should work properly`() = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        coEvery {
            updateSubjectProfileUseCase.updateSubjectProfile(
                Subject.SubjectId.from(userId),
                updateUserCommand
            )
        } returns Unit

        assertDoesNotThrow {
            controller.updateSubjectProfile(updateUserCommand)
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `updateUserProfile should throw IllegalArgumentException if userId is empty`(userId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        assertThrows<IllegalArgumentException> {
            controller.updateSubjectProfile(updateUserCommand)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getUserProfile should work properly`() = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        coEvery {
            getUserProfileUseCase.getSubjectProfile(
                Subject.SubjectId.from(userId)
            )
        } returns subjectDomain

        val response = controller.getUserProfile().aggregate().join()
        assertEquals(HttpStatus.OK, response.status())
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getUserProfile should throw IllegalArgumentException if userId is empty`(userId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        assertThrows<IllegalArgumentException> {
            controller.getUserProfile()
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getUserStatus should work properly`() = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        coEvery {
            getUserProfileUseCase.getSubjectStatus(
                Subject.SubjectId.from(userId),
                studyId
            )
        } returns researchstack.backend.domain.subject.SubjectStatusInfo(SubjectStatus.DROP)

        val response = controller.getSubjectStatus(studyId).aggregate().join()

        assertEquals(HttpStatus.OK, response.status())
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getUserStatus should throw IllegalArgumentException if userId is empty`(userId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        assertThrows<IllegalArgumentException> {
            controller.getSubjectStatus(studyId)
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getUserStatus should throw IllegalArgumentException if studyId is empty`(studyId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        assertThrows<IllegalArgumentException> {
            controller.getSubjectStatus(studyId)
        }
    }
}
