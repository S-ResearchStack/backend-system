package researchstack.backend.application.service.studydata

import researchstack.backend.application.port.outgoing.storage.DownloadObjectPort
import researchstack.backend.enums.StudyDataFileType
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.zip.GZIPOutputStream

open class StudyDataBaseService(
    private val downloadObjectPort: DownloadObjectPort
) {
    suspend fun getPreview(type: StudyDataFileType, path: String): String? {
        val preview: String = when (type) {
            StudyDataFileType.RAW_DATA -> getCSVPreview(path)
            StudyDataFileType.META_INFO -> getMetaInfoPreview(path)
            StudyDataFileType.MESSAGE_LOG -> getCSVPreview(path)
            else -> null
        } ?: return null

        val bos = ByteArrayOutputStream()
        GZIPOutputStream(bos).bufferedWriter().use { it.write(preview) }
        return Base64.getEncoder().encodeToString(bos.toByteArray())
    }

    private suspend fun getCSVPreview(path: String): String? {
        return downloadObjectPort.getPartialObject(path)
            ?.split("\n")
            ?.dropLast(1)
            ?.joinToString("\n")
    }

    private suspend fun getMetaInfoPreview(path: String): String? {
        return downloadObjectPort.getObject(path)
    }
}
