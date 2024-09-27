package researchstack.backend.adapter.outgoing.mongo.education

import com.linecorp.armeria.server.ServiceRequestContext
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import researchstack.backend.adapter.outgoing.mongo.mapper.toDomain
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.EducationalContentRepository
import researchstack.backend.application.exception.AlreadyExistsException
import researchstack.backend.application.port.outgoing.education.CreateEducationalContentOutPort
import researchstack.backend.domain.education.EducationalContent

@Component
class CreateEducationalContentMongoAdapter(
    private val educationalContentRepository: EducationalContentRepository
) : CreateEducationalContentOutPort {
    override suspend fun createEducationalContent(educationalContent: EducationalContent): EducationalContent {
        val contextAwareScheduler = Schedulers.fromExecutor(ServiceRequestContext.current().blockingTaskExecutor())
        return educationalContentRepository
            .existsByTitle(educationalContent.title)
            .publishOn(contextAwareScheduler)
            .flatMap {
                if (it) {
                    Mono.error(
                        AlreadyExistsException(
                            "educationalContent(" +
                                "title: ${educationalContent.title}, " +
                                ") already exists"
                        )
                    )
                } else {
                    educationalContentRepository.insert(educationalContent.toEntity())
                }
            }
            .map { it.toDomain() }
            .onErrorMap(DuplicateKeyException::class.java) {
                AlreadyExistsException("duplicate key: ${it.message}")
            }
            .awaitSingle()
    }
}
