package researchstack.backend.adapter.outgoing.mongo.healthdata

import com.linecorp.armeria.server.ServiceRequestContext
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import reactor.kotlin.core.publisher.toFlux
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.mongo.entity.healthdata.HealthDataEntity
import researchstack.backend.adapter.outgoing.mongo.repository.healthdata.HealthDataRepository
import researchstack.backend.adapter.outgoing.mongo.repository.healthdata.HealthDataRepositoryLookup
import researchstack.backend.config.JacksonConfig
import researchstack.backend.config.STUDY_ID_KEY
import researchstack.backend.domain.healthdata.BatchHealthData
import researchstack.backend.domain.healthdata.HealthData
import researchstack.backend.domain.subject.Subject
import researchstack.backend.enums.HealthDataType
import java.util.stream.Stream
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class UploadHealthDataMongoAdapterTest {
    private val serviceRequestContext = mockk<ServiceRequestContext>()
    private val healthDataRepositoryLookup = mockk<HealthDataRepositoryLookup>()
    private val objectMapper = JacksonConfig().objectMapper()
    private val healthDataRepository = mockk<HealthDataRepository<HealthDataEntity>>()
    private val adapter = UploadHealthDataMongoAdapter(healthDataRepositoryLookup, objectMapper)

    @BeforeEach
    fun setup() {
        mockkStatic(ServiceRequestContext::class)
        every {
            ServiceRequestContext.current()
        } returns serviceRequestContext
        every {
            serviceRequestContext.setAttr(STUDY_ID_KEY, any())
        } returns ""
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `upload should throw IllegalArgumentException when it received an unsupported type`() = runTest {
        val userId = "u1"
        val studyIds = listOf("s1", "s2")
        val unsupportedType = HealthDataType.UNSPECIFIED
        val data = listOf(HealthData(mapOf()))

        every {
            healthDataRepositoryLookup.getRepository(unsupportedType)
        } returns healthDataRepository
        every {
            healthDataRepository.saveAll(any<Iterable<HealthDataEntity>>())
        } returns listOf<HealthDataEntity>().toFlux()

        val exception = assertThrows<IllegalArgumentException> {
            adapter.upload(
                Subject.SubjectId.from(userId),
                studyIds,
                unsupportedType,
                data
            )
        }
        assertEquals("Unsupported data type: ${unsupportedType.name}", exception.message)
    }

    @ParameterizedTest
    @MethodSource("researchstack.backend.adapter.outgoing.mongo.healthdata.UploadHealthDataMongoAdapterTest#provideHealthData")
    @Tag(POSITIVE_TEST)
    fun `upload should work properly`(
        data: List<HealthData>,
        type: HealthDataType
    ) = runTest {
        val subjectId = "s1"
        val studyIds = listOf("s1", "s2")

        every {
            healthDataRepositoryLookup.getRepository(type)
        } returns healthDataRepository
        every {
            healthDataRepository.saveAll(any<Iterable<HealthDataEntity>>())
        } returns listOf<HealthDataEntity>().toFlux()

        assertDoesNotThrow {
            adapter.upload(
                Subject.SubjectId.from(subjectId),
                studyIds,
                type,
                data
            )
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `uploadBatch should work properly`() = runTest {
        val userId = "u1"
        val studyIds = listOf("s1", "s2")
        val type = HealthDataType.WEAR_PPG_GREEN
        val data = listOf(
            BatchHealthData(
                type,
                listOf(
                    HealthData(
                        mapOf(
                            "ppg" to 80,
                            "timestamp" to 1721624074123
                        )
                    )
                )
            )
        )

        every {
            healthDataRepositoryLookup.getRepository(type)
        } returns healthDataRepository
        every {
            healthDataRepository.saveAll(any<Iterable<HealthDataEntity>>())
        } returns listOf<HealthDataEntity>().toFlux()

        assertDoesNotThrow {
            adapter.uploadBatch(
                Subject.SubjectId.from(userId),
                studyIds,
                data
            )
        }
    }

    companion object {
        @JvmStatic
        private fun provideHealthData(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    listOf(
                        HealthData(
                            mapOf(
                                "x" to 3.14,
                                "y" to 3.14,
                                "z" to 3.14,
                                "timestamp" to 1721624074123
                            )
                        )
                    ),
                    HealthDataType.ACCELEROMETER
                ),
                Arguments.of(
                    listOf(
                        HealthData(
                            mapOf(
                                "percentage" to 3.14,
                                "charging" to true,
                                "timestamp" to 1721624074123
                            )
                        )
                    ),
                    HealthDataType.BATTERY
                ),
                Arguments.of(
                    listOf(
                        HealthData(
                            mapOf(
                                "start_time" to 1721624074123,
                                "glucose" to 3.14,
                                "measurement_type" to 1
                            )
                        )
                    ),
                    HealthDataType.BLOOD_GLUCOSE
                ),
                Arguments.of(
                    listOf(
                        HealthData(
                            mapOf(
                                "start_time" to 1721624074123,
                                "systolic" to 3.14,
                                "diastolic" to 3.14
                            )
                        )
                    ),
                    HealthDataType.BLOOD_PRESSURE
                ),
                Arguments.of(
                    listOf(
                        HealthData(
                            mapOf(
                                "start_time" to 1721624074123,
                                "end_time" to 1721624074123,
                                "exercise_type" to 1,
                                "calorie" to 3.14,
                                "duration" to 80
                            )
                        )
                    ),
                    HealthDataType.EXERCISE
                ),
                Arguments.of(
                    listOf(
                        HealthData(
                            mapOf(
                                "x" to 3.14,
                                "y" to 3.14,
                                "z" to 3.14,
                                "timestamp" to 1721624074123
                            )
                        )
                    ),
                    HealthDataType.GYROSCOPE
                ),
                Arguments.of(
                    listOf(
                        HealthData(
                            mapOf(
                                "start_time" to 1721624074123,
                                "end_time" to 1721624074123,
                                "heartRate" to 3.14
                            )
                        )
                    ),
                    HealthDataType.HEART_RATE
                ),
                Arguments.of(
                    listOf(
                        HealthData(
                            mapOf(
                                "start_time" to 1721624074123,
                                "height" to 3.14
                            )
                        )
                    ),
                    HealthDataType.HEIGHT
                ),
                Arguments.of(
                    listOf(
                        HealthData(
                            mapOf(
                                "accuracy" to 3.14,
                                "lx" to 3.14,
                                "timestamp" to 1721624074123
                            )
                        )
                    ),
                    HealthDataType.LIGHT
                ),
                Arguments.of(
                    listOf(
                        HealthData(
                            mapOf(
                                "value" to 3.14,
                                "timestamp" to 1721624074123
                            )
                        )
                    ),
                    HealthDataType.OFF_BODY
                ),
                Arguments.of(
                    listOf(
                        HealthData(
                            mapOf(
                                "start_time" to 1721624074123,
                                "end_time" to 1721624074123,
                                "spo2" to 3.14
                            )
                        )
                    ),
                    HealthDataType.OXYGEN_SATURATION
                ),
                Arguments.of(
                    listOf(HealthData(mapOf())),
                    HealthDataType.RESPIRATORY_RATE
                ),
                Arguments.of(
                    listOf(
                        HealthData(
                            mapOf(
                                "start_time" to 1721624074123,
                                "end_time" to 1721624074123
                            )
                        )
                    ),
                    HealthDataType.SLEEP_SESSION
                ),
                Arguments.of(
                    listOf(
                        HealthData(
                            mapOf(
                                "start_time" to 1721624074123,
                                "end_time" to 1721624074123,
                                "stage" to 1
                            )
                        )
                    ),
                    HealthDataType.SLEEP_STAGE
                ),
                Arguments.of(
                    listOf(
                        HealthData(
                            mapOf(
                                "start_time" to 1721624074123,
                                "end_time" to 1721624074123,
                                "count" to 3.14,
                                "calorie" to 3.14,
                                "distance" to 3.14,
                                "speed" to 3.14
                            )
                        )
                    ),
                    HealthDataType.STEPS
                ),
                Arguments.of(
                    listOf(
                        HealthData(
                            mapOf(
                                "start_time" to 1721624074123,
                                "end_time" to 1721624074123,
                                "calories" to 3.14
                            )
                        )
                    ),
                    HealthDataType.TOTAL_CALORIES_BURNED
                ),
                Arguments.of(
                    listOf(
                        HealthData(
                            mapOf(
                                "x" to 3.14,
                                "y" to 3.14,
                                "z" to 3.14,
                                "timestamp" to 1721624074123
                            )
                        )
                    ),
                    HealthDataType.WEAR_ACCELEROMETER
                ),
                Arguments.of(
                    listOf(
                        HealthData(
                            mapOf(
                                "charging" to true,
                                "percentage" to 3.14,
                                "timestamp" to 1721624074123
                            )
                        )
                    ),
                    HealthDataType.WEAR_BATTERY
                ),
                Arguments.of(
                    listOf(
                        HealthData(
                            mapOf(
                                "basalMetabolicRate" to 3.14,
                                "bodyFatMass" to 3.14,
                                "bodyFatRatio" to 3.14,
                                "fatFreeMass" to 3.14,
                                "fatFreeRatio" to 3.14,
                                "skeletalMuscleMass" to 3.14,
                                "skeletalMuscleRatio" to 3.14,
                                "totalBodyWater" to 3.14,
                                "measurementProgress" to 3.14,
                                "status" to 1,
                                "timestamp" to 1721624074123
                            )
                        )
                    ),
                    HealthDataType.WEAR_BIA
                ),
                Arguments.of(
                    listOf(
                        HealthData(
                            mapOf(
                                "sbp" to 80,
                                "dbp" to 80,
                                "timestamp" to 1721624074123
                            )
                        )
                    ),
                    HealthDataType.WEAR_BLOOD_PRESSURE
                ),
                Arguments.of(
                    listOf(
                        HealthData(
                            mapOf(
                                "timestamp" to 1721624074123
                            )
                        )
                    ),
                    HealthDataType.WEAR_ECG
                ),
                Arguments.of(
                    listOf(
                        HealthData(
                            mapOf(
                                "x" to 3.14,
                                "y" to 3.14,
                                "z" to 3.14,
                                "timestamp" to 1721624074123
                            )
                        )
                    ),
                    HealthDataType.WEAR_GYROSCOPE
                ),
                Arguments.of(
                    listOf(
                        HealthData(
                            mapOf(
                                "name" to "1",
                                "timestamp" to 1721624074123
                            )
                        )
                    ),
                    HealthDataType.WEAR_HEALTH_EVENT
                ),
                Arguments.of(
                    listOf(
                        HealthData(
                            mapOf(
                                "value" to 80,
                                "heartRateStatus" to 1,
                                "ibiList" to "[1063, 946]",
                                "ibiStatusList" to "[-1, -1]",
                                "timestamp" to 1721624074123
                            )
                        )
                    ),
                    HealthDataType.WEAR_HEART_RATE
                ),
                Arguments.of(
                    listOf(
                        HealthData(
                            mapOf(
                                "ppg" to 80,
                                "timestamp" to 1721624074123
                            )
                        )
                    ),
                    HealthDataType.WEAR_PPG_GREEN
                ),
                Arguments.of(
                    listOf(
                        HealthData(
                            mapOf(
                                "ppg" to 80,
                                "timestamp" to 1721624074123
                            )
                        )
                    ),
                    HealthDataType.WEAR_PPG_IR
                ),
                Arguments.of(
                    listOf(
                        HealthData(
                            mapOf(
                                "ppg" to 80,
                                "timestamp" to 1721624074123
                            )
                        )
                    ),
                    HealthDataType.WEAR_PPG_RED
                ),
                Arguments.of(
                    listOf(
                        HealthData(
                            mapOf(
                                "heartRate" to 80,
                                "spO2" to 80,
                                "status" to 1,
                                "timestamp" to 1721624074123
                            )
                        )
                    ),
                    HealthDataType.WEAR_SPO2
                ),
                Arguments.of(
                    listOf(
                        HealthData(
                            mapOf(
                                "sweatLoss" to 3.14,
                                "status" to 80,
                                "timestamp" to 1721624074123
                            )
                        )
                    ),
                    HealthDataType.WEAR_SWEAT_LOSS
                ),
                Arguments.of(
                    listOf(
                        HealthData(
                            mapOf(
                                "start_time" to 1721624074123,
                                "weight" to 3.14
                            )
                        )
                    ),
                    HealthDataType.WEIGHT
                )
            )
        }
    }
}
