package researchstack.backend.adapter.outgoing.mongo.education

import com.linecorp.armeria.server.ServiceRequestContext
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import reactor.core.scheduler.Schedulers
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.EducationalContentRepository
import researchstack.backend.application.port.outgoing.education.UpdateEducationalContentOutPort
import researchstack.backend.domain.education.EducationalContent

@Component
class UpdateEducationalContentMongoAdapter(
    private val educationalContentRepository: EducationalContentRepository
) : UpdateEducationalContentOutPort {
    override suspend fun updateEducationalContent(educationalContent: EducationalContent) {
        val contextAwareScheduler = Schedulers.fromExecutor(ServiceRequestContext.current().blockingTaskExecutor())
        if (educationalContent.id == null) {
            throw IllegalArgumentException("educational content id is null")
        }
        educationalContentRepository
            .findById(educationalContent.id)
            .publishOn(contextAwareScheduler)
            .flatMap {
                educationalContentRepository.save(educationalContent.toEntity())
            }
            .awaitSingle()
    }
}
