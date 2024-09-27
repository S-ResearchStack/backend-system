package researchstack.backend.adapter.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import researchstack.backend.domain.task.Question
import researchstack.backend.enums.QuestionTag
import researchstack.backend.enums.QuestionType

class QuestionSerializer<T>(
    private val itemPropertiesSerializer: KSerializer<T>
) : KSerializer<Question> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Question") {
            element<String>("id")
            element<String>("title")
            element<String>("explanation")
            element<QuestionTag>("tag")
            element<Boolean>("required")
            element<Question.ItemProperties>("itemProperties")
            element<QuestionType>("type")
        }

    override fun deserialize(decoder: Decoder): Question {
        return decoder.decodeStructure(descriptor) {
            var id = ""
            var title = ""
            var explanation = ""
            var tag = QuestionTag.UNSPECIFIED
            var required = false
            var itemProperties: Question.ItemProperties = Question.TextProperties()
            var type = QuestionType.UNSPECIFIED

            while (true) {
                when (decodeElementIndex(descriptor)) {
                    0 -> id = decodeStringElement(descriptor, 0)
                    1 -> title = decodeStringElement(descriptor, 1)
                    2 -> explanation = decodeStringElement(descriptor, 2)
                    3 -> tag = enumValueOf(decodeStringElement(descriptor, 3))
                    4 -> required = decodeBooleanElement(descriptor, 4)
                    5 ->
                        itemProperties =
                            decodeSerializableElement(
                                descriptor,
                                5,
                                itemPropertiesSerializer
                            ) as Question.ItemProperties

                    6 -> type = enumValueOf(decodeStringElement(descriptor, 6))
                    CompositeDecoder.DECODE_DONE -> break
                }
            }

            Question(id, title, explanation, tag, itemProperties, required, type)
        }
    }

    override fun serialize(encoder: Encoder, value: Question) {
        encoder.encodeString(value.toString())
    }
}
