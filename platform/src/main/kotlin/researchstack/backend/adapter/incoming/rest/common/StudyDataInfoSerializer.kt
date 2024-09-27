package researchstack.backend.adapter.incoming.rest.common

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import researchstack.backend.domain.studydata.StudyDataFile
import researchstack.backend.domain.studydata.StudyDataFolder
import java.lang.reflect.Type

object StudyDataInfoSerializer : JsonSerializer<StudyDataFolder> {
    override fun serialize(src: StudyDataFolder, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("id", src.id)
        jsonObject.addProperty("name", src.name)
        jsonObject.addProperty("studyId", src.studyId)
        jsonObject.addProperty("parentId", src.parentId)
        jsonObject.addProperty("type", src.type.name)
        if (src is StudyDataFile) {
            jsonObject.addProperty("fileType", src.fileType.name)
            jsonObject.addProperty("filePath", src.filePath)
            jsonObject.addProperty("fileSize", src.fileSize)
            jsonObject.addProperty("filePreview", src.filePreview)
            jsonObject.addProperty("createdAt", src.createdAt.toString())
        }
        return jsonObject
    }
}
