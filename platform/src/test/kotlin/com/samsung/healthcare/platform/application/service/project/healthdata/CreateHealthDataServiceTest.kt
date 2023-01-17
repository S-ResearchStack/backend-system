package com.samsung.healthcare.platform.application.service.project.healthdata

import com.fasterxml.jackson.databind.ObjectMapper
import com.samsung.healthcare.platform.POSITIVE_TEST
import com.samsung.healthcare.platform.application.port.input.project.healthdata.SaveHealthDataCommand
import com.samsung.healthcare.platform.application.port.output.project.healthdata.SaveHealthDataPort
import com.samsung.healthcare.platform.domain.project.UserProfile.UserId
import com.samsung.healthcare.platform.domain.project.healthdata.HealthData
import com.samsung.healthcare.platform.domain.project.healthdata.HeartRate
import io.mockk.coJustRun
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.Instant

internal class CreateHealthDataServiceTest {
    private val saveHealthDataPort = mockk<SaveHealthDataPort>()
    private val objectMapper = mockk<ObjectMapper>()
    private val createHealthDataService = CreateHealthDataService(saveHealthDataPort, objectMapper)

    @Test
    @Tag(POSITIVE_TEST)
    fun `should save all provided HealthData records`() = runTest {
        val userId = UserId.from("UserId")

        val healthDataMap1 = mapOf<String, Any>(
            "id" to HealthData.HealthDataId.from(1),
            "time" to Instant.now(),
            "bpm" to "84".toLong()
        )
        val healthDataMap2 = mapOf<String, Any>(
            "id" to HealthData.HealthDataId.from(2),
            "time" to Instant.now(),
            "bpm" to "80".toLong()
        )
        val healthDataMap3 = mapOf<String, Any>(
            "id" to HealthData.HealthDataId.from(3),
            "time" to Instant.now(),
            "bpm" to "73".toLong()
        )
        val heartRate1 = toHeartRate(healthDataMap1)
        val heartRate2 = toHeartRate(healthDataMap2)
        val heartRate3 = toHeartRate(healthDataMap3)

        val saveHealthDataCommand = SaveHealthDataCommand(
            HealthData.HealthDataType.HEART_RATE,
            listOf(healthDataMap1, healthDataMap2, healthDataMap3)
        )

        every {
            objectMapper.convertValue(any(), HealthData.HealthDataType.HEART_RATE.dataClass)
        } answers {
            toHeartRate(firstArg())
        }

        coJustRun {
            saveHealthDataPort.save(
                userId,
                HealthData.HealthDataType.HEART_RATE,
                listOf(heartRate1, heartRate2, heartRate3)
            )
        }

        createHealthDataService.saveHealthData(userId, saveHealthDataCommand)

        coVerifySequence {
            objectMapper.convertValue(healthDataMap1, HealthData.HealthDataType.HEART_RATE.dataClass)
            objectMapper.convertValue(healthDataMap2, HealthData.HealthDataType.HEART_RATE.dataClass)
            objectMapper.convertValue(healthDataMap3, HealthData.HealthDataType.HEART_RATE.dataClass)
            saveHealthDataPort.save(
                userId,
                HealthData.HealthDataType.HEART_RATE,
                listOf(heartRate1, heartRate2, heartRate3)
            )
        }
    }

    private fun toHeartRate(dataMap: Map<String, Any>): HeartRate =
        HeartRate(
            dataMap["id"] as HealthData.HealthDataId,
            dataMap["time"] as Instant,
            dataMap["bpm"] as Long
        )
}
