package com.samsung.healthcare.dataqueryservice.application.port.input

import java.time.Instant

data class AverageHR(
    val userId: String,
    val average: Double,
)

data class AverageBG(
    val userId: String,
    val average: Double,
)

data class AverageRR(
    val userId: String,
    val average: Double,
)

data class AverageSPO2(
    val userId: String,
    val average: Float,
)

data class TotalStep(
    val userId: String,
    val total: Int,
)

data class AverageBP(
    val userId: String,
    val avgSystolic: Double,
    val avgDiastolic: Double,
)

data class AverageSleep(
    val userId: String,
    val average: Double,
)

data class HealthData(
    val heartrates: List<HeartRate>,
)

data class HeartRateData(
    val userId: String,
    val heartrates: List<HeartRate>,
)

data class HeartRate(
    val time: String,
    val bpm: Int,
)

interface HealthDataQuery {
    fun fetchLatestAverageHR(projectId: String, userIds: List<String>, accountId: String): List<AverageHR>

    fun fetchLatestAverageBG(projectId: String, userIds: List<String>, accountId: String): List<AverageBG>

    fun fetchLatestAverageRR(projectId: String, userIds: List<String>, accountId: String): List<AverageRR>

    fun fetchLatestAverageSPO2(projectId: String, userIds: List<String>, accountId: String): List<AverageSPO2>

    fun fetchLatestAverageBP(projectId: String, userIds: List<String>, accountId: String): List<AverageBP>

    fun fetchLatestTotalStep(projectId: String, userIds: List<String>, accountId: String): List<TotalStep>

    fun fetchAverageSleep(projectId: String, userIds: List<String>, accountId: String): List<AverageSleep>

    fun fetchHeartRate(projectId: String, from: Instant, to: Instant, accountId: String): List<HeartRateData>

    fun fetchAverageHR(projectId: String, from: Instant, to: Instant, accountId: String): List<AverageHR>
}
