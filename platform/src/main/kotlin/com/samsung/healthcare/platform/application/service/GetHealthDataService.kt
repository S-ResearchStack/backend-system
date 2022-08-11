package com.samsung.healthcare.platform.application.service

import com.samsung.healthcare.platform.application.exception.BadRequestException
import com.samsung.healthcare.platform.application.exception.NotImplementedException
import com.samsung.healthcare.platform.application.port.input.GetHealthDataCommand
import com.samsung.healthcare.platform.application.port.input.GetHealthDataQuery
import com.samsung.healthcare.platform.application.port.output.LoadIntervalDataPort
import com.samsung.healthcare.platform.application.port.output.LoadSampleDataCommand
import com.samsung.healthcare.platform.application.port.output.LoadSampleDataPort
import com.samsung.healthcare.platform.domain.healthdata.HealthData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.merge
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class GetHealthDataService(
    private val loadSampleDataPort: LoadSampleDataPort,
    private val loadIntervalDataPort: LoadIntervalDataPort,
) : GetHealthDataQuery {
    companion object {
        const val defaultDateSearchRangeWeeks = 4L
    }

    override suspend fun findByPeriod(command: GetHealthDataCommand): Flow<HealthData> {
        var flowList: Flow<HealthData> = flowOf()

        val (startDate, endDate) = setDefaultDateSearchRange(command.startDate, command.endDate)

        command.types.forEach {
            when (it) {
                HealthData.HealthDataType.HEART_RATE ->
                    flowList = merge(
                        flowList,
                        loadSampleDataPort.findByPeriod(
                            LoadSampleDataCommand(
                                it,
                                command.users,
                                startDate,
                                endDate
                            )
                        )
                    )
                else -> throw NotImplementedException("$it is not implemented yet")
            }
        }
        return flowList
    }

    private fun setDefaultDateSearchRange(
        startDateCommand: LocalDateTime?,
        endDateCommand: LocalDateTime?
    ): Pair<LocalDateTime, LocalDateTime> {

        if (startDateCommand == null && endDateCommand == null) {
            throw BadRequestException("Provide at least one of startDate or endDate")
        }

        val startDate: LocalDateTime = startDateCommand ?: endDateCommand!!.minusWeeks(defaultDateSearchRangeWeeks)
        val endDate: LocalDateTime = endDateCommand ?: startDateCommand!!.plusWeeks(defaultDateSearchRangeWeeks)

        return startDate to endDate
    }
}
