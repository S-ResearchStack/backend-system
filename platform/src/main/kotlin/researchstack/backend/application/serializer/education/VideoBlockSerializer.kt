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

class VideoBlockSerializer : KSerializer<EducationalContent.ScratchContent.VideoBlock> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("VideoContentBlock") {
            element<String>("id")
            element<ScratchContentBlockType>("type")
            element<Int>("sequence")
            element<String>("url")
            element<String>("text")
        }

    override fun deserialize(decoder: Decoder): EducationalContent.ScratchContent.VideoBlock {
        return decoder.decodeStructure(descriptor) {
            var id = ""
            var type = ScratchContentBlockType.VIDEO
            var sequence = -1
            var url = ""
            var text = ""

            while (true) {
                when (decodeElementIndex(descriptor)) {
                    0 -> id = decodeStringElement(descriptor, 0)
                    1 -> type = enumValueOf(decodeStringElement(descriptor, 1))
                    2 -> sequence = decodeIntElement(descriptor, 2)
                    3 -> url = decodeStringElement(descriptor, 3)
                    4 -> text = decodeStringElement(descriptor, 4)
                    CompositeDecoder.DECODE_DONE -> break
                }
            }

            EducationalContent.ScratchContent.VideoBlock(id, type, sequence, url, text)
        }
    }

    override fun serialize(encoder: Encoder, value: EducationalContent.ScratchContent.VideoBlock) {
        TODO("Not yet implemented")
    }
}
