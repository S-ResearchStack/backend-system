package researchstack.backend.domain.study

import kotlinx.serialization.Serializable
import researchstack.backend.adapter.serializer.LocalDateSerializer
import researchstack.backend.adapter.serializer.LocalTimeSerializer
import researchstack.backend.domain.task.SurveyTask
import researchstack.backend.enums.QuestionType
import java.time.LocalDate
import java.time.LocalTime

data class EligibilityTest(
    val surveyTask: SurveyTask,
    val answers: List<Answer>
) {
    data class Answer(
        val questionId: String,
        val type: QuestionType?,
        val item: AnswerItem
    )

    sealed interface AnswerItem

    @Serializable
    data class ChoiceAnswer(val options: List<Option>) : AnswerItem {
        @Serializable
        data class Option(
            val value: String,
            val label: String
        )
    }

    @Serializable
    data class ScaleAnswer(
        val from: Int,
        val to: Int
    ) : AnswerItem

    @Serializable
    data class TextAnswer(val answers: List<String>) : AnswerItem

    @Serializable
    data class RankingAnswer(val answers: List<String>) : AnswerItem

    @Serializable
    data class DateTimeAnswer(
        @Serializable(with = LocalDateSerializer::class)
        val fromDate: LocalDate,
        @Serializable(with = LocalDateSerializer::class)
        val toDate: LocalDate,
        @Serializable(with = LocalTimeSerializer::class)
        val fromTime: LocalTime,
        @Serializable(with = LocalTimeSerializer::class)
        val toTime: LocalTime
    ) : AnswerItem

    companion object {
        fun new(
            surveyTask: SurveyTask,
            answers: List<Answer>
        ): EligibilityTest {
            return EligibilityTest(
                surveyTask = surveyTask,
                answers = answers
            )
        }
    }
}
