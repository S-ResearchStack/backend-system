package researchstack.backend.adapter.outgoing.mongo.healthdata

import com.fasterxml.jackson.databind.ObjectMapper
import com.linecorp.armeria.server.ServiceRequestContext
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.entity.healthdata.HealthDataEntity
import researchstack.backend.adapter.outgoing.mongo.entity.healthdata.toHealthDataEntityClass
import researchstack.backend.adapter.outgoing.mongo.repository.healthdata.HealthDataRepositoryLookup
import researchstack.backend.application.port.outgoing.healthdata.UploadHealthDataOutPort
import researchstack.backend.config.STUDY_ID_KEY
import researchstack.backend.domain.healthdata.BatchHealthData
import researchstack.backend.domain.healthdata.HealthData
import researchstack.backend.domain.subject.Subject
import researchstack.backend.enums.HealthDataType

@ConditionalOnProperty(name = ["database-type"], havingValue = "MONGO")
@Component
class UploadHealthDataMongoAdapter(
    private val healthDataRepositoryLookup: HealthDataRepositoryLookup,
    private val objectMapper: ObjectMapper
) : UploadHealthDataOutPort {
    override suspend fun upload(
        subjectId: Subject.SubjectId,
        studyIds: List<String>,
        type: HealthDataType,
        data: List<HealthData>
    ) {
        val healthDataEntities = mutableListOf<HealthDataEntity>()
        data.forEach { dataElement ->
            healthDataEntities.add(
                objectMapper.convertValue(
                    dataElement.data.toMutableMap().apply {
                        set("subjectId", subjectId.value)
                    },
                    type.toHealthDataEntityClass()
                )
            )
        }
        studyIds.forEach { studyId ->
            ServiceRequestContext.current().setAttr(STUDY_ID_KEY, studyId)
            healthDataRepositoryLookup.getRepository(type)
                ?.saveAll(healthDataEntities)
                ?.subscribe()
        }
    }

    override suspend fun uploadBatch(
        subjectId: Subject.SubjectId,
        studyIds: List<String>,
        batchData: List<BatchHealthData>
    ) {
        batchData.forEach {
            val healthDataEntities = mutableListOf<HealthDataEntity>()

            val type = it.type
            it.data.forEach { dataElement ->
                healthDataEntities.add(
                    objectMapper.convertValue(
                        dataElement.data.toMutableMap().apply {
                            set("subjectId", subjectId.value)
                        },
                        type.toHealthDataEntityClass()
                    )
                )
            }

            studyIds.forEach { studyId ->
                ServiceRequestContext.current().setAttr(STUDY_ID_KEY, studyId)
                healthDataRepositoryLookup.getRepository(type)
                    ?.saveAll(healthDataEntities)
                    ?.subscribe()
            }
        }
    }
}
