package researchstack.backend.adapter.outgoing.mongo.config

import com.linecorp.armeria.server.ServiceRequestContext
import com.mongodb.reactivestreams.client.MongoClient
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration
import researchstack.backend.config.MongoDbProperties
import researchstack.backend.config.getStudyId

@Configuration
class MultiTenantReactiveMongoConfiguration(
    mongoClient: MongoClient,
    mongoDbProperties: MongoDbProperties
) : AbstractReactiveMongoConfiguration() {
    val mongoDatabaseFactory = MultiTenantReactiveMongoDatabaseFactory(mongoClient, mongoDbProperties.database)

    override fun getDatabaseName(): String {
        return getStudyDatabaseName(ServiceRequestContext.current().getStudyId())
    }

    override fun reactiveMongoDbFactory(): ReactiveMongoDatabaseFactory {
        return mongoDatabaseFactory
    }
}
