package researchstack.backend.application.service.healthdata

import com.opencsv.CSVWriter
import org.springframework.stereotype.Service
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.ACTION_WRITE
import researchstack.backend.adapter.outgoing.casbin.RoleConstant.RESOURCE_HEALTH_DATA
import researchstack.backend.adapter.role.Role
import researchstack.backend.adapter.role.Tenants
import researchstack.backend.application.port.incoming.healthdata.UploadBatchHealthDataCommand
import researchstack.backend.application.port.incoming.healthdata.UploadHealthDataCommand
import researchstack.backend.application.port.incoming.healthdata.UploadHealthDataUseCase
import researchstack.backend.application.port.outgoing.healthdata.UploadHealthDataOutPort
import researchstack.backend.application.port.outgoing.storage.UploadObjectPort
import researchstack.backend.application.port.outgoing.subject.GetSubjectProfileOutPort
import researchstack.backend.config.DASH
import researchstack.backend.config.SLASH
import researchstack.backend.domain.healthdata.HealthData
import researchstack.backend.domain.subject.Subject
import researchstack.backend.enums.HealthDataType
import java.io.File
import java.io.FileWriter

@Service
class UploadHealthDataService(
    private val getSubjectProfileOutPort: GetSubjectProfileOutPort,
    private val uploadHealthDataOutPort: UploadHealthDataOutPort,
    private val uploadObjectPort: UploadObjectPort
) : UploadHealthDataUseCase {
    @Role(actions = [ACTION_WRITE], resources = [RESOURCE_HEALTH_DATA])
    override suspend fun upload(
        subjectId: Subject.SubjectId,
        @Tenants studyIds: List<String>,
        command: UploadHealthDataCommand
    ) {
        uploadHealthDataOutPort.upload(
            subjectId,
            studyIds,
            command.type,
            command.data.map {
                HealthData(it)
            }
        )
    }

    @Role(actions = [ACTION_WRITE], resources = [RESOURCE_HEALTH_DATA])
    override suspend fun uploadBatch(
        subjectId: Subject.SubjectId,
        @Tenants studyIds: List<String>,
        command: UploadBatchHealthDataCommand
    ) {
        studyIds.forEach { studyId ->
            val subjectNumber = getSubjectProfileOutPort.getSubjectNumber(studyId, subjectId.value)
            if (command.batches.isNullOrEmpty()) {
                if (command.batches.isEmpty()) return@forEach
            }
            val healthDataType = command.batches.first().type
            if (command.batches.first().data.isNullOrEmpty()) return@forEach
            val startTimestamp = command.batches.first().data.first()["timestamp"]
            val endTimestamp = command.batches.last().data.last()["timestamp"]

            val fileNameWithPath =
                "$studyId$DASH$subjectNumber$DASH$startTimestamp$DASH$endTimestamp$DASH$healthDataType.csv"
            createCsvFile(command.batches, fileNameWithPath)
            uploadCsvFile(studyId, subjectNumber, healthDataType, fileNameWithPath)
            deleteCsvFile(fileNameWithPath)
        }
    }

    private fun createCsvFile(batches: List<UploadBatchHealthDataCommand.BatchHealthData>, fileNameWithPath: String) {
        val csvWriter = CSVWriter(FileWriter(fileNameWithPath))
        var isFirst = true
        batches.forEach { batchData ->
            if (isFirst) {
                csvWriter.writeNext(batchData.data[0].keys.toTypedArray<String>())
                isFirst = false
            }
            batchData.data.forEach { dataElement ->
                var row = arrayOf<String>()
                dataElement.forEach { entry ->
                    row = row.plus(entry.value.toString())
                }
                csvWriter.writeNext(row)
            }
        }
        csvWriter.close()
    }

    private suspend fun uploadCsvFile(
        studyId: String,
        subjectNumber: String,
        healthDataType: HealthDataType,
        fileNameWithPath: String
    ) {
        val objectName = listOf(studyId, subjectNumber)
            .joinToString(SLASH.toString())
            .plus(SLASH)
            .plus(WEAR_DIR)
            .plus(SLASH)
            .plus(healthDataType)
            .plus(SLASH)
            .plus(fileNameWithPath)
        uploadObjectPort.uploadFile(fileNameWithPath, objectName)
    }

    private fun deleteCsvFile(fileNameWithPath: String) {
        File(fileNameWithPath).delete()
    }

    companion object {
        const val WEAR_DIR = "wear"
    }
}
