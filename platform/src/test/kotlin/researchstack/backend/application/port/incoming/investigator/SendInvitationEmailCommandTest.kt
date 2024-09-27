package researchstack.backend.application.port.incoming.investigator

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.application.port.incoming.common.SendInvitationEmailCommand
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class SendInvitationEmailCommandTest {
    private val email = "test@test.com"
    private val sender = "test-sender"
    private val studyName = "test-study-name"
    private val role = "test-role"

    @Test
    @Tag(NEGATIVE_TEST)
    fun `SendInvitationEmailCommand should throw IllegalArgumentException if email is empty`() = runTest {
        val email = ""
        val exception = assertThrows<IllegalArgumentException> {
            SendInvitationEmailCommand(
                email,
                sender,
                studyName,
                role
            )
        }
        assertEquals(ExceptionMessage.EMPTY_EMAIL, exception.message)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `SendInvitationEmailCommand should throw IllegalArgumentException if sender is empty`() = runTest {
        val sender = ""
        val exception = assertThrows<IllegalArgumentException> {
            SendInvitationEmailCommand(
                email,
                sender,
                studyName,
                role
            )
        }
        assertEquals(ExceptionMessage.EMPTY_SENDER, exception.message)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `SendInvitationEmailCommand should throw IllegalArgumentException if study name is empty`() = runTest {
        val studyName = ""
        val exception = assertThrows<IllegalArgumentException> {
            SendInvitationEmailCommand(
                email,
                sender,
                studyName,
                role
            )
        }
        assertEquals(ExceptionMessage.EMPTY_STUDY_NAME, exception.message)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `SendInvitationEmailCommand should throw IllegalArgumentException if role is empty`() = runTest {
        val role = ""
        val exception = assertThrows<IllegalArgumentException> {
            SendInvitationEmailCommand(
                email,
                sender,
                studyName,
                role
            )
        }
        assertEquals(ExceptionMessage.EMPTY_ROLE, exception.message)
    }
}
