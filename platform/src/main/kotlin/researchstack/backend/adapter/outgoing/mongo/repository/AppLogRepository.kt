package researchstack.backend.adapter.outgoing.mongo.repository

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import researchstack.backend.adapter.outgoing.mongo.entity.AppLogEntity

interface AppLogRepository : ReactiveMongoRepository<AppLogEntity, String>
