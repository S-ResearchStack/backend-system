package researchstack.backend.adapter.incoming.rest.study

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
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.application.port.incoming.study.CreateParticipationRequirementCommand
import researchstack.backend.application.port.incoming.study.CreateParticipationRequirementUseCase
import researchstack.backend.application.port.incoming.study.DeleteParticipationRequirementUseCase
import researchstack.backend.application.port.incoming.study.GetParticipationRequirementUseCase
import researchstack.backend.application.port.incoming.study.ParticipationRequirementResponse
import researchstack.backend.application.port.incoming.study.UpdateParticipationRequirementCommand
import researchstack.backend.application.port.incoming.study.UpdateParticipationRequirementUseCase
import researchstack.backend.config.getUserId
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class ParticipationRequirementRestControllerTest {
    private val serviceRequestContext = mockk<ServiceRequestContext>()

    private val createUseCase = mockk<CreateParticipationRequirementUseCase>()
    private val getUseCase = mockk<GetParticipationRequirementUseCase>()
    private val updateUseCase = mockk<UpdateParticipationRequirementUseCase>()
    private val deleteUseCase = mockk<DeleteParticipationRequirementUseCase>()

    private val participationRequirementRestController = ParticipationRequirementRestController(
        createUseCase,
        getUseCase,
        updateUseCase,
        deleteUseCase
    )

    private val userId = "7e3ba225-0e3c-4c87-9d02-b1fbe0802239"
    private val studyId = "test-study-id"
    private val participationRequirement = ParticipationRequirementResponse(
        null,
        ParticipationRequirementResponse.InformedConsentResponse("http://www.example.com/image.png"),
        listOf(),
        emptyList()
    )

    private fun createCreateParticipationRequirementCommand() = CreateParticipationRequirementCommand(
        CreateParticipationRequirementCommand.InformedConsent("http://www.example.com/image.png"),
        listOf(),
        null,
        emptyList()
    )

    private fun updateParticipationRequirementCommand() = UpdateParticipationRequirementCommand(
        UpdateParticipationRequirementCommand.InformedConsent("http://www.example.com/image.png"),
        listOf(),
        null,
        emptyList()
    )

    @BeforeEach
    fun setup() {
        mockkStatic(ServiceRequestContext::class)
        coEvery { ServiceRequestContext.current() } returns serviceRequestContext
        coEvery { serviceRequestContext.getUserId() } returns userId
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `createParticipationRequirement throws IllegalArgumentException when studyId was empty`(studyId: String) =
        runTest {
            val command = createCreateParticipationRequirementCommand()
            coEvery { createUseCase.create(command, studyId) } returns Unit

            val exception = assertThrows<IllegalArgumentException> {
                participationRequirementRestController.createParticipationRequirement(studyId, command)
            }
            assertEquals(ExceptionMessage.EMPTY_STUDY_ID, exception.message)
        }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `createParticipationRequirement throws IllegalArgumentException when userId was empty`(userId: String) =
        runTest {
            val command = createCreateParticipationRequirementCommand()
            coEvery { createUseCase.create(command, studyId) } returns Unit
            coEvery { serviceRequestContext.getUserId() } returns userId
            val exception = assertThrows<IllegalArgumentException> {
                participationRequirementRestController.createParticipationRequirement(studyId, command)
            }
            assertEquals(ExceptionMessage.EMPTY_USER_ID, exception.message)
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `createParticipationRequirement should work properly`() = runTest {
        val command = createCreateParticipationRequirementCommand()
        coEvery { createUseCase.create(command, studyId) } returns Unit

        val res =
            participationRequirementRestController.createParticipationRequirement(studyId, command).aggregate().join()
        assertEquals(HttpStatus.OK, res.status())
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getParticipationRequirement should work properly`() = runTest {
        coEvery { getUseCase.getParticipationRequirementForHttp(studyId) } returns participationRequirement
        val res = participationRequirementRestController.getParticipationRequirement(studyId).aggregate().join()
        assertEquals(HttpStatus.OK, res.status())
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getParticipationRequirement throws IllegalArgumentException when studyId was empty`(studyId: String) =
        runTest {
            coEvery { getUseCase.getParticipationRequirementForHttp(studyId) } returns participationRequirement

            val exception = assertThrows<IllegalArgumentException> {
                participationRequirementRestController.getParticipationRequirement(studyId)
            }
            assertEquals(ExceptionMessage.EMPTY_STUDY_ID, exception.message)
        }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getParticipationRequirement throws IllegalArgumentException when userId was empty`(userId: String) = runTest {
        coEvery { getUseCase.getParticipationRequirementForHttp(studyId) } returns participationRequirement
        coEvery { serviceRequestContext.getUserId() } returns userId
        val exception = assertThrows<IllegalArgumentException> {
            participationRequirementRestController.getParticipationRequirement(studyId)
        }
        assertEquals(ExceptionMessage.EMPTY_USER_ID, exception.message)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `updateParticipationRequirement should work properly`() = runTest {
        val command = updateParticipationRequirementCommand()
        coEvery { updateUseCase.update(studyId, command) } returns Unit
        val res =
            participationRequirementRestController.updateParticipationRequirement(studyId, command).aggregate().join()
        assertEquals(HttpStatus.OK, res.status())
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `updateParticipationRequirement throws IllegalArgumentException when studyId was empty`(studyId: String) =
        runTest {
            val command = updateParticipationRequirementCommand()
            coEvery { updateUseCase.update(studyId, command) } returns Unit
            val exception = assertThrows<IllegalArgumentException> {
                participationRequirementRestController.updateParticipationRequirement(studyId, command)
            }
            assertEquals(ExceptionMessage.EMPTY_STUDY_ID, exception.message)
        }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `updateParticipationRequirement throws IllegalArgumentException when userId was empty`(userId: String) =
        runTest {
            val command = updateParticipationRequirementCommand()
            coEvery { updateUseCase.update(studyId, command) } returns Unit
            coEvery { serviceRequestContext.getUserId() } returns userId
            val exception = assertThrows<IllegalArgumentException> {
                participationRequirementRestController.updateParticipationRequirement(studyId, command)
            }
            assertEquals(ExceptionMessage.EMPTY_USER_ID, exception.message)
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `deleteParticipationRequirement should work properly`() = runTest {
        coEvery { deleteUseCase.delete(studyId) } returns Unit
        val res = participationRequirementRestController.deleteParticipationRequirement(studyId).aggregate().join()
        assertEquals(HttpStatus.OK, res.status())
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `deleteParticipationRequirement throws IllegalArgumentException when studyId was empty`(studyId: String) =
        runTest {
            coEvery { deleteUseCase.delete(studyId) } returns Unit
            val exception = assertThrows<IllegalArgumentException> {
                participationRequirementRestController.deleteParticipationRequirement(studyId)
            }
            assertEquals(ExceptionMessage.EMPTY_STUDY_ID, exception.message)
        }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `deleteParticipationRequirement throws IllegalArgumentException when userId was empty`(userId: String) =
        runTest {
            coEvery { deleteUseCase.delete(studyId) } returns Unit
            coEvery { serviceRequestContext.getUserId() } returns userId
            val exception = assertThrows<IllegalArgumentException> {
                participationRequirementRestController.deleteParticipationRequirement(studyId)
            }
            assertEquals(ExceptionMessage.EMPTY_USER_ID, exception.message)
        }
}
