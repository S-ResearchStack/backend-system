package researchstack.backend.application.port.incoming.task

import researchstack.backend.enums.QuestionTag

data class SurveyTaskResponse(val sections: List<Section>) : TaskSpecResponse.Task

data class Section(val questions: List<Question>)

data class Question(
    val id: String,
    val title: String,
    val explanation: String,
    val tag: QuestionTag,
    val itemProperties: ItemProperties,
    val required: Boolean
) {
    sealed interface ItemProperties
    data class ChoiceProperties(val options: List<Option>) : ItemProperties

    data class ScaleProperties(
        val low: Int,
        val high: Int,
        val lowLabel: String,
        val highLabel: String
    ) : ItemProperties

    class TextProperties : ItemProperties

    data class RankingProperties(val options: List<Option>) : ItemProperties

    data class DateTimeProperties(
        val isTime: Boolean,
        val isDate: Boolean,
        val isRange: Boolean
    ) : ItemProperties

    data class Option(
        val value: String,
        val label: String
    )
}
