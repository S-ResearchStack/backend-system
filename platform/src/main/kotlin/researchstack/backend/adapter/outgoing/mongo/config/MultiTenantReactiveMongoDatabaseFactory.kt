package researchstack.backend.adapter.outgoing.mongo.config

import com.linecorp.armeria.server.ServiceRequestContext
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoDatabase
import org.bson.codecs.configuration.CodecRegistry
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory
import reactor.core.publisher.Mono
import researchstack.backend.config.getStudyId

class MultiTenantReactiveMongoDatabaseFactory(
    private val mongoClient: MongoClient,
    defaultDatabaseName: String
) : SimpleReactiveMongoDatabaseFactory(mongoClient, defaultDatabaseName) {
    override fun getCodecRegistry(): CodecRegistry {
        return try {
            mongoClient.getDatabase(
                getStudyDatabaseName(ServiceRequestContext.current().getStudyId())
            ).codecRegistry
        } catch (e: Exception) {
            super.getCodecRegistry()
        }
    }

    override fun getMongoDatabase(): Mono<MongoDatabase> {
        return try {
            getMongoDatabase(
                getStudyDatabaseName(ServiceRequestContext.current().getStudyId())
            )
        } catch (e: Exception) {
            super.getMongoDatabase()
        }
    }
}
