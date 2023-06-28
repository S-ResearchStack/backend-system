package com.samsung.healthcare.dataqueryservice.application.service

import com.samsung.healthcare.dataqueryservice.application.port.input.AverageBG
import com.samsung.healthcare.dataqueryservice.application.port.input.AverageBP
import com.samsung.healthcare.dataqueryservice.application.port.input.AverageHR
import com.samsung.healthcare.dataqueryservice.application.port.input.AverageRR
import com.samsung.healthcare.dataqueryservice.application.port.input.AverageSPO2
import com.samsung.healthcare.dataqueryservice.application.port.input.AverageSleep
import com.samsung.healthcare.dataqueryservice.application.port.input.HealthDataQuery
import com.samsung.healthcare.dataqueryservice.application.port.input.HeartRate
import com.samsung.healthcare.dataqueryservice.application.port.input.HeartRateData
import com.samsung.healthcare.dataqueryservice.application.port.input.TotalStep
import com.samsung.healthcare.dataqueryservice.application.port.output.QueryDataPort
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant

@Service
class HealthDataQueryService(
    private val queryDataPort: QueryDataPort,
) : HealthDataQuery {
    override fun fetchLatestAverageHR(
        projectId: String,
        userIds: List<String>,
        accountId: String,
    ): List<AverageHR> {
        require(userIds.isNotEmpty())

        return queryDataPort.executeQuery(
            projectId,
            accountId,
            makeAverageHRQuery(userIds.size),
            userIds,
        ).data.map {
            it.toAverageHR()
        }
    }

    override fun fetchLatestAverageBG(
        projectId: String,
        userIds: List<String>,
        accountId: String
    ): List<AverageBG> {
        require(userIds.isNotEmpty())

        return queryDataPort.executeQuery(
            projectId,
            accountId,
            makeAverageBGQuery(userIds.size),
            userIds,
        ).data.map {
            it.toAverageBG()
        }
    }

    override fun fetchLatestAverageRR(
        projectId: String,
        userIds: List<String>,
        accountId: String
    ): List<AverageRR> {
        require(userIds.isNotEmpty())

        return queryDataPort.executeQuery(
            projectId,
            accountId,
            makeAverageRRQuery(userIds.size),
            userIds,
        ).data.map {
            it.toAverageRR()
        }
    }

    override fun fetchLatestAverageSPO2(
        projectId: String,
        userIds: List<String>,
        accountId: String,
    ): List<AverageSPO2> {
        require(userIds.isNotEmpty())

        return queryDataPort.executeQuery(
            projectId,
            accountId,
            makeAverageSPO2Query(userIds.size),
            userIds,
        ).data.map {
            it.toAverageSPO2()
        }
    }

    override fun fetchLatestAverageBP(
        projectId: String,
        userIds: List<String>,
        accountId: String,
    ): List<AverageBP> {
        require(userIds.isNotEmpty())

        return queryDataPort.executeQuery(
            projectId,
            accountId,
            makeAverageBPQuery(userIds.size),
            userIds,
        ).data.map {
            it.toAverageBP()
        }
    }

    override fun fetchLatestTotalStep(
        projectId: String,
        userIds: List<String>,
        accountId: String,
    ): List<TotalStep> {
        require(userIds.isNotEmpty())

        return queryDataPort.executeQuery(
            projectId,
            accountId,
            makeQueryToGetStepOfUsers(userIds.size),
            userIds,
        ).data.map {
            it.toLatestTotalStep()
        }
    }

    override fun fetchAverageSleep(projectId: String, userIds: List<String>, accountId: String): List<AverageSleep> {
        require(userIds.isNotEmpty())

        return queryDataPort.executeQuery(
            projectId,
            accountId,
            makeQueryToGetSleepOfUsers(userIds.size),
            userIds,
        ).data.map {
            it.toAverageSleep()
        }
    }

    override fun fetchHeartRate(
        projectId: String,
        from: Instant,
        to: Instant,
        accountId: String,
    ): List<HeartRateData> {
        require(from.isBefore(to))

        return queryDataPort.executeQuery(
            projectId,
            accountId,
            GET_HEART_RATE_QUERY,
            listOf(Timestamp.from(from), Timestamp.from(to)),
        ).data.groupBy { it[USER_ID_COLUMN] as String }
            .map { (userId, rows) ->
                HeartRateData(userId, rows.map { it.toHeartRate() })
            }
    }

    override fun fetchAverageHR(projectId: String, from: Instant, to: Instant, accountId: String): List<AverageHR> {
        require(from.isBefore(to))

        return queryDataPort.executeQuery(
            projectId,
            accountId,
            AVERAGE_HEART_RATE_QUERY,
            listOf(Timestamp.from(from), Timestamp.from(to)),
        ).data.map {
            it.toAverageHR()
        }
    }

    private fun Map<String, Any?>.toAverageHR() =
        AverageHR(
            this[USER_ID_COLUMN] as String,
            (this[AVERAGE_HR_COLUMN] as Double),
        )

    private fun Map<String, Any?>.toAverageBG() =
        AverageBG(
            this[USER_ID_COLUMN] as String,
            (this[AVERAGE_BG_COLUMN] as Double),
        )

    private fun Map<String, Any?>.toAverageRR() =
        AverageRR(
            this[USER_ID_COLUMN] as String,
            (this[AVERAGE_RR_COLUMN] as Double),
        )

    private fun Map<String, Any?>.toAverageSPO2() =
        AverageSPO2(
            this[USER_ID_COLUMN] as String,
            this[AVERAGE_SPO2_COLUMN].toString().toFloat(),
        )

    private fun Map<String, Any?>.toAverageBP() =
        AverageBP(
            this[USER_ID_COLUMN] as String,
            (this[AVERAGE_BP_SYSTOLIC_COLUMN] as Double),
            (this[AVERAGE_BP_DIASTOLIC_COLUMN] as Double),
        )

    private fun Map<String, Any?>.toAverageSleep() =
        AverageSleep(
            this[USER_ID_COLUMN] as String,
            (this[AVERAGE_SLEEP_COLUMN] as Double),
        )

    private fun Map<String, Any?>.toLatestTotalStep() =
        TotalStep(
            this[USER_ID_COLUMN] as String,
            (this[LAST_TOTAL_STEP_COLUMN] as Long).toInt(),
        )

    private fun Map<String, Any?>.toHeartRate() =
        HeartRate(
            (this[TIME_COLUMN] as Timestamp).toString(),
            (this[BPM_COLUMN] as Long).toInt(),
        )
}
