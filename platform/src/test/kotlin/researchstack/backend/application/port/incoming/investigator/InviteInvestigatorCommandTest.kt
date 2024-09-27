package researchstack.backend.application.port.incoming.investigator

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.adapter.exception.ExceptionMessage
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class InviteInvestigatorCommandTest {
    private val email = "test@test.com"
    private val studyId = "test-study-id"
    private val role = "test-role"

    @Test
    @Tag(NEGATIVE_TEST)
    fun `InviteInvestigatorCommand should throw IllegalArgumentException if email is empty`() = runTest {
        val email = ""
        val exception = assertThrows<IllegalArgumentException> {
            InviteInvestigatorCommand(
                email,
                studyId,
                role
            )
        }
        assertEquals(ExceptionMessage.EMPTY_EMAIL, exception.message)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `InviteInvestigatorCommand should throw IllegalArgumentException if studyId is empty`() = runTest {
        val studyId = ""
        val exception = assertThrows<IllegalArgumentException> {
            InviteInvestigatorCommand(
                email,
                studyId,
                role
            )
        }
        assertEquals(ExceptionMessage.EMPTY_STUDY_ID, exception.message)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `InviteInvestigatorCommand should throw IllegalArgumentException if role is empty`() = runTest {
        val role = ""
        val exception = assertThrows<IllegalArgumentException> {
            InviteInvestigatorCommand(
                email,
                studyId,
                role
            )
        }
        assertEquals(ExceptionMessage.EMPTY_ROLE, exception.message)
    }
}
