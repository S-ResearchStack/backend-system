package researchstack.backend.adapter.incoming.rest.investigator

import com.linecorp.armeria.common.HttpStatus
import com.linecorp.armeria.server.ServiceRequestContext
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import researchstack.backend.InvestigatorTestUtil
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.application.port.incoming.investigator.DeleteInvestigatorUseCase
import researchstack.backend.application.port.incoming.investigator.GetInvestigatorUseCase
import researchstack.backend.application.port.incoming.investigator.InviteInvestigatorUseCase
import researchstack.backend.application.port.incoming.investigator.RegisterInvestigatorUseCase
import researchstack.backend.application.port.incoming.investigator.UpdateInvestigatorUseCase
import researchstack.backend.config.getEmail
import researchstack.backend.config.getUserId
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class InvestigatorRestControllerTest {
    private val serviceRequestContext = mockk<ServiceRequestContext>()

    private val registerInvestigatorUseCase = mockk<RegisterInvestigatorUseCase>()
    private val getInvestigatorUseCase = mockk<GetInvestigatorUseCase>()
    private val updateInvestigatorUseCase = mockk<UpdateInvestigatorUseCase>()
    private val inviteInvestigatorUseCase = mockk<InviteInvestigatorUseCase>()
    private val deleteInvestigatorUseCase = mockk<DeleteInvestigatorUseCase>()
    private val investigatorRestController = InvestigatorRestController(
        registerInvestigatorUseCase,
        getInvestigatorUseCase,
        updateInvestigatorUseCase,
        inviteInvestigatorUseCase,
        deleteInvestigatorUseCase
    )

    private val email = InvestigatorTestUtil.email
    private val userId = InvestigatorTestUtil.investigatorId
    private val studyId = InvestigatorTestUtil.studyId

    @BeforeEach
    fun setup() {
        mockkStatic(ServiceRequestContext::class)
        coEvery {
            ServiceRequestContext.current()
        } returns serviceRequestContext
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `registerInvestigator throws IllegalArgumentException when userId was empty`(userId: String) = runTest {
        val command = InvestigatorTestUtil.createRegisterInvestigatorCommand()
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId
        coEvery {
            serviceRequestContext.getEmail()
        } returns email
        coEvery {
            registerInvestigatorUseCase.registerInvestigator(userId, email, command)
        }

        val exception = assertThrows<IllegalArgumentException> {
            investigatorRestController.registerInvestigator(command)
        }
        assertEquals(ExceptionMessage.EMPTY_INVESTIGATOR_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `registerInvestigator throws IllegalArgumentException when email was empty`(email: String) = runTest {
        val command = InvestigatorTestUtil.createRegisterInvestigatorCommand()
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId
        coEvery {
            serviceRequestContext.getEmail()
        } returns email
        coEvery {
            registerInvestigatorUseCase.registerInvestigator(userId, email, command)
        }

        val exception = assertThrows<IllegalArgumentException> {
            investigatorRestController.registerInvestigator(command)
        }
        assertEquals(ExceptionMessage.EMPTY_EMAIL, exception.message)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `registerInvestigator should work properly`() = runTest {
        val command = InvestigatorTestUtil.createRegisterInvestigatorCommand()
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId
        coEvery {
            serviceRequestContext.getEmail()
        } returns email
        coEvery {
            registerInvestigatorUseCase.registerInvestigator(userId, email, command)
        } returns InvestigatorTestUtil.createInvestigator()

        val res = investigatorRestController.registerInvestigator(command).aggregate().join()
        assertEquals(HttpStatus.OK, res.status())
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getInvestigator throws IllegalArgumentException when userId was empty`(userId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId
        coEvery {
            getInvestigatorUseCase.getInvestigator(userId)
        } returns InvestigatorTestUtil.createInvestigator()

        val exception = assertThrows<IllegalArgumentException> {
            investigatorRestController.getInvestigator()
        }
        assertEquals(ExceptionMessage.EMPTY_INVESTIGATOR_ID, exception.message)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getInvestigator should work properly`() = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId
        coEvery {
            getInvestigatorUseCase.getInvestigator(userId)
        } returns InvestigatorTestUtil.createInvestigator()

        val res = investigatorRestController.getInvestigator().aggregate().join()
        assertEquals(HttpStatus.OK, res.status())
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getInvestigators throws IllegalArgumentException when studyId was empty`(studyId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId
        coEvery {
            getInvestigatorUseCase.getInvestigators(studyId)
        } returns listOf()

        val exception = assertThrows<IllegalArgumentException> {
            investigatorRestController.getInvestigators(studyId)
        }
        assertEquals(ExceptionMessage.EMPTY_STUDY_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getInvestigators throws IllegalArgumentException when userId was empty`(userId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId
        coEvery {
            getInvestigatorUseCase.getInvestigators(studyId)
        } returns listOf()

        val exception = assertThrows<IllegalArgumentException> {
            investigatorRestController.getInvestigators(studyId)
        }
        assertEquals(ExceptionMessage.EMPTY_INVESTIGATOR_ID, exception.message)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getInvestigators should work properly`() = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId
        coEvery {
            getInvestigatorUseCase.getInvestigators(studyId)
        } returns listOf()

        val res = investigatorRestController.getInvestigators(studyId).aggregate().join()
        assertEquals(HttpStatus.OK, res.status())
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `updateInvestigator throws IllegalArgumentException when investigatorId was empty`(userId: String) = runTest {
        val command = InvestigatorTestUtil.createUpdateInvestigatorCommand()
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId
        coEvery {
            serviceRequestContext.getEmail()
        } returns email
        coEvery {
            updateInvestigatorUseCase.updateInvestigator(userId, email, command)
        }

        val exception = assertThrows<IllegalArgumentException> {
            investigatorRestController.updateInvestigator(command)
        }
        assertEquals(ExceptionMessage.EMPTY_INVESTIGATOR_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `updateInvestigator throws IllegalArgumentException when email was empty`(email: String) = runTest {
        val command = InvestigatorTestUtil.createUpdateInvestigatorCommand()
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId
        coEvery {
            serviceRequestContext.getEmail()
        } returns email
        coEvery {
            updateInvestigatorUseCase.updateInvestigator(userId, email, command)
        }

        val exception = assertThrows<IllegalArgumentException> {
            investigatorRestController.updateInvestigator(command)
        }
        assertEquals(ExceptionMessage.EMPTY_EMAIL, exception.message)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `updateInvestigator should work properly`() = runTest {
        val command = InvestigatorTestUtil.createUpdateInvestigatorCommand()
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId
        coEvery {
            serviceRequestContext.getEmail()
        } returns email
        coEvery {
            updateInvestigatorUseCase.updateInvestigator(userId, email, command)
        } returns Unit

        val res = investigatorRestController.updateInvestigator(command).aggregate().join()
        assertEquals(HttpStatus.OK, res.status())
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `inviteInvestigator throws IllegalArgumentException when userId was empty`(userId: String) = runTest {
        val command = InvestigatorTestUtil.createInviteInvestigatorCommand()
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId
        coEvery {
            inviteInvestigatorUseCase.inviteInvestigator(userId, command.studyId, command.role, command)
        } returns true

        val exception = assertThrows<IllegalArgumentException> {
            investigatorRestController.inviteInvestigator(command)
        }
        assertEquals(ExceptionMessage.EMPTY_USER_ID, exception.message)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `inviteInvestigator should work properly`() = runTest {
        val command = InvestigatorTestUtil.createInviteInvestigatorCommand()
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId
        coEvery {
            inviteInvestigatorUseCase.inviteInvestigator(userId, command.studyId, command.role, command)
        } returns true

        val res = investigatorRestController.inviteInvestigator(command).aggregate().join()
        assertEquals(HttpStatus.OK, res.status())
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `updateInvestigatorRole throws IllegalArgumentException when userId was empty`(userId: String) = runTest {
        val command = InvestigatorTestUtil.createUpdateInvestigatorRoleCommand()
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId
        coEvery {
            updateInvestigatorUseCase.updateInvestigatorRole(userId, command.studyId, command.role)
        } returns InvestigatorTestUtil.invitedRole

        val exception = assertThrows<IllegalArgumentException> {
            investigatorRestController.updateInvestigatorRole(userId, command)
        }
        assertEquals(ExceptionMessage.EMPTY_INVESTIGATOR_ID, exception.message)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `updateInvestigatorRole should work properly`() = runTest {
        val command = InvestigatorTestUtil.createUpdateInvestigatorRoleCommand()
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId
        coEvery {
            updateInvestigatorUseCase.updateInvestigatorRole(userId, command.studyId, command.role)
        } returns InvestigatorTestUtil.invitedRole

        val res = investigatorRestController.updateInvestigatorRole(userId, command).aggregate().join()
        assertEquals(HttpStatus.OK, res.status())
    }
}
