package researchstack.backend.adapter.incoming.rest.studydata

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
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.application.port.incoming.studydata.AddStudyDataUseCase
import researchstack.backend.application.port.incoming.studydata.GetStudyDataUseCase
import researchstack.backend.application.port.incoming.studydata.GetSubjectUseCase
import researchstack.backend.application.port.incoming.studydata.StudyDataCountResponse
import researchstack.backend.application.port.incoming.studydata.UpdateSubjectUseCase
import researchstack.backend.config.getUserId
import researchstack.backend.enums.SubjectStatus
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class StudyDataRestControllerTest {
    private val serviceRequestContext = mockk<ServiceRequestContext>()

    private val getSubjectUseCase = mockk<GetSubjectUseCase>()
    private val updateSubjectUseCase = mockk<UpdateSubjectUseCase>()
    private val addStudyDataUseCase = mockk<AddStudyDataUseCase>()
    private val getStudyDataUseCase = mockk<GetStudyDataUseCase>()

    private val studyDataRestController = StudyDataRestController(
        getSubjectUseCase,
        updateSubjectUseCase,
        addStudyDataUseCase,
        getStudyDataUseCase
    )

    private val userId = "7e3ba225-0e3c-4c87-9d02-b1fbe0802239"
    private val studyId = "test-study-id"
    private val subjectNumber = "test-subject-number"
    private val countResponse = StudyDataCountResponse(1)

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
    fun `getSubjectInfoList throws IllegalArgumentException when studyId was empty`(studyId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId
        coEvery {
            getSubjectUseCase.getSubjectInfoList(studyId)
        } returns listOf()

        val exception = assertThrows<IllegalArgumentException> {
            studyDataRestController.getSubjectInfoList(studyId)
        }
        assertEquals(ExceptionMessage.EMPTY_STUDY_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getSubjectInfoList throws IllegalArgumentException when userId was empty`(userId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId
        coEvery {
            getSubjectUseCase.getSubjectInfoList(studyId)
        } returns listOf()

        val exception = assertThrows<IllegalArgumentException> {
            studyDataRestController.getSubjectInfoList(studyId)
        }
        assertEquals(ExceptionMessage.EMPTY_USER_ID, exception.message)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getSubjectInfoList should work properly`() = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId
        coEvery {
            getSubjectUseCase.getSubjectInfoList(studyId)
        } returns listOf()

        val res = studyDataRestController.getSubjectInfoList(studyId).aggregate().join()
        assertEquals(HttpStatus.OK, res.status())
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getSubjectInfoListCount throws IllegalArgumentException when studyId is empty`(studyId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val exception = assertThrows<IllegalArgumentException> {
            studyDataRestController.getSubjectInfoListCount(studyId)
        }
        assertEquals(ExceptionMessage.EMPTY_STUDY_ID, exception.message)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getSubjectInfoListCount should work properly`() = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId
        coEvery {
            getSubjectUseCase.getSubjectInfoListCount(studyId)
        } returns countResponse

        val res = studyDataRestController.getSubjectInfoListCount(studyId).aggregate().join()
        assertEquals(HttpStatus.OK, res.status())
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `updateSubjectStatus should work properly`() = runTest {
        val subjectStatus = "PARTICIPATING"
        coEvery {
            ServiceRequestContext.current().getUserId()
        } returns userId
        coEvery {
            updateSubjectUseCase.updateSubjectStatus(
                studyId,
                subjectNumber,
                SubjectStatus.valueOf(subjectStatus)
            )
        } returns Unit

        assertDoesNotThrow {
            studyDataRestController.setSubjectStatus(
                studyId,
                subjectNumber,
                subjectStatus
            )
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `updateSubjectStatus should throw IllegalArgumentException if studyId is empty`(studyId: String) = runTest {
        coEvery {
            ServiceRequestContext.current().getUserId()
        } returns userId
        val subjectStatus = "PARTICIPATING"
        assertThrows<IllegalArgumentException> {
            studyDataRestController.setSubjectStatus(
                studyId,
                subjectNumber,
                subjectStatus
            )
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `updateSubjectStatus should throw IllegalArgumentException if subjectNumber is empty`(subjectNumber: String) =
        runTest {
            coEvery {
                ServiceRequestContext.current().getUserId()
            } returns userId
            val subjectStatus = "PARTICIPATING"
            assertThrows<IllegalArgumentException> {
                studyDataRestController.setSubjectStatus(
                    studyId,
                    subjectNumber,
                    subjectStatus
                )
            }
        }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `updateSubjectStatus should throw IllegalArgumentException if subjectStatus is invalid`() = runTest {
        coEvery {
            ServiceRequestContext.current().getUserId()
        } returns userId
        val subjectStatus = "UNSPECIFIED"
        assertThrows<IllegalArgumentException> {
            studyDataRestController.setSubjectStatus(
                studyId,
                subjectNumber,
                subjectStatus
            )
        }
    }
}
