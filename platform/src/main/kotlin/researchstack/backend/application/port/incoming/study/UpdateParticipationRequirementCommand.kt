package researchstack.backend.application.port.incoming.study

import researchstack.backend.application.service.mapper.ParticipationRequirementMapper
import researchstack.backend.domain.study.ParticipationRequirement
import researchstack.backend.enums.HealthDataType
import researchstack.backend.enums.QuestionTag
import researchstack.backend.enums.QuestionType

data class UpdateParticipationRequirementCommand(
    val informedConsent: InformedConsent,
    val healthDataTypeList: List<HealthDataType>,
    val eligibilityTest: EligibilityTest?,
    val taskInfo: List<DataSpec>?
) {
    fun toDomain(): ParticipationRequirement = ParticipationRequirementMapper.INSTANCE.toDomain(this)

    data class InformedConsent(
        val imagePath: String
    )

    data class EligibilityTest(
        val sections: List<Section>,
        val answers: List<Answer>
    ) {
        init {
            val m = mutableMapOf<String, QuestionType>()
            sections.forEach { section ->
                section.questions.forEach { question ->
                    m[question.id] = question.type
                }
            }
            answers.forEach {
                require(m[it.questionId] == it.type) { "Unmatched answer with question id: ${it.questionId}" }
            }
        }

        data class Section(
            val questions: List<Question>
        ) {
            data class Question(
                val id: String,
                val title: String,
                val explanation: String,
                val tag: QuestionTag,
                val required: Boolean,
                val type: QuestionType,
                val properties: Map<String, Any>
            )
        }

        data class Answer(
            val questionId: String,
            val type: QuestionType,
            val properties: Map<String, Any>
        )
    }

    data class DataSpec(
        val dataId: String,
        val dataName: String,
        val dataDescription: String,
        val collectionMethod: String,
        val targetTrialNumber: Long,
        val durationThreshold: Long
    )
}
