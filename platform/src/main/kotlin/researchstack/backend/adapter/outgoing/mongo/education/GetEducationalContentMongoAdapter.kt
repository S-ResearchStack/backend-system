package researchstack.backend.adapter.outgoing.mongo.education

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.mapper.toDomain
import researchstack.backend.adapter.outgoing.mongo.repository.EducationalContentRepository
import researchstack.backend.application.port.outgoing.education.GetEducationalContentOutPort
import researchstack.backend.domain.education.EducationalContent
import researchstack.backend.enums.EducationalContentStatus

@Component
class GetEducationalContentMongoAdapter(
    private val educationalContentRepository: EducationalContentRepository
) : GetEducationalContentOutPort {
    override suspend fun getEducationalContent(contentId: String): EducationalContent {
        return educationalContentRepository
            .findById(contentId)
            .map { it.toDomain() }
            .awaitSingle()
    }

    override suspend fun getEducationalContentList(): List<EducationalContent> {
        return educationalContentRepository
            .findAll()
            .map { it.toDomain() }
            .collectList()
            .awaitSingle()
    }

    override suspend fun getEducationalContentList(
        status: EducationalContentStatus
    ): List<EducationalContent> {
        return educationalContentRepository
            .findByStatus(status)
            .map { it.toDomain() }
            .collectList()
            .awaitSingle()
    }
}
