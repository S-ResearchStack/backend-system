package researchstack.backend.adapter.serializer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import researchstack.backend.domain.task.Question
import researchstack.backend.enums.QuestionType

class QuestionPolymorphicSerializer : JsonContentPolymorphicSerializer<Question>(Question::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Question> {
        return when (element.jsonObject["type"]?.jsonPrimitive?.content) {
            QuestionType.CHOICE.name -> QuestionSerializer(Question.ChoiceProperties.serializer())
            QuestionType.SCALE.name -> QuestionSerializer(Question.ScaleProperties.serializer())
            QuestionType.TEXT.name -> QuestionSerializer(Question.TextProperties.serializer())
            QuestionType.DATETIME.name -> QuestionSerializer(Question.DateTimeProperties.serializer())
            QuestionType.RANKING.name -> QuestionSerializer(Question.RankingProperties.serializer())
            else -> throw IllegalArgumentException("failed to read type of question")
        }
    }
}
