package com.samsung.healthcare.dataqueryservice.application.service

import com.samsung.healthcare.dataqueryservice.NEGATIVE_TEST
import com.samsung.healthcare.dataqueryservice.POSITIVE_TEST
import com.samsung.healthcare.dataqueryservice.application.port.output.QueryDataPort
import com.samsung.healthcare.dataqueryservice.application.port.output.QueryDataResult
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.sql.Timestamp
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID
import kotlin.random.Random

internal class HealthDataQueryServiceTest {
    private val queryDataPort = mockk<QueryDataPort>()

    private val healthDataQueryService = HealthDataQueryService(queryDataPort)

    private val testProjectId = "test-project-id"
    private val testAccountId = "test-account-id"

    private val testUserIds = listOf(
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString()
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `fetchLatestAverageHR should return averageHR of users`() {

        every {
            queryDataPort.executeQuery(testProjectId, testAccountId, any(), testUserIds)
        } returns QueryDataResult(
            emptyList(),
            testUserIds.map {
                mapOf(
                    USER_ID_COLUMN to it,
                    AVERAGE_HR_COLUMN to Random.nextDouble(30.0, 180.0)
                )
            }
        )

        val averageHRs = healthDataQueryService.fetchLatestAverageHR(testProjectId, testUserIds, testAccountId)
        assertTrue(averageHRs.size <= testUserIds.size)
        averageHRs.forEach {
            assertTrue(testUserIds.contains(it.userId))
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `fetchLatestAverageHR should throw IllegalArgumentException when users is empty`() {
        assertThrows<IllegalArgumentException> {
            healthDataQueryService.fetchLatestAverageHR(
                testProjectId,
                emptyList(),
                testAccountId
            )
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `fetchLatestAverageBP should return averageHR of users`() {
        every {
            queryDataPort.executeQuery(testProjectId, testAccountId, any(), testUserIds)
        } returns QueryDataResult(
            emptyList(),
            testUserIds.map {
                mapOf(
                    USER_ID_COLUMN to it,
                    AVERAGE_BP_SYSTOLIC_COLUMN to Random.nextDouble(50.0, 100.0),
                    AVERAGE_BP_DIASTOLIC_COLUMN to Random.nextDouble(100.0, 180.0)
                )
            }
        )

        val averageBPs = healthDataQueryService.fetchLatestAverageBP(testProjectId, testUserIds, testAccountId)
        assertTrue(averageBPs.size <= testUserIds.size)
        averageBPs.forEach {
            assertTrue(testUserIds.contains(it.userId))
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `fetchLatestAverageBP should throw IllegalArgumentException when users is empty`() {
        assertThrows<IllegalArgumentException> {
            healthDataQueryService.fetchLatestAverageBP(
                testProjectId,
                emptyList(),
                testAccountId
            )
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `fetchLatestTotalStep should return totalStep of users`() {
        every {
            queryDataPort.executeQuery(testProjectId, testAccountId, any(), testUserIds)
        } returns QueryDataResult(
            emptyList(),
            testUserIds.map {
                mapOf(
                    USER_ID_COLUMN to it,
                    LAST_TOTAL_STEP_COLUMN to Random.nextLong(1000)
                )
            }
        )

        val totalSteps = healthDataQueryService.fetchLatestTotalStep(testProjectId, testUserIds, testAccountId)
        assertTrue(totalSteps.size <= testUserIds.size)
        totalSteps.forEach {
            assertTrue(testUserIds.contains(it.userId))
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `fetchLatestTotalStep should throw IllegalArgumentException when users is empty`() {
        assertThrows<IllegalArgumentException> {
            healthDataQueryService.fetchLatestTotalStep(
                testProjectId,
                emptyList(),
                testAccountId
            )
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `fetchAverageSleep should return totalStep of users`() {
        every {
            queryDataPort.executeQuery(testProjectId, testAccountId, any(), testUserIds)
        } returns QueryDataResult(
            emptyList(),
            testUserIds.map {
                mapOf(
                    USER_ID_COLUMN to it,
                    AVERAGE_SLEEP_COLUMN to Random.nextDouble(1.0, 1000.0)
                )
            }
        )

        val averageSleeps = healthDataQueryService.fetchAverageSleep(testProjectId, testUserIds, testAccountId)
        assertTrue(averageSleeps.size <= testUserIds.size)
        averageSleeps.forEach {
            assertTrue(testUserIds.contains(it.userId))
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `fetchAverageSleep should throw IllegalArgumentException when users is empty`() {
        assertThrows<IllegalArgumentException> {
            healthDataQueryService.fetchAverageSleep(
                testProjectId,
                emptyList(),
                testAccountId
            )
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `fetchHeartRate should return heart rate of all users`() {
        val to = Instant.now()
        val from = to.minus(1, ChronoUnit.DAYS)
        every {
            queryDataPort.executeQuery(testProjectId, testAccountId, any(), any())
        } returns QueryDataResult(
            emptyList(),
            testUserIds.map {
                mapOf(
                    USER_ID_COLUMN to it,
                    TIME_COLUMN to Timestamp.from(to.minusSeconds(Random.nextLong(1, 1000))),
                    BPM_COLUMN to Random.nextLong(30, 180)
                )
            }
        )

        val heartRates = healthDataQueryService.fetchHeartRate(testProjectId, from, to, testAccountId)
        assertTrue(heartRates.isNotEmpty())
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `fetchAverageSleep should throw IllegalArgumentException when from-time is not before to-time`() {
        val to = Instant.now()
        val from = to.plus(1, ChronoUnit.DAYS)
        assertThrows<IllegalArgumentException> {
            healthDataQueryService.fetchHeartRate(testProjectId, from, to, testAccountId)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `fetchAverageHR should return heart rate of all users`() {
        val to = Instant.now()
        val from = to.minus(1, ChronoUnit.DAYS)
        every {
            queryDataPort.executeQuery(testProjectId, testAccountId, any(), any())
        } returns QueryDataResult(
            emptyList(),
            testUserIds.map {
                mapOf(
                    USER_ID_COLUMN to it,
                    AVERAGE_HR_COLUMN to Random.nextDouble(30.0, 180.0)
                )
            }
        )

        val heartRates = healthDataQueryService.fetchAverageHR(testProjectId, from, to, testAccountId)
        assertTrue(heartRates.isNotEmpty())
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `fetchAverageHR should throw IllegalArgumentException when from-time is not before to-time`() {
        val to = Instant.now()
        val from = to.plus(1, ChronoUnit.DAYS)
        assertThrows<IllegalArgumentException> {
            healthDataQueryService.fetchAverageHR(testProjectId, from, to, testAccountId)
        }
    }
}
