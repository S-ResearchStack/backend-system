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
import researchstack.backend.StudyTestUtil.Companion.createCreateStudyCommand
import researchstack.backend.StudyTestUtil.Companion.createUpdateStudyCommand
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.application.port.incoming.study.CreateStudyCommandEnum
import researchstack.backend.application.port.incoming.study.CreateStudyResponse
import researchstack.backend.application.port.incoming.study.CreateStudyUseCase
import researchstack.backend.application.port.incoming.study.DeleteStudyUseCase
import researchstack.backend.application.port.incoming.study.GetStudyUseCase
import researchstack.backend.application.port.incoming.study.UpdateStudyUseCase
import researchstack.backend.config.getUserId
import researchstack.backend.domain.study.HealthDataGroup
import java.nio.charset.Charset
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class StudyRestControllerTest {
    private val serviceRequestContext = mockk<ServiceRequestContext>()

    private val createStudyUseCase = mockk<CreateStudyUseCase>()
    private val getStudyUseCase = mockk<GetStudyUseCase>()
    private val updateStudyUseCase = mockk<UpdateStudyUseCase>()
    private val deleteStudyUseCase = mockk<DeleteStudyUseCase>()
    private val studyRestController = StudyRestController(
        createStudyUseCase,
        getStudyUseCase,
        updateStudyUseCase,
        deleteStudyUseCase
    )

    private val userId = "7e3ba225-0e3c-4c87-9d02-b1fbe0802239"
    private val studyId = "test-study-id"

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
    fun `createStudy throws IllegalArgumentException when userId was empty`(userId: String) = runTest {
        val command = createCreateStudyCommand(studyId)
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId
        coEvery {
            createStudyUseCase.createStudy(userId, command)
        } returns CreateStudyResponse(studyId)

        val exception = assertThrows<IllegalArgumentException> {
            studyRestController.createStudy(command)
        }
        assertEquals(ExceptionMessage.EMPTY_USER_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `createStudy throws IllegalArgumentException when studyId was empty`(studyId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        assertThrows<IllegalArgumentException> {
            studyRestController.createStudy(createCreateStudyCommand(studyId))
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `createStudy should work properly`() = runTest {
        val command = createCreateStudyCommand(studyId)
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId
        coEvery {
            createStudyUseCase.createStudy(userId, command)
        } returns CreateStudyResponse(studyId)

        val res = studyRestController.createStudy(command).aggregate().join()
        assertEquals(HttpStatus.OK, res.status())
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getCreateStudyCommand throws IllegalArgumentException when userId was empty`(userId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId
        coEvery {
            getStudyUseCase.getCreateStudyCommand()
        } returns CreateStudyCommandEnum()

        val exception = assertThrows<IllegalArgumentException> {
            studyRestController.getCreateStudyCommand()
        }
        assertEquals(ExceptionMessage.EMPTY_USER_ID, exception.message)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getCreateStudyCommand should work properly`() = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId
        coEvery {
            getStudyUseCase.getCreateStudyCommand()
        } returns CreateStudyCommandEnum()

        val res = studyRestController.getCreateStudyCommand().aggregate().join()
        assertEquals(HttpStatus.OK, res.status())
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getStudyList throws IllegalArgumentException when userId was empty`(userId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId
        coEvery {
            getStudyUseCase.getStudyListByUser(userId)
        } returns listOf()

        val exception = assertThrows<IllegalArgumentException> {
            studyRestController.getStudyList()
        }
        assertEquals(ExceptionMessage.EMPTY_USER_ID, exception.message)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getStudyList should work properly`() = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId
        coEvery {
            getStudyUseCase.getStudyListByUser(userId)
        } returns listOf()

        val res = studyRestController.getStudyList().aggregate().join()
        assertEquals(HttpStatus.OK, res.status())
        assertEquals("[]", res.content(Charset.defaultCharset()))
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getStudy throws IllegalArgumentException when userId was empty`(userId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val exception = assertThrows<IllegalArgumentException> {
            studyRestController.getStudy(studyId)
        }
        assertEquals(ExceptionMessage.EMPTY_USER_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getStudy throws IllegalArgumentException when studyId was empty`(studyId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val exception = assertThrows<IllegalArgumentException> {
            studyRestController.getStudy(studyId)
        }
        assertEquals(ExceptionMessage.EMPTY_STUDY_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `updateStudy throws IllegalArgumentException when userId was empty`(userId: String) = runTest {
        val command = createUpdateStudyCommand()
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId
        coEvery {
            updateStudyUseCase.updateStudy(studyId, command)
        } returns Unit

        val exception = assertThrows<IllegalArgumentException> {
            studyRestController.updateStudy(studyId, command)
        }
        assertEquals(ExceptionMessage.EMPTY_USER_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `updateStudy throws IllegalArgumentException when studyId was empty`(studyId: String) = runTest {
        val command = createUpdateStudyCommand()
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId
        coEvery {
            updateStudyUseCase.updateStudy(studyId, command)
        } returns Unit

        val exception = assertThrows<IllegalArgumentException> {
            studyRestController.updateStudy(studyId, command)
        }
        assertEquals(ExceptionMessage.EMPTY_STUDY_ID, exception.message)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `updateStudy should work properly`() = runTest {
        val command = createUpdateStudyCommand()
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId
        coEvery {
            updateStudyUseCase.updateStudy(studyId, command)
        } returns Unit

        val res = studyRestController.updateStudy(studyId, command).aggregate().join()
        assertEquals(HttpStatus.OK, res.status())
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `deleteStudy throws IllegalArgumentException when userId was empty`(userId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId
        coEvery {
            deleteStudyUseCase.deleteStudy(studyId)
        } returns Unit

        val exception = assertThrows<IllegalArgumentException> {
            studyRestController.deleteStudy(studyId)
        }
        assertEquals(ExceptionMessage.EMPTY_USER_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `deleteStudy throws IllegalArgumentException when studyId was empty`(studyId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId
        coEvery {
            deleteStudyUseCase.deleteStudy(studyId)
        } returns Unit

        val exception = assertThrows<IllegalArgumentException> {
            studyRestController.deleteStudy(studyId)
        }
        assertEquals(ExceptionMessage.EMPTY_STUDY_ID, exception.message)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `deleteStudy should work properly`() = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId
        coEvery {
            deleteStudyUseCase.deleteStudy(studyId)
        } returns Unit

        val res = studyRestController.deleteStudy(studyId).aggregate().join()
        assertEquals(HttpStatus.OK, res.status())
    }
}
