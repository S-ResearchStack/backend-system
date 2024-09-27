package researchstack.backend.application.serializer.education

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import researchstack.backend.domain.education.EducationalContent
import researchstack.backend.enums.ScratchContentBlockType

class ScratchContentBlockPolymorphicSerializer :
    JsonContentPolymorphicSerializer<EducationalContent.ScratchContent.Block>(EducationalContent.ScratchContent.Block::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<EducationalContent.ScratchContent.Block> {
        return when (element.jsonObject["type"]?.jsonPrimitive?.content) {
            ScratchContentBlockType.TEXT.name -> TextBlockSerializer()
            ScratchContentBlockType.IMAGE.name -> ImageBlockSerializer()
            ScratchContentBlockType.VIDEO.name -> VideoBlockSerializer()
            else -> throw IllegalArgumentException("failed to read type of scratch content block")
        }
    }
}
