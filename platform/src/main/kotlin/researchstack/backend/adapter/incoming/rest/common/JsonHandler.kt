package researchstack.backend.adapter.incoming.rest.common

import com.google.gson.GsonBuilder
import researchstack.backend.domain.studydata.StudyDataFolder
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*

object JsonHandler {
    private val gson = GsonBuilder().registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeSerializer())
        .registerTypeAdapter(Optional::class.java, OptionalSerializer())
        .disableHtmlEscaping()
        .setPrettyPrinting()
        .create()
    private val gsonWithZonedDateTime =
        GsonBuilder().registerTypeAdapter(ZonedDateTime::class.java, ZonedDateTimeSerializer())
            .setPrettyPrinting()
            .create()

    private val gsonWithStudyDataFolder = GsonBuilder()
        .registerTypeHierarchyAdapter(StudyDataFolder::class.java, StudyDataInfoSerializer)
        .registerTypeAdapter(ZonedDateTime::class.java, ZonedDateTimeSerializer())
        .setPrettyPrinting()
        .create()

    fun toJson(obj: Any) = gson.toJson(obj)

    fun toJsonWithZonedDateTime(obj: Any) = gsonWithZonedDateTime.toJson(obj)

    fun toJsonWithStudyDataInfo(obj: Any) = gsonWithStudyDataFolder.toJson(obj)
}
