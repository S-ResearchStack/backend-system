package researchstack.backend.adapter.outgoing.mongo.version

import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.mapper.toDomain
import researchstack.backend.adapter.outgoing.mongo.repository.VersionRepository
import researchstack.backend.application.port.outgoing.version.GetApplicationVersionOutPort
import researchstack.backend.domain.version.Version

@ConditionalOnProperty(name = ["database-type"], havingValue = "MONGO")
@Component
class GetApplicationVersionMongoAdapter(
    private val versionRepository: VersionRepository
) : GetApplicationVersionOutPort {
    override suspend fun getApplicationVersion(): Version {
        return versionRepository.findAll().awaitSingle().toDomain()
    }
}
