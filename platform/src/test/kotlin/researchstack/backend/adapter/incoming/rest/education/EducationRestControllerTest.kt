package researchstack.backend.adapter.incoming.rest.education

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
import researchstack.backend.EducationTestUtil
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.adapter.incoming.rest.common.JsonHandler
import researchstack.backend.application.port.incoming.education.CreateEducationalContentResponse
import researchstack.backend.application.port.incoming.education.CreateEducationalContentUseCase
import researchstack.backend.application.port.incoming.education.DeleteEducationalContentUseCase
import researchstack.backend.application.port.incoming.education.GetEducationalContentUseCase
import researchstack.backend.application.port.incoming.education.UpdateEducationalContentUseCase
import researchstack.backend.config.getUserId
import researchstack.backend.domain.education.EducationalContent
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class EducationRestControllerTest {
    private val serviceRequestContext = mockk<ServiceRequestContext>()

    private val createEducationalContentUseCase = mockk<CreateEducationalContentUseCase>()
    private val getEducationalContentUseCase = mockk<GetEducationalContentUseCase>()
    private val updateEducationalContentUseCase = mockk<UpdateEducationalContentUseCase>()
    private val deleteEducationalContentUseCase = mockk<DeleteEducationalContentUseCase>()
    private val educationRestController = EducationRestController(
        createEducationalContentUseCase,
        getEducationalContentUseCase,
        updateEducationalContentUseCase,
        deleteEducationalContentUseCase
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
    fun `createEducationalContent should throws IllegalArgumentException when studyId is empty`(studyId: String) =
        runTest {
            val command = EducationTestUtil.getCreateEducationalContentCommand()

            coEvery {
                serviceRequestContext.getUserId()
            } returns EducationTestUtil.userId

            val exception = assertThrows<IllegalArgumentException> {
                educationRestController.createEducationalContent(studyId, command)
            }

            assertEquals(ExceptionMessage.EMPTY_STUDY_ID, exception.message)
        }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `createEducationalContent should throws IllegalArgumentException when userId is empty`(userId: String) =
        runTest {
            val studyId = EducationTestUtil.studyId
            val command = EducationTestUtil.getCreateEducationalContentCommand()

            coEvery {
                serviceRequestContext.getUserId()
            } returns userId

            val exception = assertThrows<IllegalArgumentException> {
                educationRestController.createEducationalContent(studyId, command)
            }

            assertEquals(ExceptionMessage.EMPTY_USER_ID, exception.message)
        }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `createEducationalContent should throws IllegalArgumentException when title is empty`(title: String) = runTest {
        val studyId = EducationTestUtil.studyId
        val userId = EducationTestUtil.userId
        val command = EducationTestUtil.getCreateEducationalContentCommand(t = title)

        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val exception = assertThrows<IllegalArgumentException> {
            educationRestController.createEducationalContent(studyId, command)
        }

        assertEquals(ExceptionMessage.EMPTY_EDUCATION_TITLE, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `createEducationalContent should throws IllegalArgumentException when category is empty`(category: String) =
        runTest {
            val studyId = EducationTestUtil.studyId
            val userId = EducationTestUtil.userId
            val command = EducationTestUtil.getCreateEducationalContentCommand(c = category)

            coEvery {
                serviceRequestContext.getUserId()
            } returns userId

            val exception = assertThrows<IllegalArgumentException> {
                educationRestController.createEducationalContent(studyId, command)
            }

            assertEquals(ExceptionMessage.EMPTY_EDUCATION_CATEGORY, exception.message)
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `createEducationalContent should work properly`() = runTest {
        val studyId = EducationTestUtil.studyId
        val userId = EducationTestUtil.userId
        val command = EducationTestUtil.getCreateEducationalContentCommand()

        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        coEvery {
            createEducationalContentUseCase.createEducationalContent(studyId, userId, command)
        } returns CreateEducationalContentResponse(EducationTestUtil.contentId)

        val res = educationRestController.createEducationalContent(studyId, command).aggregate().join()
        assertEquals(HttpStatus.OK, res.status())
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getEducationalContentList with studyId should throws IllegalArgumentException when studyId is empty`(studyId: String) =
        runTest {
            coEvery {
                serviceRequestContext.getUserId()
            } returns EducationTestUtil.userId

            val exception = assertThrows<IllegalArgumentException> {
                educationRestController.getEducationalContentList(studyId)
            }

            assertEquals(ExceptionMessage.EMPTY_STUDY_ID, exception.message)
        }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getEducationalContentList with studyId should throws IllegalArgumentException when userId is empty`(userId: String) =
        runTest {
            coEvery {
                serviceRequestContext.getUserId()
            } returns userId

            val exception = assertThrows<IllegalArgumentException> {
                educationRestController.getEducationalContentList(EducationTestUtil.studyId)
            }

            assertEquals(ExceptionMessage.EMPTY_USER_ID, exception.message)
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getEducationalContentList with studyId should return empty list when there is no educational content corresponding to a given study and status`() =
        runTest {
            val studyId = EducationTestUtil.studyId
            val userId = EducationTestUtil.userId
            val contentList = listOf<EducationalContent>()

            coEvery {
                serviceRequestContext.getUserId()
            } returns userId

            coEvery {
                getEducationalContentUseCase.getEducationalContentList(studyId)
            } returns contentList

            val res = educationRestController.getEducationalContentList(studyId).aggregate().join()
            assertEquals(JsonHandler.toJson(contentList), res.content().toStringUtf8())
            assertEquals(HttpStatus.OK, res.status())
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getEducationalContentList with studyId should work properly`() = runTest {
        val studyId = EducationTestUtil.studyId
        val userId = EducationTestUtil.userId
        val contentList = listOf(EducationTestUtil.getEducationalContent())

        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        coEvery {
            getEducationalContentUseCase.getEducationalContentList(studyId)
        } returns contentList

        val res = educationRestController.getEducationalContentList(studyId).aggregate().join()
        assertEquals(JsonHandler.toJson(contentList), res.content().toStringUtf8())
        assertEquals(HttpStatus.OK, res.status())
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getEducationalContentList with studyId and status should throws IllegalArgumentException when studyId is empty`(
        studyId: String
    ) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns EducationTestUtil.userId

        val exception = assertThrows<IllegalArgumentException> {
            educationRestController.getEducationalContentList(studyId, EducationTestUtil.status)
        }

        assertEquals(ExceptionMessage.EMPTY_STUDY_ID, exception.message)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getEducationalContentList with studyId and status should throws IllegalArgumentException when userId is empty`(
        userId: String
    ) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val exception = assertThrows<IllegalArgumentException> {
            educationRestController.getEducationalContentList(EducationTestUtil.studyId, EducationTestUtil.status)
        }

        assertEquals(ExceptionMessage.EMPTY_USER_ID, exception.message)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getEducationalContentList with studyId and status should return empty list when there is no educational content corresponding to a given study and status`() =
        runTest {
            val studyId = EducationTestUtil.studyId
            val status = EducationTestUtil.status
            val userId = EducationTestUtil.userId
            val contentList = listOf<EducationalContent>()

            coEvery {
                serviceRequestContext.getUserId()
            } returns userId

            coEvery {
                getEducationalContentUseCase.getEducationalContentList(studyId, status)
            } returns contentList

            val res = educationRestController.getEducationalContentList(studyId, status).aggregate().join()
            assertEquals(JsonHandler.toJson(contentList), res.content().toStringUtf8())
            assertEquals(HttpStatus.OK, res.status())
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getEducationalContentList with studyId and status should work properly`() = runTest {
        val studyId = EducationTestUtil.studyId
        val status = EducationTestUtil.status
        val userId = EducationTestUtil.userId
        val contentList = listOf(EducationTestUtil.getEducationalContent())

        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        coEvery {
            getEducationalContentUseCase.getEducationalContentList(studyId, status)
        } returns contentList

        val res = educationRestController.getEducationalContentList(studyId, status).aggregate().join()
        assertEquals(JsonHandler.toJson(contentList), res.content().toStringUtf8())
        assertEquals(HttpStatus.OK, res.status())
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `updateEducationalContent should work properly`() = runTest {
        val studyId = EducationTestUtil.studyId
        val contentId = EducationTestUtil.contentId
        val userId = EducationTestUtil.userId
        val command = EducationTestUtil.getUpdateEducationalContentCommand()

        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        coEvery {
            updateEducationalContentUseCase.updateEducationalContent(studyId, contentId, command)
        } returns Unit

        val res = educationRestController.updateEducationalContent(studyId, contentId, command).aggregate().join()
        assertEquals(HttpStatus.OK, res.status())
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `deleteEducationalContent should work properly`() = runTest {
        val studyId = EducationTestUtil.studyId
        val contentId = EducationTestUtil.contentId
        val userId = EducationTestUtil.userId

        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        coEvery {
            deleteEducationalContentUseCase.deleteEducationalContent(studyId, contentId)
        } returns Unit

        val res = educationRestController.deleteEducationalContent(studyId, contentId).aggregate().join()
        assertEquals(HttpStatus.OK, res.status())
    }
}
