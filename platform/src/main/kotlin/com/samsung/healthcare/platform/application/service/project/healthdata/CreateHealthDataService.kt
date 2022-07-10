package com.samsung.healthcare.platform.application.service.project.healthdata

import com.fasterxml.jackson.databind.ObjectMapper
import com.samsung.healthcare.platform.application.port.input.project.healthdata.SaveHealthDataCommand
import com.samsung.healthcare.platform.application.port.input.project.healthdata.SaveHealthDataUseCase
import com.samsung.healthcare.platform.application.port.output.project.healthdata.SaveHealthDataPort
import com.samsung.healthcare.platform.domain.project.UserProfile.UserId
import org.springframework.stereotype.Service

@Service
class CreateHealthDataService(
    private val saveHealthDataPort: SaveHealthDataPort,
    private val objectMapper: ObjectMapper,
) : SaveHealthDataUseCase {
    /**
     * Saves provided [HealthData][com.samsung.healthcare.platform.domain.project.healthdata.HealthData] and associates it with the given [UserId].
     *
     * @param userId The id of the User providing the HealthData.
     * @param command  [SaveHealthDataCommand] with request parameters.
     */
    @Suppress("UNCHECKED_CAST")
    override suspend fun saveHealthData(userId: UserId, command: SaveHealthDataCommand) {
        saveHealthDataPort.save(
            userId,
            command.type,
            command.data.map {
                objectMapper.convertValue(it, command.type.dataClass)
            }
        )
    }
}
