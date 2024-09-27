package researchstack.backend.application.service.subject

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.SubjectTestUtil
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.application.port.outgoing.casbin.AddRoleOutPort
import researchstack.backend.application.port.outgoing.subject.RegisterSubjectOutPort
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class RegisterUserServiceTest {
    private val registerSubjectOutPort = mockk<RegisterSubjectOutPort>()
    private val addRoleOutPort = mockk<AddRoleOutPort>()

    private val registerUserService = RegisterSubjectService(
        registerSubjectOutPort,
        addRoleOutPort
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `registerUser should work properly`() = runTest {
        val registerUserCommand = SubjectTestUtil.createRegisterSubjectCommand(
            SubjectTestUtil.subjectId
        )
        val userProfile = registerUserCommand.toDomain()

        coEvery {
            registerSubjectOutPort.registerSubject(userProfile)
        } returns Unit

        coEvery {
            addRoleOutPort.addRolesForMyself(SubjectTestUtil.subjectId)
        } returns Unit

        assertDoesNotThrow {
            registerUserService.registerSubject(registerUserCommand)
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `registerUser should throw IllegalArgumentException if subjectId is empty`(subjectId: String) = runTest {
        val exception = assertThrows<IllegalArgumentException> {
            registerUserService.registerSubject(
                SubjectTestUtil.createRegisterSubjectCommand(subjectId)
            )
        }
        assertEquals(ExceptionMessage.EMPTY_SUBJECT_ID, exception.message)
    }
}
