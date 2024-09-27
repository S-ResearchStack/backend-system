package researchstack.backend.adapter.outgoing.mongo.repository

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import researchstack.backend.adapter.outgoing.mongo.entity.SubjectEntity

interface SubjectRepository : ReactiveMongoRepository<SubjectEntity, String>
