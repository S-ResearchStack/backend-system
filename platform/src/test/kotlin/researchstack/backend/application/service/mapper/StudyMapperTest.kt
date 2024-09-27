package researchstack.backend.application.service.mapper

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.StudyTestUtil
import researchstack.backend.StudyTestUtil.Companion.createCreateStudyCommand
import researchstack.backend.StudyTestUtil.Companion.createUpdateStudyCommand
import researchstack.backend.application.port.incoming.study.ParticipateInStudyCommand.EligibilityTestResultCommand
import researchstack.backend.application.port.incoming.study.ParticipateInStudyCommand.SignedInformedConsentCommand
import researchstack.backend.application.port.incoming.task.TaskResultCommand
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class StudyMapperTest {
    @Test
    @Tag(POSITIVE_TEST)
    fun `should map CreateStudyCommand to Study domain`() = runTest {
        // given
        val createStudyCommand = createCreateStudyCommand(null)

        // when
        val result = createStudyCommand.toDomain()

        // then
        assertEquals(StudyTestUtil.studyId, result.id)
        assertEquals(StudyTestUtil.participationCode, result.participationCode)
        assertEquals(StudyTestUtil.studyName, result.studyInfo.name)
        assertEquals(StudyTestUtil.studyDescription, result.studyInfo.description)
        assertEquals(StudyTestUtil.participationApprovalType, result.studyInfo.participationApprovalType)
        assertEquals(StudyTestUtil.studyScope, result.studyInfo.scope)
        assertEquals(StudyTestUtil.studyStage, result.studyInfo.stage)
        assertEquals(StudyTestUtil.logoUrl, result.studyInfo.logoUrl)
        assertEquals(StudyTestUtil.imageUrl, result.studyInfo.imageUrl)
        assertEquals(StudyTestUtil.organization, result.studyInfo.organization)
        assertEquals(StudyTestUtil.duration, result.studyInfo.duration)
        assertEquals(StudyTestUtil.period, result.studyInfo.period)
        assertEquals(StudyTestUtil.requirements, result.studyInfo.requirements)
        assertEquals(StudyTestUtil.decisionType, result.irbInfo.irbDecisionType)
        assertEquals(StudyTestUtil.decidedAt, result.irbInfo.decidedAt)
        assertEquals(StudyTestUtil.expiredAt, result.irbInfo.expiredAt)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should map UpdateStudyCommand to Study domain`() = runTest {
        // given
        val updateStudyCommand = createUpdateStudyCommand()

        // when
        val result = StudyMapper.INSTANCE.toDomain(updateStudyCommand, StudyTestUtil.studyId)

        // then
        assertEquals(StudyTestUtil.studyId, result.id)
        assertEquals(StudyTestUtil.participationCode, result.participationCode)
        assertEquals(StudyTestUtil.studyName, result.studyInfo.name)
        assertEquals(StudyTestUtil.studyDescription, result.studyInfo.description)
        assertEquals(StudyTestUtil.participationApprovalType, result.studyInfo.participationApprovalType)
        assertEquals(StudyTestUtil.studyScope, result.studyInfo.scope)
        assertEquals(StudyTestUtil.studyStage, result.studyInfo.stage)
        assertEquals(StudyTestUtil.logoUrl, result.studyInfo.logoUrl)
        assertEquals(StudyTestUtil.imageUrl, result.studyInfo.imageUrl)
        assertEquals(StudyTestUtil.organization, result.studyInfo.organization)
        assertEquals(StudyTestUtil.duration, result.studyInfo.duration)
        assertEquals(StudyTestUtil.period, result.studyInfo.period)
        assertEquals(StudyTestUtil.requirements, result.studyInfo.requirements)
        assertEquals(StudyTestUtil.decisionType, result.irbInfo.irbDecisionType)
        assertEquals(StudyTestUtil.decidedAt, result.irbInfo.decidedAt)
        assertEquals(StudyTestUtil.expiredAt, result.irbInfo.expiredAt)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should map EligibilityTestResultCommand to EligibilityTestResult domain`() = runTest {
        // given
        val questionId = "questionId"
        val questionResult = "questionResult"
        val eligibilityTestResultCommand = EligibilityTestResultCommand(
            result = TaskResultCommand.SurveyResult(
                questionResults = listOf(
                    TaskResultCommand.SurveyResult.QuestionResult(
                        id = questionId,
                        result = questionResult
                    )
                )
            )
        )

        // when
        val result = eligibilityTestResultCommand.toDomain()

        // then
        assertEquals(questionId, result.result.questionResults[0].id)
        assertEquals(questionResult, result.result.questionResults[0].result)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should map SignedInformedConsentCommand to SignedInformedConsent domain`() = runTest {
        // given
        val imagePath = "http://test-image.com"
        val signedInformedConsentCommand = SignedInformedConsentCommand(imagePath)

        // when
        val result = signedInformedConsentCommand.toDomain()

        // then
        assertEquals(imagePath, result.imagePath)
    }
}
