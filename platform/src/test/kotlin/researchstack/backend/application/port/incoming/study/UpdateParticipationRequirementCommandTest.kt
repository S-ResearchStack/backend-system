package researchstack.backend.application.port.incoming.study

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.enums.QuestionTag
import researchstack.backend.enums.QuestionType
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class UpdateParticipationRequirementCommandTest {
    private val questionId = "test-question-id"
    private val questionTitle = "test-question-title"
    private val questionExplanation = "test-question-explanation"
    private val questionTag = QuestionTag.CHECKBOX
    private val questionRequired = false
    private val questionType = QuestionType.RANKING
    private val questionProperties = mapOf("key" to "value")

    @Test
    @Tag(POSITIVE_TEST)
    fun `Create Question class should work properly`() = runTest {
        val question = UpdateParticipationRequirementCommand.EligibilityTest.Section.Question(
            id = questionId,
            title = questionTitle,
            explanation = questionExplanation,
            tag = questionTag,
            required = questionRequired,
            type = questionType,
            properties = questionProperties
        )

        assertEquals(questionId, question.id)
        assertEquals(questionTitle, question.title)
        assertEquals(questionExplanation, question.explanation)
        assertEquals(questionTag, question.tag)
        assertEquals(questionRequired, question.required)
        assertEquals(questionType, question.type)
        assertEquals(questionProperties, question.properties)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `Create DataSpec should work properly`() = runTest {
        val dataId = "test-data-id"
        val dataName = "test-data-name"
        val dataDescription = "test-description"
        val collectionMethod = "test-collection-method"
        val targetTrialNumber = 1
        val durationThreshold = 1

        val dataSpec = UpdateParticipationRequirementCommand.DataSpec(
            dataId = dataId,
            dataName = dataName,
            dataDescription = dataDescription,
            collectionMethod = collectionMethod,
            targetTrialNumber = targetTrialNumber.toLong(),
            durationThreshold = durationThreshold.toLong()
        )

        assertEquals(dataId, dataSpec.dataId)
        assertEquals(dataName, dataSpec.dataName)
        assertEquals(dataDescription, dataSpec.dataDescription)
        assertEquals(collectionMethod, dataSpec.collectionMethod)
        assertEquals(targetTrialNumber.toLong(), dataSpec.targetTrialNumber)
        assertEquals(durationThreshold.toLong(), dataSpec.durationThreshold)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `Create Answer should work properly`() = runTest {
        val questionId = "test-question-id"
        val type = QuestionType.RANKING
        val properties = mapOf("key" to "value")

        val answer = UpdateParticipationRequirementCommand.EligibilityTest.Answer(
            questionId = questionId,
            type = type,
            properties = properties
        )

        assertEquals(questionId, answer.questionId)
        assertEquals(type, answer.type)
        assertEquals(properties, answer.properties)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `Create EligibilityTest should work properly`() = runTest {
        val sections = listOf(
            UpdateParticipationRequirementCommand.EligibilityTest.Section(
                questions = listOf(
                    UpdateParticipationRequirementCommand.EligibilityTest.Section.Question(
                        id = questionId,
                        title = questionTitle,
                        explanation = questionExplanation,
                        tag = questionTag,
                        required = questionRequired,
                        type = questionType,
                        properties = questionProperties
                    )
                )
            )
        )
        val answers = listOf(
            UpdateParticipationRequirementCommand.EligibilityTest.Answer(
                questionId = questionId,
                type = questionType,
                properties = questionProperties
            )
        )

        val eligibilityTest = UpdateParticipationRequirementCommand.EligibilityTest(
            sections = sections,
            answers = answers
        )

        assertEquals(sections, eligibilityTest.sections)
        assertEquals(answers, eligibilityTest.answers)
    }
}
