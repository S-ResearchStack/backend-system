package com.samsung.healthcare.platform.adapter.persistence.project.healthdata

import com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata.toEntity
import com.samsung.healthcare.platform.application.port.output.project.healthdata.SaveHealthDataPort
import com.samsung.healthcare.platform.domain.project.healthdata.HealthData
import com.samsung.healthcare.platform.domain.project.healthdata.HealthData.HealthDataType
import com.samsung.healthcare.platform.domain.project.UserProfile.UserId
import kotlinx.coroutines.reactor.asFlux
import org.springframework.stereotype.Component
import kotlin.coroutines.coroutineContext

@Component
class SaveHealthDataStorageAdapter(
    private val repositoryLookup: HealthDataRepositoryLookup
) : SaveHealthDataPort {

    override suspend fun save(userId: UserId, type: HealthDataType, data: List<HealthData>) {
        if (data.isEmpty()) return

        repositoryLookup.getRepository(type)
            ?.saveAll(data.map { it.toEntity(userId) })
            ?.asFlux(coroutineContext)
            ?.doOnError { } // TODO logging }
            ?.subscribe()
    }
}
