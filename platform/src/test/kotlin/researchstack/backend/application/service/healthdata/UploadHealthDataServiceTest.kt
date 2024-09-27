package researchstack.backend.application.service.healthdata

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.port.incoming.healthdata.UploadHealthDataCommand
import researchstack.backend.application.port.outgoing.healthdata.UploadHealthDataOutPort
import researchstack.backend.application.port.outgoing.storage.UploadObjectPort
import researchstack.backend.application.port.outgoing.subject.GetSubjectProfileOutPort
import researchstack.backend.domain.healthdata.HealthData
import researchstack.backend.domain.subject.Subject.SubjectId
import researchstack.backend.enums.HealthDataType

@ExperimentalCoroutinesApi
internal class UploadHealthDataServiceTest {
    private val getSubjectProfileOutPort = mockk<GetSubjectProfileOutPort>()
    private val uploadHealthDataOutPort = mockk<UploadHealthDataOutPort>()
    private val uploadObjectPort = mockk<UploadObjectPort>()

    private val uploadHealthDataService = UploadHealthDataService(
        getSubjectProfileOutPort,
        uploadHealthDataOutPort,
        uploadObjectPort
    )

    private val studyIds = listOf("heartStudy", "mentalCareStudy")
    private val command = UploadHealthDataCommand(
        HealthDataType.HEART_RATE,
        listOf(
            mapOf(
                "time" to "2023-07-12T03:56:36",
                "heart_rate" to 80
            ),
            mapOf(
                "time" to "2023-07-12T03:59:42",
                "heart_rate" to 87
            ),
            mapOf(
                "time" to "2023-07-12T04:02:01",
                "heart_rate" to 83
            )
        )
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `upload should work properly`() = runTest {
        val subjectId = SubjectId.from("test-subject")

        coEvery {
            uploadHealthDataOutPort.upload(
                subjectId,
                studyIds,
                HealthDataType.HEART_RATE,
                listOf(
                    HealthData(
                        mapOf("time" to "2023-07-12T03:56:36", "heart_rate" to 80)
                    ),
                    HealthData(
                        mapOf("time" to "2023-07-12T03:59:42", "heart_rate" to 87)
                    ),
                    HealthData(
                        mapOf("time" to "2023-07-12T04:02:01", "heart_rate" to 83)
                    )
                )
            )
        } returns Unit

        uploadHealthDataService.upload(subjectId, studyIds, command)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `upload should throw IllegalArgumentException if userId is null`() = runTest {
        val userId = null
        assertThrows<IllegalArgumentException> {
            uploadHealthDataService.upload(SubjectId.from(userId), studyIds, command)
        }
    }
}
