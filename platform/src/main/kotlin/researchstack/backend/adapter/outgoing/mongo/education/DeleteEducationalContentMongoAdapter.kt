package researchstack.backend.adapter.outgoing.mongo.education

import kotlinx.coroutines.reactive.awaitFirstOrDefault
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.repository.EducationalContentRepository
import researchstack.backend.application.port.outgoing.education.DeleteEducationalContentOutPort

@Component
class DeleteEducationalContentMongoAdapter(
    private val educationalContentRepository: EducationalContentRepository
) : DeleteEducationalContentOutPort {
    override suspend fun deleteEducationalContent(contentId: String) {
        educationalContentRepository.deleteById(contentId).awaitFirstOrDefault(null)
    }
}
