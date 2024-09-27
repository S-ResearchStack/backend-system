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
internal class RegisterInvestigatorCommandTest {
    private val firstName = "kevin"
    private val lastName = "kim"
    private val company = "samsung"
    private val team = "DIT"
    private val officePhoneNumber = "02-1234-5678"
    private val mobilePhoneNumber = "010-1234-5678"
    private val attachmentUrls = null

    @Test
    @Tag(NEGATIVE_TEST)
    fun `RegisterInvestigatorCommand should throw IllegalArgumentException if first name is empty`() = runTest {
        val firstName = ""
        val exception = assertThrows<IllegalArgumentException> {
            RegisterInvestigatorCommand(
                firstName,
                lastName,
                company,
                team,
                officePhoneNumber,
                mobilePhoneNumber,
                attachmentUrls
            )
        }
        assertEquals(ExceptionMessage.EMPTY_FIRST_NAME, exception.message)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `RegisterInvestigatorCommand should throw IllegalArgumentException if last name is empty`() = runTest {
        val lastName = ""
        val exception = assertThrows<IllegalArgumentException> {
            RegisterInvestigatorCommand(
                firstName,
                lastName,
                company,
                team,
                officePhoneNumber,
                mobilePhoneNumber,
                attachmentUrls
            )
        }
        assertEquals(ExceptionMessage.EMPTY_LAST_NAME, exception.message)
    }
}
