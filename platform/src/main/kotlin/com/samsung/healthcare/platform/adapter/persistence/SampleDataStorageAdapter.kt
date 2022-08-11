package com.samsung.healthcare.platform.adapter.persistence

import com.samsung.healthcare.platform.application.exception.NotFoundException
import com.samsung.healthcare.platform.application.port.output.LoadSampleDataCommand
import com.samsung.healthcare.platform.application.port.output.LoadSampleDataPort
import com.samsung.healthcare.platform.domain.healthdata.HealthData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Component

@Component
class SampleDataStorageAdapter(
    private val repositoryLookup: HealthDataRepositoryLookup
) : LoadSampleDataPort {

    override suspend fun findByPeriod(command: LoadSampleDataCommand): Flow<HealthData> {
        val repo = (
            repositoryLookup.getRepository(command.type)
                ?: throw NotFoundException("The healthData type ${command.type} does not exist.")
            ) as SampleDataRepository

        return if (command.users.isEmpty()) repo.findByTimeBetween(command.startDate, command.endDate)
        else {
            repo.findByUserIdInAndTimeBetween(
                command.users.map { it.value },
                command.startDate,
                command.endDate
            )
        }.map { it.toDomain() }
    }
}
