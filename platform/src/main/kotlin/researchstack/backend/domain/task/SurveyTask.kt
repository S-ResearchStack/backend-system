package researchstack.backend.domain.task

import kotlinx.serialization.Serializable
import researchstack.backend.adapter.serializer.QuestionPolymorphicSerializer
import researchstack.backend.enums.QuestionTag
import researchstack.backend.enums.QuestionType

@Serializable
data class SurveyTask(val sections: List<Section>) : TaskSpec.Task

@Serializable
data class Section(val questions: List<Question>)

@Serializable(QuestionPolymorphicSerializer::class)
data class Question(
    val id: String,
    val title: String,
    val explanation: String,
    val tag: QuestionTag,
    val itemProperties: ItemProperties,
    val required: Boolean,
    val type: QuestionType?
) {
    @Serializable
    sealed interface ItemProperties

    @Serializable
    data class ChoiceProperties(val options: List<Option>) : ItemProperties

    @Serializable
    data class ScaleProperties(
        val low: Int,
        val high: Int,
        val lowLabel: String,
        val highLabel: String
    ) : ItemProperties

    @Serializable
    class TextProperties : ItemProperties

    @Serializable
    data class RankingProperties(val options: List<Option>) : ItemProperties

    @Serializable
    data class DateTimeProperties(
        val isTime: Boolean,
        val isDate: Boolean,
        val isRange: Boolean
    ) : ItemProperties

    @Serializable
    data class Option(
        val value: String,
        val label: String
    )
}
