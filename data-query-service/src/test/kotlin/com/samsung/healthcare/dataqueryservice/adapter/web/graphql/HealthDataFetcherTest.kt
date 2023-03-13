package com.samsung.healthcare.dataqueryservice.adapter.web.graphql

import com.netflix.graphql.dgs.DgsQueryExecutor
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration
import com.ninjasquad.springmockk.MockkBean
import com.samsung.healthcare.dataqueryservice.NEGATIVE_TEST
import com.samsung.healthcare.dataqueryservice.POSITIVE_TEST
import com.samsung.healthcare.dataqueryservice.adapter.web.PROJECT_ID
import com.samsung.healthcare.dataqueryservice.adapter.web.exception.DataFetchingExceptionHandler
import com.samsung.healthcare.dataqueryservice.application.context.AuthContext
import com.samsung.healthcare.dataqueryservice.application.port.input.AverageBP
import com.samsung.healthcare.dataqueryservice.application.port.input.AverageHR
import com.samsung.healthcare.dataqueryservice.application.port.input.AverageSleep
import com.samsung.healthcare.dataqueryservice.application.port.input.HealthDataQuery
import com.samsung.healthcare.dataqueryservice.application.port.input.HeartRate
import com.samsung.healthcare.dataqueryservice.application.port.input.HeartRateData
import com.samsung.healthcare.dataqueryservice.application.port.input.TotalStep
import com.samsung.healthcare.dataqueryservice.application.port.input.User
import com.samsung.healthcare.dataqueryservice.application.port.input.UserDataQuery
import graphql.ExecutionResult
import io.mockk.Called
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders

@SpringBootTest(classes = [DgsAutoConfiguration::class, HealthDataFetcher::class, DataFetchingExceptionHandler::class])
internal class HealthDataFetcherTest {
    @MockkBean
    lateinit var healthDataQuery: HealthDataQuery
    @MockkBean
    lateinit var userDataQuery: UserDataQuery

    @Autowired
    lateinit var dgsQueryExecutor: DgsQueryExecutor

    @Test
    @Tag(POSITIVE_TEST)
    fun `healthDataOverview should return proper data`() {
        val expectedResult = listOf(
            "u1,t1,1.0,121.0,81.0,1.0,1.0",
            "u2,t2,2.0,122.0,82.0,2.0,2.0",
            "u3,t3,3.0,123.0,83.0,3.0,3.0"
        )

        every { userDataQuery.fetchUsers(any(), any(), any(), any(), any(), any()) } returns listOf(
            User("u1", listOf(), "t1"),
            User("u2", listOf(), "t2"),
            User("u3", listOf(), "t3"),
        )
        every { healthDataQuery.fetchLatestAverageHR(any(), any(), any()) } returns listOf(
            AverageHR("u1", 1.0),
            AverageHR("u2", 2.0),
            AverageHR("u3", 3.0),
        )
        every { healthDataQuery.fetchLatestAverageBP(any(), any(), any()) } returns listOf(
            AverageBP("u1", 121.0, 81.0),
            AverageBP("u2", 122.0, 82.0),
            AverageBP("u3", 123.0, 83.0),
        )
        every { healthDataQuery.fetchLatestTotalStep(any(), any(), any()) } returns listOf(
            TotalStep("u1", 1),
            TotalStep("u2", 2),
            TotalStep("u3", 3),
        )
        every { healthDataQuery.fetchAverageSleep(any(), any(), any()) } returns listOf(
            AverageSleep("u1", 1.0),
            AverageSleep("u2", 2.0),
            AverageSleep("u3", 3.0),
        )

        val result = executeGraphQLQuery(
            """
                {
                    healthDataOverview(
                        offset: 0,
                        limit: 10,
                        includeAttributes: ["age", "email"]
                    ) {
                        userId
                        lastSyncTime
                        profiles{ key value }
                        latestAverageHR
                        latestAverageSystolicBP
                        latestAverageDiastolicBP
                        averageSleep
                        latestTotalStep
                    }
                }
            """.trimIndent()
        )

        assertTrue(result.errors.isEmpty())
        val data = result.getData<Map<String, List<Map<String, Any>>>>()["healthDataOverview"] ?: fail("")
        assertEquals(
            expectedResult,
            data.map {
                "${it["userId"]},${it["lastSyncTime"]},${it["latestAverageHR"]},${it["latestAverageSystolicBP"]}," +
                    "${it["latestAverageDiastolicBP"]},${it["averageSleep"]},${it["latestTotalStep"]}"
            }
        )
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @CsvSource("-1,-1", "-1,1", "0,0", "0,101")
    fun `healthDataOverview should throw an exception when it receives out of range value of offset or limit`(
        offset: Int,
        limit: Int
    ) {
        val result = executeGraphQLQuery(
            """
                {
                    healthDataOverview(
                        offset: $offset,
                        limit: $limit,
                    ) { userId }
                }
            """.trimIndent()
        )

        println("[error]!" + result.errors)
        assertTrue(!result.errors.isEmpty())

        verify {
            userDataQuery wasNot Called
            healthDataQuery wasNot Called
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `healthDataOverviewOfUser should return proper data`() {
        val expectedResult = "u1,t1,1.0,121.0,81.0,1.0,1.0"

        every { userDataQuery.fetchUsers(any(), any(), any(), any()) } returns listOf(
            User("u1", listOf(), "t1"),
        )
        every { healthDataQuery.fetchLatestAverageHR(any(), any(), any()) } returns listOf(
            AverageHR("u1", 1.0),
        )
        every { healthDataQuery.fetchLatestAverageBP(any(), any(), any()) } returns listOf(
            AverageBP("u1", 121.0, 81.0),
        )
        every { healthDataQuery.fetchLatestTotalStep(any(), any(), any()) } returns listOf(
            TotalStep("u1", 1),
        )
        every { healthDataQuery.fetchAverageSleep(any(), any(), any()) } returns listOf(
            AverageSleep("u1", 1.0),
        )

        val result = executeGraphQLQuery(
            """
                {
                    healthDataOverviewOfUser(
                        userId: "u1"
                        includeAttributes: ["age", "email"]
                    ) {
                        userId
                        lastSyncTime
                        profiles{ key value }
                        latestAverageHR
                        latestAverageSystolicBP
                        latestAverageDiastolicBP
                        averageSleep
                        latestTotalStep
                    }
                }
            """.trimIndent()
        )

        assertTrue(result.errors.isEmpty())
        val data = result.getData<Map<String, Map<String, Any>>>()["healthDataOverviewOfUser"] ?: fail("")
        assertEquals(
            expectedResult,
            "${data["userId"]},${data["lastSyncTime"]},${data["latestAverageHR"]},${data["latestAverageSystolicBP"]}," +
                "${data["latestAverageDiastolicBP"]},${data["averageSleep"]},${data["latestTotalStep"]}"
        )
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `rawHealthData should return proper data`() {
        val expectedResult = listOf(
            "u1,[],{heartRates=[{time=t1, bpm=1}]}",
            "u2,[],{heartRates=[{time=t1, bpm=1}, {time=t2, bpm=2}]}",
        )

        every { userDataQuery.fetchUsers(any(), any(), any()) } returns listOf(
            User("u1", listOf(), "t1"),
            User("u2", listOf(), "t2"),
        )
        every { healthDataQuery.fetchHeartRate(any(), any(), any(), any()) } returns listOf(
            HeartRateData("u1", listOf(HeartRate("t1", 1))),
            HeartRateData("u2", listOf(HeartRate("t1", 1), HeartRate("t2", 2)))
        )

        val result = executeGraphQLQuery(
            """
                {
                    rawHealthData(
                        from: "2021-01-04T00:00:00.000Z"
                        to: "2022-01-10T00:00:00.000Z"
                        includeAttributes: ["age", "email"]
                    ) {
                        userId
                        profiles{ key value }
                        healthData { heartRates { time bpm } }
                    }
                }
            """.trimIndent()
        )

        assertTrue(result.errors.isEmpty())
        val data = result.getData<Map<String, List<Map<String, Any>>>>()["rawHealthData"] ?: fail("")
        assertEquals(expectedResult, data.map { "${it["userId"]},${it["profiles"]},${it["healthData"]}" })
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `averageHealthData should return proper data`() {
        val expectedResult = listOf(
            "u1,t1,[],1.0",
            "u2,t2,[],2.0",
        )

        every { userDataQuery.fetchUsers(any(), any(), any()) } returns listOf(
            User("u1", listOf(), "t1"),
            User("u2", listOf(), "t2"),
        )
        every { healthDataQuery.fetchAverageHR(any(), any(), any(), any()) } returns listOf(
            AverageHR("u1", 1.0),
            AverageHR("u2", 2.0),
        )

        val result = executeGraphQLQuery(
            """
                {
                    averageHealthData(
                        from: "2021-01-04T00:00:00.000Z"
                        to: "2022-01-10T00:00:00.000Z"
                        includeAttributes: ["age", "email"]
                    ) {
                        userId
                        lastSyncTime
                        profiles{ key value }
                        averageHR
                    }
                }
            """.trimIndent()
        )

        println(result.errors)
        assertTrue(result.errors.isEmpty())
        val data = result.getData<Map<String, List<Map<String, Any>>>>()["averageHealthData"] ?: fail("")
        assertEquals(
            expectedResult,
            data.map { "${it["userId"]},${it["lastSyncTime"]},${it["profiles"]},${it["averageHR"]}" }
        )
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `healthDataOverview should throw an exception when orderByColumn is not valid`() {
        val result = executeGraphQLQuery(
            """
                {
                    healthDataOverview(
                        orderByColumn: "invalid-column"
                    ) { userId }
                }
            """.trimIndent()
        )

        assertTrue(result.errors.isNotEmpty())

        verify {
            userDataQuery wasNot Called
            healthDataQuery wasNot Called
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `healthDataOverview should throw an exception when orderBySort is not valid`() {
        val result = executeGraphQLQuery(
            """
                {
                    healthDataOverview(
                        orderBySort: "adesc"
                    ) { userId }
                }
            """.trimIndent()
        )

        assertTrue(result.errors.isNotEmpty())

        verify {
            userDataQuery wasNot Called
            healthDataQuery wasNot Called
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `healthDataOverviewOfUser should throw an exception when userIs is blank`() {
        val result = executeGraphQLQuery(
            """
                {
                    healthDataOverviewOfUser(
                        userId: "   "
                    ) {
                        userId
                        lastSyncTime
                    }
                }
            """.trimIndent()
        )

        assertTrue(result.errors.isNotEmpty())
    }

    private fun executeGraphQLQuery(query: String): ExecutionResult = dgsQueryExecutor.execute(
        query, emptyMap(), emptyMap(),
        HttpHeaders().apply {
            this[PROJECT_ID] = "test-project-id"
            this[AuthContext.ACCOUNT_ID_KEY_NAME] = "test-account-id"
        }
    )
}
