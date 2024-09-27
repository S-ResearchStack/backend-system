package researchstack.backend.adapter.outgoing.mongo.data

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Component
import reactor.kotlin.core.publisher.toFlux
import researchstack.backend.adapter.outgoing.mongo.config.getStudyDatabaseName
import researchstack.backend.application.port.outgoing.data.GetDataSourceOutPort

@Component
class GetDataSourceMongoAdapter(
    private val mongoTemplate: ReactiveMongoTemplate
) : GetDataSourceOutPort {
    override suspend fun getDatabaseNames(studyId: String): List<String> {
        return listOf(getStudyDatabaseName(studyId))
    }

    override suspend fun getTableNames(databaseName: String): List<String> {
        return mongoTemplate.mongoDatabaseFactory
            .getMongoDatabase(databaseName)
            .awaitSingle()
            .listCollectionNames()
            .toFlux()
            .collectList()
            .awaitSingle()
    }
}
