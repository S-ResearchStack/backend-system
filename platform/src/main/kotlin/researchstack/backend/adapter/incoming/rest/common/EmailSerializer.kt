package researchstack.backend.adapter.incoming.rest.common

import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import researchstack.backend.domain.common.Email
import java.lang.reflect.Type

class EmailSerializer : JsonSerializer<Email> {
    override fun serialize(src: Email?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return context?.serialize(src?.value) ?: JsonNull.INSTANCE
    }
}
