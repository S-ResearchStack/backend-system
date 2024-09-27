package researchstack.backend.application.port.incoming.study

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.enums.IrbDecisionType
import researchstack.backend.enums.StudyParticipationApprovalType
import researchstack.backend.enums.StudyScope
import researchstack.backend.enums.StudyStage
import java.time.LocalDateTime
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class CreateStudyCommandTest {
    private val id = "test-study-id"
    private val participationCode = "secret"
    private val name = "Dummy Study"
    private val description = "description for dummy study"
    private val participationApprovalType = StudyParticipationApprovalType.MANUAL
    private val scope = StudyScope.PRIVATE
    private val stage = StudyStage.CREATED
    private val logoUrl = "https://dummy.com/logo.png"
    private val imageUrl = "https://dummy.com/image.png"
    private val organization = "Samsung Medical Center"
    private val duration = "Takes about 15 minutes a week"
    private val period = "Takes about 1 month in total"
    private val requirements = listOf(
        "Complete daily check-in",
        "You have to wear Galaxy Watch all day except when you're asleep"
    )
    private val irbDecisionType = IrbDecisionType.EXEMPT
    private val targetSubject = 150L
    private val startDate = LocalDateTime.now()
    private val endDate = startDate.plusMonths(6)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `CreateStudyCommand should throw IllegalArgumentException if id is empty`() = runTest {
        val id = ""
        val exception = assertThrows<IllegalArgumentException> {
            CreateStudyCommand(
                id,
                participationCode,
                name,
                description,
                participationApprovalType,
                scope,
                stage,
                logoUrl,
                imageUrl,
                organization,
                duration,
                period,
                requirements,
                irbDecisionType,
                null,
                null,
                targetSubject,
                startDate,
                endDate
            )
        }
        assertEquals(ExceptionMessage.EMPTY_STUDY_ID, exception.message)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `CreateStudyCommand should throw IllegalArgumentException if name is empty`() = runTest {
        val name = ""
        val exception = assertThrows<IllegalArgumentException> {
            CreateStudyCommand(
                id,
                participationCode,
                name,
                description,
                participationApprovalType,
                scope,
                stage,
                logoUrl,
                imageUrl,
                organization,
                duration,
                period,
                requirements,
                irbDecisionType,
                null,
                null,
                targetSubject,
                startDate,
                endDate
            )
        }
        assertEquals(ExceptionMessage.EMPTY_STUDY_NAME, exception.message)
    }
}
