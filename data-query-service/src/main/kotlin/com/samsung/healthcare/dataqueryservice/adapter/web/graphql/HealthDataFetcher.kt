package com.samsung.healthcare.dataqueryservice.adapter.web.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import com.netflix.graphql.dgs.context.DgsContext
import com.samsung.healthcare.dataqueryservice.adapter.web.graphql.DgsContextExtension.getAccount
import com.samsung.healthcare.dataqueryservice.adapter.web.graphql.DgsContextExtension.getProjectId
import com.samsung.healthcare.dataqueryservice.application.port.input.AverageBG
import com.samsung.healthcare.dataqueryservice.application.port.input.AverageBP
import com.samsung.healthcare.dataqueryservice.application.port.input.AverageHR
import com.samsung.healthcare.dataqueryservice.application.port.input.AverageRR
import com.samsung.healthcare.dataqueryservice.application.port.input.AverageSPO2
import com.samsung.healthcare.dataqueryservice.application.port.input.AverageSleep
import com.samsung.healthcare.dataqueryservice.application.port.input.HealthData
import com.samsung.healthcare.dataqueryservice.application.port.input.HealthDataQuery
import com.samsung.healthcare.dataqueryservice.application.port.input.HeartRate
import com.samsung.healthcare.dataqueryservice.application.port.input.HeartRateData
import com.samsung.healthcare.dataqueryservice.application.port.input.Pageable
import com.samsung.healthcare.dataqueryservice.application.port.input.ParticipantListColumn
import com.samsung.healthcare.dataqueryservice.application.port.input.Sort
import com.samsung.healthcare.dataqueryservice.application.port.input.TotalStep
import com.samsung.healthcare.dataqueryservice.application.port.input.User
import com.samsung.healthcare.dataqueryservice.application.port.input.UserDataQuery
import graphql.execution.DataFetcherResult
import graphql.schema.DataFetchingEnvironment
import java.time.Instant
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit.DAYS

@DgsComponent
class HealthDataFetcher(
    private val healthDataQuery: HealthDataQuery,
    private val userDataQuery: UserDataQuery,
) {
    private val emptyHealthStatusContext = HealthStatusContext()

    @DgsQuery
    fun healthDataOverview(
        dfe: DataFetchingEnvironment,
        @InputArgument offset: Int,
        @InputArgument limit: Int,
        @InputArgument orderByColumn: ParticipantListColumn?,
        @InputArgument orderBySort: Sort?,
        @InputArgument includeAttributes: List<String>?,
    ): DataFetcherResult<List<User>> {
        val pageable = Pageable(offset, limit)
        val context = DgsContext.from(dfe)
        val users = userDataQuery.fetchUsers(
            context.getProjectId(),
            orderByColumn ?: ParticipantListColumn.ID,
            orderBySort ?: Sort.ASC,
            pageable,
            includeAttributes ?: emptyList(),
            context.getAccount()
        )
        return DataFetcherResult.newResult<List<User>>()
            .data(users)
            .localContext(preloadData(dfe, users.map { it.userId }))
            .build()
    }

    @DgsQuery
    fun healthDataOverviewOfUser(
        dfe: DataFetchingEnvironment,
        @InputArgument userId: String,
        @InputArgument includeAttributes: List<String>?,
    ): DataFetcherResult<User?> {
        require(userId.isNotBlank())

        val result = DataFetcherResult.newResult<User>()
        val user = fetchUser(dfe, userId, includeAttributes ?: emptyList())
            ?: return result.build()

        return result.data(user)
            .localContext(preloadData(dfe, listOf(userId)))
            .build()
    }

    private fun preloadData(dfe: DataFetchingEnvironment, userIds: List<String>): HealthStatusContext {
        if (userIds.isEmpty()) return emptyHealthStatusContext
        return HealthStatusContext(
            userIdToHeartRate = preloadHeartRate(dfe, userIds),
            userIdToBloodGlucose = preloadBloodGlucose(dfe, userIds),
            userIdToRespiratoryRate = preloadRespiratoryRate(dfe, userIds),
            userIdToOxygenSaturation = preloadOxygenSaturation(dfe, userIds),
            userIdToBloodPressure = preloadBloodPressure(dfe, userIds),
            userIdToStep = preloadStep(dfe, userIds),
            userIdToSleep = preloadSleep(dfe, userIds)
        )
    }

    private fun preloadHeartRate(
        dfe: DataFetchingEnvironment,
        userIds: List<String>
    ): Map<String, AverageHR> {
        if (!dfe.selectionSet.contains("latestAverageHR"))
            return emptyMap()

        val context = DgsContext.from(dfe)
        return healthDataQuery.fetchLatestAverageHR(context.getProjectId(), userIds, context.getAccount())
            .associateBy { it.userId }
    }

    private fun preloadBloodGlucose(
        dfe: DataFetchingEnvironment,
        userIds: List<String>
    ): Map<String, AverageBG> {
        if (!dfe.selectionSet.contains("latestAverageBG"))
            return emptyMap()

        val context = DgsContext.from(dfe)
        return healthDataQuery.fetchLatestAverageBG(context.getProjectId(), userIds, context.getAccount())
            .associateBy { it.userId }
    }

    private fun preloadRespiratoryRate(
        dfe: DataFetchingEnvironment,
        userIds: List<String>
    ): Map<String, AverageRR> {
        if (!dfe.selectionSet.contains("latestAverageRR"))
            return emptyMap()

        val context = DgsContext.from(dfe)
        return healthDataQuery.fetchLatestAverageRR(context.getProjectId(), userIds, context.getAccount())
            .associateBy { it.userId }
    }

    private fun preloadOxygenSaturation(
        dfe: DataFetchingEnvironment,
        userIds: List<String>
    ): Map<String, AverageSPO2> {
        if (!dfe.selectionSet.contains("latestAverageSPO2"))
            return emptyMap()

        val context = DgsContext.from(dfe)
        return healthDataQuery.fetchLatestAverageSPO2(context.getProjectId(), userIds, context.getAccount())
            .associateBy { it.userId }
    }

    private fun preloadBloodPressure(
        dfe: DataFetchingEnvironment,
        userIds: List<String>
    ): Map<String, AverageBP> {
        if (!dfe.selectionSet.contains("latestAverageSystolicBP") &&
            !dfe.selectionSet.contains("latestAverageDiastolicBP")
        ) return emptyMap()

        val context = DgsContext.from(dfe)
        return healthDataQuery.fetchLatestAverageBP(context.getProjectId(), userIds, context.getAccount())
            .associateBy { it.userId }
    }

    private fun preloadStep(
        dfe: DataFetchingEnvironment,
        userIds: List<String>
    ): Map<String, TotalStep> {
        if (!dfe.selectionSet.contains("latestTotalStep"))
            return emptyMap()

        val context = DgsContext.from(dfe)
        return healthDataQuery.fetchLatestTotalStep(context.getProjectId(), userIds, context.getAccount())
            .associateBy { it.userId }
    }

    private fun preloadSleep(
        dfe: DataFetchingEnvironment,
        userIds: List<String>
    ): Map<String, AverageSleep> {
        if (!dfe.selectionSet.contains("averageSleep"))
            return emptyMap()

        val context = DgsContext.from(dfe)
        return healthDataQuery.fetchAverageSleep(context.getProjectId(), userIds, context.getAccount())
            .associateBy { it.userId }
    }

    private fun fetchUser(dfe: DataFetchingEnvironment, userId: String, includeUserProfiles: List<String>): User? {
        val context = DgsContext.from(dfe)
        return userDataQuery.fetchUsers(
            context.getProjectId(),
            listOf(userId),
            includeUserProfiles,
            context.getAccount()
        ).firstOrNull()
    }

    @DgsData(parentType = "HealthDataOverview", field = "latestAverageHR")
    fun latestAverageHR(dfe: DgsDataFetchingEnvironment): Double? {
        val user = dfe.getSource<User>()
        return dfe.getLocalContext<HealthStatusContext>()
            .userIdToHeartRate[user.userId]?.average
    }

    @DgsData(parentType = "HealthDataOverview", field = "latestAverageBG")
    fun latestAverageBG(dfe: DgsDataFetchingEnvironment): Double? {
        val user = dfe.getSource<User>()
        return dfe.getLocalContext<HealthStatusContext>()
            .userIdToBloodGlucose[user.userId]?.average
    }

    @DgsData(parentType = "HealthDataOverview", field = "latestAverageRR")
    fun latestAverageRR(dfe: DgsDataFetchingEnvironment): Double? {
        val user = dfe.getSource<User>()
        return dfe.getLocalContext<HealthStatusContext>()
            .userIdToRespiratoryRate[user.userId]?.average
    }

    @DgsData(parentType = "HealthDataOverview", field = "latestAverageSPO2")
    fun latestAverageSPO2(dfe: DgsDataFetchingEnvironment): Float? {
        val user = dfe.getSource<User>()
        return dfe.getLocalContext<HealthStatusContext>()
            .userIdToOxygenSaturation[user.userId]?.average
    }

    @DgsData(parentType = "HealthDataOverview", field = "latestAverageSystolicBP")
    fun latestAverageSystolicBP(dfe: DgsDataFetchingEnvironment): Double? {
        val user = dfe.getSource<User>()
        return dfe.getLocalContext<HealthStatusContext>()
            .userIdToBloodPressure[user.userId]?.avgSystolic
    }

    @DgsData(parentType = "HealthDataOverview", field = "latestAverageDiastolicBP")
    fun latestAverageDiastolicBP(dfe: DgsDataFetchingEnvironment): Double? {
        val user = dfe.getSource<User>()
        return dfe.getLocalContext<HealthStatusContext>()
            .userIdToBloodPressure[user.userId]?.avgDiastolic
    }

    @DgsData(parentType = "HealthDataOverview", field = "latestTotalStep")
    fun latestTotalStep(dfe: DgsDataFetchingEnvironment): Int? {
        val user = dfe.getSource<User>()
        return dfe.getLocalContext<HealthStatusContext>()
            .userIdToStep[user.userId]?.total
    }

    @DgsData(parentType = "HealthDataOverview", field = "averageSleep")
    fun averageSleep(dfe: DgsDataFetchingEnvironment): Double? {
        val user = dfe.getSource<User>()
        return dfe.getLocalContext<HealthStatusContext>()
            .userIdToSleep[user.userId]?.average
    }

    @DgsQuery
    fun rawHealthData(
        dfe: DataFetchingEnvironment,
        @InputArgument from: String?,
        @InputArgument to: String?,
        @InputArgument includeAttributes: List<String>?,
    ): DataFetcherResult<List<User>> {
        val (fromTime, toTime) = getPeriod(from, to)

        val context = DgsContext.from(dfe)
        val preloadData =
            if (dfe.selectionSet.contains("UserHealthData.healthData/HealthData.heartRates"))
                healthDataQuery.fetchHeartRate(context.getProjectId(), fromTime, toTime, context.getAccount())
                    .associateBy { it.userId }
            else null

        return fetchUserWithPreloadData(context, includeAttributes ?: emptyList(), preloadData)
    }

    @DgsQuery
    fun averageHealthData(
        dfe: DataFetchingEnvironment,
        @InputArgument from: String?,
        @InputArgument to: String?,
        @InputArgument includeAttributes: List<String>?
    ): DataFetcherResult<List<User>> {
        val (fromTime, toTime) = getPeriod(from, to)

        val context = DgsContext.from(dfe)
        val preloadData =
            if (dfe.selectionSet.contains("averageHR"))
                healthDataQuery.fetchAverageHR(context.getProjectId(), fromTime, toTime, context.getAccount())
                    .associateBy { it.userId }
            else null

        return fetchUserWithPreloadData(context, includeAttributes ?: emptyList(), preloadData)
    }

    @DgsData(parentType = "UserHealthData", field = "healthData")
    fun hearthData(dfe: DgsDataFetchingEnvironment): HealthData {
        val user = dfe.getSource<User>()
        return HealthData(
            dfe.getLocalContext<Map<String, HeartRateData>>()
                .get(user.userId)?.heartrates ?: emptyList()
        )
    }

    @DgsData(parentType = "HealthData", field = "heartRates")
    fun heartRates(dfe: DgsDataFetchingEnvironment): List<HeartRate> {
        return dfe.getSource<HealthData>().heartrates
    }

    @DgsData(parentType = "AverageHealthData", field = "averageHR")
    fun averageHR(dfe: DgsDataFetchingEnvironment): Double? {
        val user = dfe.getSource<User>()
        return dfe.getLocalContext<Map<String, AverageHR>>()
            .get(user.userId)?.average
    }

    private fun fetchUserWithPreloadData(
        context: DgsContext,
        includeAttributes: List<String>,
        preloadData: Any?
    ): DataFetcherResult<List<User>> =
        DataFetcherResult.newResult<List<User>>()
            .data(userDataQuery.fetchUsers(context.getProjectId(), includeAttributes, context.getAccount()))
            .localContext(preloadData)
            .build()

    private fun getPeriod(from: String?, to: String?): Pair<Instant, Instant> {
        val toTime = to?.toInstantTime() ?: Instant.now()
        val fromTime = from?.toInstantTime() ?: toTime.minus(1, DAYS)

        require(fromTime.isBefore(toTime))

        return Pair(fromTime, toTime)
    }

    private fun String.toInstantTime() =
        try {
            Instant.parse(this)
        } catch (_: DateTimeParseException) {
            throw IllegalArgumentException()
        }

    private data class HealthStatusContext(
        val userIdToHeartRate: Map<String, AverageHR> = emptyMap(),
        val userIdToBloodGlucose: Map<String, AverageBG> = emptyMap(),
        val userIdToRespiratoryRate: Map<String, AverageRR> = emptyMap(),
        val userIdToOxygenSaturation: Map<String, AverageSPO2> = emptyMap(),
        val userIdToBloodPressure: Map<String, AverageBP> = emptyMap(),
        val userIdToStep: Map<String, TotalStep> = emptyMap(),
        val userIdToSleep: Map<String, AverageSleep> = emptyMap()
    )
}
