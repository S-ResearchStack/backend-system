package researchstack.backend.application.port.incoming.study

import researchstack.backend.application.port.incoming.task.SurveyTaskResponse
import researchstack.backend.enums.HealthDataType
import java.time.LocalDate
import java.time.LocalTime

data class ParticipationRequirementResponse(
    val eligibilityTest: EligibilityTestResponse?,
    val informedConsent: InformedConsentResponse,
    val healthDataTypeList: List<HealthDataType>,
    val taskInfo: List<DataSpecResponse>?
) {
    data class EligibilityTestResponse(
        val surveyTask: SurveyTaskResponse,
        val answers: List<Answer>
    ) {
        data class Answer(
            val questionId: String,
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

    data class InformedConsentResponse(
        val imagePath: String
    )

    data class DataSpecResponse(
        val dataId: String,
        val dataName: String,
        val dataDescription: String,
        val collectionMethod: String,
        val targetTrialNumber: Long,
        val durationThreshold: Long
    )
}
