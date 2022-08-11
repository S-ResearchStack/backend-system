package com.samsung.healthcare.platform.adapter.persistence

import com.samsung.healthcare.platform.adapter.persistence.entity.healthdata.toEntity
import com.samsung.healthcare.platform.application.port.output.SaveHealthDataPort
import com.samsung.healthcare.platform.domain.User.UserId
import com.samsung.healthcare.platform.domain.healthdata.HealthData
import kotlinx.coroutines.reactor.asFlux
import org.springframework.stereotype.Component
import kotlin.coroutines.coroutineContext

@Component
class SaveHealthDataStorageAdapter(
    private val repositoryLookup: HealthDataRepositoryLookup
) : SaveHealthDataPort {

    override suspend fun save(userId: UserId, data: List<HealthData>) {
        if (data.isEmpty()) return

        repositoryLookup.getRepository(data[0].type)
            ?.saveAll(data.map { it.toEntity(userId) })
            ?.asFlux(coroutineContext)
            ?.doOnError { } // TODO logging }
            ?.subscribe()
    }
}
