package researchstack.backend.application.serializer.education

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import researchstack.backend.domain.education.EducationalContent
import researchstack.backend.enums.ScratchContentBlockType

class ImageBlockSerializer : KSerializer<EducationalContent.ScratchContent.ImageBlock> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("ImageContentBlock") {
            element<String>("id")
            element<ScratchContentBlockType>("type")
            element<Int>("sequence")
            element<List<EducationalContent.ScratchContent.ImageBlock>>("images")
        }

    override fun deserialize(decoder: Decoder): EducationalContent.ScratchContent.ImageBlock {
        return decoder.decodeStructure(descriptor) {
            var id = ""
            var type = ScratchContentBlockType.IMAGE
            var sequence = -1
            var images = listOf<EducationalContent.ScratchContent.Image>()

            while (true) {
                when (decodeElementIndex(descriptor)) {
                    0 -> id = decodeStringElement(descriptor, 0)
                    1 -> type = enumValueOf(decodeStringElement(descriptor, 1))
                    2 -> sequence = decodeIntElement(descriptor, 2)
                    3 -> images = decodeSerializableElement(
                        descriptor,
                        3,
                        ListSerializer(EducationalContent.ScratchContent.Image.serializer())
                    )

                    CompositeDecoder.DECODE_DONE -> break
                }
            }
            EducationalContent.ScratchContent.ImageBlock(id, type, sequence, images)
        }
    }

    override fun serialize(encoder: Encoder, value: EducationalContent.ScratchContent.ImageBlock) {
        encoder.encodeString(value.toString())
    }
}
