package researchstack.backend.adapter.outgoing.mongo.entity.study

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import researchstack.backend.adapter.outgoing.mongo.entity.task.TaskSpecEntity
import researchstack.backend.enums.HealthDataType
import researchstack.backend.enums.QuestionType
import java.time.LocalDate
import java.time.LocalTime

@Document("participationRequirement")
data class ParticipationRequirementEntity(
    @Id
    val studyId: String,
    val eligibilityTest: EligibilityTest?,
    val informedConsent: InformedConsent,
    val healthDataTypeList: List<HealthDataType>,
    val taskInfo: List<DataSpec>?
) {
    data class EligibilityTest(
        val surveyTask: TaskSpecEntity.SurveyTask,
        val answers: List<Answer>
    ) {
        data class Answer(
            val questionId: String,
            val type: QuestionType?,
            val item: AnswerItem
        )

        sealed interface AnswerItem

        data class ChoiceAnswer(val options: List<Option>) : AnswerItem {
            data class Option(
                val value: String,
                val label: String
            )
        }

        data class ScaleAnswer(
            val from: Int,
            val to: Int
        ) : AnswerItem

        data class TextAnswer(val answers: List<String>) : AnswerItem

        data class RankingAnswer(val answers: List<String>) : AnswerItem

        data class DateTimeAnswer(
            val fromDate: LocalDate,
            val toDate: LocalDate,
            val fromTime: LocalTime,
            val toTime: LocalTime
        ) : AnswerItem
    }

    data class InformedConsent(
        val imagePath: String
    )

    data class DataSpec(
        val dataId: String,
        val dataName: String,
        val dataDescription: String,
        val collectionMethod: String,
        val targetTrialNumber: Long,
        val durationThreshold: Long
    )
}
