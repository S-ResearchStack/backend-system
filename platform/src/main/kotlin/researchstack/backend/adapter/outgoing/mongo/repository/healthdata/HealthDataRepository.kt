package researchstack.backend.adapter.outgoing.mongo.repository.healthdata

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import researchstack.backend.adapter.outgoing.mongo.entity.healthdata.HealthDataEntity

interface HealthDataRepository<T : HealthDataEntity> : ReactiveMongoRepository<T, String>
