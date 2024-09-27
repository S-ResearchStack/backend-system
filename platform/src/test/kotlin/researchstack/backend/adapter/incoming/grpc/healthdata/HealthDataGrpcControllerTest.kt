package researchstack.backend.adapter.incoming.grpc.healthdata

import com.google.protobuf.Any
import com.google.protobuf.ByteString
import com.google.protobuf.Int64Value
import com.google.protobuf.Timestamp
import com.linecorp.armeria.server.ServiceRequestContext
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.incoming.mapper.healthdata.toDomain
import researchstack.backend.adapter.incoming.mapper.toDomain
import researchstack.backend.application.port.incoming.healthdata.UploadHealthDataCommand
import researchstack.backend.application.port.incoming.healthdata.UploadHealthDataUseCase
import researchstack.backend.config.getUserId
import researchstack.backend.domain.subject.Subject
import researchstack.backend.grpc.HealthData
import researchstack.backend.grpc.HealthData.Data
import researchstack.backend.grpc.HealthDataSyncRequest

@ExperimentalCoroutinesApi
internal class HealthDataGrpcControllerTest {
    private val uploadHealthDataUseCase = mockk<UploadHealthDataUseCase>()
    private val healthDataGrpcController = HealthDataGrpcController(
        uploadHealthDataUseCase
    )
    private val serviceRequestContext = mockk<ServiceRequestContext>()
    private val data = Data.newBuilder()
        .putDataMap(
            "time",
            Any.newBuilder()
                .setTypeUrl("type.googleapis.com/google.protobuf.Timestamp")
                .setValue(
                    ByteString.copyFrom(
                        Any.pack(
                            Timestamp.newBuilder().setSeconds(1701411670).setNanos(0).build()
                        ).value.toByteArray()
                    )
                ).build()
        )
        .putDataMap(
            "heart_rate",
            Any.newBuilder()
                .setTypeUrl("type.googleapis.com/google.protobuf.Int64Value")
                .setValue(
                    ByteString.copyFrom(
                        Any.pack(
                            Int64Value.newBuilder().setValue(180L).build()
                        ).value.toByteArray()
                    )
                ).build()
        ).build()
    private val healthData = HealthData.newBuilder()
        .setType(HealthData.HealthDataType.HEALTH_DATA_TYPE_HEART_RATE)
        .addAllData(listOf(data))
        .build()

    private val studyIds = listOf("mentalCareStudy")
    private val userId = "7e3ba225-0e3c-4c87-9d02-b1fbe0802239"

    @BeforeEach
    fun setup() {
        mockkStatic(ServiceRequestContext::class)
        coEvery {
            ServiceRequestContext.current()
        } returns serviceRequestContext
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `syncHealthData should work properly`() = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val healthDataSyncRequest = HealthDataSyncRequest.newBuilder()
            .addAllStudyIds(studyIds)
            .setHealthData(healthData)
            .build()

        coEvery {
            uploadHealthDataUseCase.upload(
                Subject.SubjectId.from(userId),
                studyIds,
                UploadHealthDataCommand(
                    healthDataSyncRequest.healthData.type.toDomain(),
                    healthDataSyncRequest.healthData.dataList.map { it.toDomain() }
                )
            )
        } returns Unit

        assertDoesNotThrow {
            healthDataGrpcController.syncHealthData(healthDataSyncRequest)
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `syncHealthData should throw IllegalArgumentException if userId is empty`(userId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val healthDataSyncRequest = HealthDataSyncRequest.newBuilder()
            .addAllStudyIds(studyIds)
            .setHealthData(healthData)
            .build()

        assertThrows<IllegalArgumentException> {
            healthDataGrpcController.syncHealthData(healthDataSyncRequest)
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `syncHealthData should throw IllegalArgumentException if studyIds is empty`() = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val studyIds = listOf<String>()
        val healthDataSyncRequest = HealthDataSyncRequest.newBuilder()
            .addAllStudyIds(studyIds)
            .setHealthData(healthData)
            .build()

        assertThrows<IllegalArgumentException> {
            healthDataGrpcController.syncHealthData(healthDataSyncRequest)
        }
    }
}
