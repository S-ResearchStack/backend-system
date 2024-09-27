package researchstack.backend.adapter.incoming.rest.inlabvisit

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
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import researchstack.backend.EducationTestUtil
import researchstack.backend.InLabVisitTestUtil
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.adapter.incoming.rest.common.JsonHandler
import researchstack.backend.application.port.incoming.inlabvisit.CreateInLabVisitResponse
import researchstack.backend.application.port.incoming.inlabvisit.CreateInLabVisitUseCase
import researchstack.backend.application.port.incoming.inlabvisit.DeleteInLabVisitUseCase
import researchstack.backend.application.port.incoming.inlabvisit.GetInLabVisitListResponse
import researchstack.backend.application.port.incoming.inlabvisit.GetInLabVisitUseCase
import researchstack.backend.application.port.incoming.inlabvisit.UpdateInLabVisitUseCase
import researchstack.backend.config.getUserId
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class InLabVisitControllerTest {
    private val serviceRequestContext = mockk<ServiceRequestContext>()

    private val createInLabVisitUseCase = mockk<CreateInLabVisitUseCase>()
    private val getInLabVisitUseCase = mockk<GetInLabVisitUseCase>()
    private val updateInLabVisitUseCase = mockk<UpdateInLabVisitUseCase>()
    private val deleteInLabVisitUseCase = mockk<DeleteInLabVisitUseCase>()
    private val inLabVisitRestController = InLabVisitRestController(
        createInLabVisitUseCase,
        getInLabVisitUseCase,
        updateInLabVisitUseCase,
        deleteInLabVisitUseCase
    )

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
    fun `createInLabVisit should throws IllegalArgumentException when studyId is empty`(studyId: String) = runTest {
        val command = InLabVisitTestUtil.getCreateInLabVisitCommand()

        coEvery {
            serviceRequestContext.getUserId()
        } returns EducationTestUtil.userId

        val exception = assertThrows<IllegalArgumentException> {
            inLabVisitRestController.createInLabVisit(studyId, command)
        }

        assertEquals(ExceptionMessage.EMPTY_STUDY_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `createInLabVisit should throws IllegalArgumentException when userId is empty`(userId: String) = runTest {
        val studyId = InLabVisitTestUtil.studyId
        val command = InLabVisitTestUtil.getCreateInLabVisitCommand()

        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val exception = assertThrows<IllegalArgumentException> {
            inLabVisitRestController.createInLabVisit(studyId, command)
        }

        assertEquals(ExceptionMessage.EMPTY_USER_ID, exception.message)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `createInLabVisit should work properly`() = runTest {
        val studyId = InLabVisitTestUtil.studyId
        val userId = InLabVisitTestUtil.creatorId
        val command = InLabVisitTestUtil.getCreateInLabVisitCommand()
        val response = CreateInLabVisitResponse(InLabVisitTestUtil.id)

        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        coEvery {
            createInLabVisitUseCase.createInLabVisit(studyId, userId, command)
        } returns response

        val res = inLabVisitRestController.createInLabVisit(studyId, command).aggregate().join()
        assertEquals(HttpStatus.OK, res.status())
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getInLabVisitList should throws IllegalArgumentException when studyId is empty`(studyId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns InLabVisitTestUtil.creatorId

        val exception = assertThrows<IllegalArgumentException> {
            inLabVisitRestController.getInLabVisitList(studyId)
        }

        assertEquals(ExceptionMessage.EMPTY_STUDY_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getInLabVisitList should throws IllegalArgumentException when userId is empty`(userId: String) = runTest {
        val studyId = InLabVisitTestUtil.studyId

        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val exception = assertThrows<IllegalArgumentException> {
            inLabVisitRestController.getInLabVisitList(studyId)
        }

        assertEquals(ExceptionMessage.EMPTY_USER_ID, exception.message)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getInLabVisitList should work properly`() = runTest {
        val studyId = InLabVisitTestUtil.studyId
        val userId = InLabVisitTestUtil.creatorId
        val inLabVisitList = listOf(InLabVisitTestUtil.getInLabVisit())
        val response = GetInLabVisitListResponse(inLabVisitList, inLabVisitList.size)

        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        coEvery {
            getInLabVisitUseCase.getInLabVisitList(studyId)
        } returns response

        val res = assertDoesNotThrow {
            inLabVisitRestController.getInLabVisitList(studyId).aggregate().join()
        }
        assertEquals(JsonHandler.toJson(response), res.content().toStringUtf8())
        assertEquals(HttpStatus.OK, res.status())
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `updateInLabVisit should work properly`() = runTest {
        val studyId = InLabVisitTestUtil.studyId
        val userId = InLabVisitTestUtil.creatorId
        val inLabVisitId = InLabVisitTestUtil.id
        val command = InLabVisitTestUtil.getUpdateInLabVisitCommand()

        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        coEvery {
            updateInLabVisitUseCase.updateInLabVisit(studyId, inLabVisitId, command)
        } returns Unit

        val res = inLabVisitRestController.updateInLabVisit(studyId, inLabVisitId, command).aggregate().join()
        assertEquals(HttpStatus.OK, res.status())
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `deleteInLabVisit should work properly`() = runTest {
        val studyId = InLabVisitTestUtil.studyId
        val userId = InLabVisitTestUtil.creatorId
        val inLabVisitId = InLabVisitTestUtil.id

        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        coEvery {
            deleteInLabVisitUseCase.deleteInLabVisit(studyId, inLabVisitId)
        } returns Unit

        val res = inLabVisitRestController.deleteInLabVisit(studyId, inLabVisitId).aggregate().join()
        assertEquals(HttpStatus.OK, res.status())
    }
}
