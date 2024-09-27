package researchstack.backend.adapter.outgoing.mongo.healthdata

import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.mapper.toDomain
import researchstack.backend.adapter.outgoing.mongo.repository.HealthDataGroupRepository
import researchstack.backend.application.port.outgoing.study.GetHealthDataOutPort
import researchstack.backend.domain.study.HealthDataGroup
import researchstack.backend.enums.HealthDataGroupId

@ConditionalOnProperty(name = ["database-type"], havingValue = "MONGO")
@Component
class GetHealthDataMongoAdapter(
    private val healthDataGroupRepository: HealthDataGroupRepository
) : GetHealthDataOutPort {
    private val logger = LoggerFactory.getLogger(GetHealthDataMongoAdapter::class.java)

    override suspend fun getHealthDataList(): List<HealthDataGroup> {
        val healthDataList = mutableListOf<HealthDataGroup>()
        HealthDataGroupId.entries.map { groupId ->
            try {
                healthDataList.add(
                    healthDataGroupRepository.findByName(groupId.value).awaitSingle().toDomain()
                )
            } catch (e: NoSuchElementException) {
                val healthDataGroupTypes = HealthDataGroupId.getHealthDataGroupTypes(groupId)
                if (healthDataGroupTypes.isNotEmpty()) {
                    healthDataList.add(HealthDataGroup(groupId.value, healthDataGroupTypes))
                }
                logger.info("$groupId does not exist in mongoDB.")
            }
        }
        return healthDataList
    }
}
