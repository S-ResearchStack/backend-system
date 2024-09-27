package researchstack.backend.adapter.incoming.rest.common

import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.util.*

class OptionalSerializer : JsonSerializer<Optional<*>> {
    override fun serialize(
        src: Optional<*>?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return if (src == null || !src.isPresent) {
            JsonNull.INSTANCE
        } else {
            context?.serialize(src.get()) ?: JsonNull.INSTANCE
        }
    }
}
