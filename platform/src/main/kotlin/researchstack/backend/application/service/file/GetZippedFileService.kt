package researchstack.backend.application.service.file

import org.springframework.stereotype.Service
import researchstack.backend.application.port.incoming.file.GetZippedFileUseCase
import researchstack.backend.application.port.outgoing.storage.DownloadObjectPort
import researchstack.backend.domain.common.Url
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Service
class GetZippedFileService(
    private val downloadObjectPort: DownloadObjectPort
) : GetZippedFileUseCase {

    override suspend fun getDownloadPresignedUrl(studyId: String, subjectNumber: String?): Url {
        val prefix = if (subjectNumber == null) {
            "$studyId/"
        } else {
            "$studyId/$subjectNumber/"
        }
        return Url(getZippedFilePresignedUrl(prefix))
    }

    private suspend fun getZippedFilePresignedUrl(prefix: String): String {
        val (objectName, objectNameTheDayBefore) = getObjectNames(prefix)

        if (downloadObjectPort.doesObjectExist(objectName)) {
            return downloadObjectPort.getDownloadPresignedUrl(objectName).toString()
        }

        return downloadObjectPort.getDownloadPresignedUrl(objectNameTheDayBefore).toString()
    }

    private fun getObjectNames(prefix: String): Pair<String, String> {
        val curTime = LocalDate.now(ZoneId.of("Asia/Seoul"))
        // Data from the previous day in Korean time
        val targetDate = curTime.minusDays(1).format(DateTimeFormatter.BASIC_ISO_DATE)
        // Data from the day before yesterday in Korean time
        val theDayBeforeTargetDate = curTime.minusDays(2).format(DateTimeFormatter.BASIC_ISO_DATE)

        val objectName = "$INTEGRATE_DIRECTORY/${prefix.dropLast(1)}-$targetDate$ZIP_DATA_EXTENSION"
        val objectNameTheDayBefore =
            "$INTEGRATE_DIRECTORY/${prefix.dropLast(1)}-$theDayBeforeTargetDate$ZIP_DATA_EXTENSION"
        return Pair(objectName, objectNameTheDayBefore)
    }

    companion object {
        const val ZIP_DATA_EXTENSION: String = ".zip"
        const val INTEGRATE_DIRECTORY: String = "INTEGRATE"
    }
}
