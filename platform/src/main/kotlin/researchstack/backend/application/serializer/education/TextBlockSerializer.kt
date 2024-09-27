package researchstack.backend.application.serializer.education

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import researchstack.backend.domain.education.EducationalContent
import researchstack.backend.enums.ScratchContentBlockType

class TextBlockSerializer : KSerializer<EducationalContent.ScratchContent.TextBlock> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("TextContentBlock") {
            element<String>("id")
            element<ScratchContentBlockType>("type")
            element<Int>("sequence")
            element<String>("text")
        }

    override fun deserialize(decoder: Decoder): EducationalContent.ScratchContent.TextBlock {
        return decoder.decodeStructure(descriptor) {
            var id = ""
            var type = ScratchContentBlockType.TEXT
            var sequence = -1
            var text = ""

            while (true) {
                when (decodeElementIndex(descriptor)) {
                    0 -> id = decodeStringElement(descriptor, 0)
                    1 -> type = enumValueOf(decodeStringElement(descriptor, 1))
                    2 -> sequence = decodeIntElement(descriptor, 2)
                    3 -> text = decodeStringElement(descriptor, 3)
                    CompositeDecoder.DECODE_DONE -> break
                }
            }

            EducationalContent.ScratchContent.TextBlock(id, type, sequence, text)
        }
    }

    override fun serialize(encoder: Encoder, value: EducationalContent.ScratchContent.TextBlock) {
        encoder.encodeString(value.toString())
    }
}
