package researchstack.backend.util

import kotlinx.serialization.json.Json
import org.json.JSONObject

val json = Json {
    ignoreUnknownKeys = true
    encodeDefaults = false
}

inline fun <reified T> Map<String, Any>.toInstance(): T =
    json.decodeFromString<T>(JSONObject(this).toString())
