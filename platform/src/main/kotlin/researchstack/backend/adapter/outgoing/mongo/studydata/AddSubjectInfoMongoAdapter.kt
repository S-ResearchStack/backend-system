package researchstack.backend.adapter.outgoing.mongo.studydata

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import researchstack.backend.adapter.outgoing.mongo.mapper.toDomain
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.SubjectInfoRepository
import researchstack.backend.application.exception.AlreadyExistsException
import researchstack.backend.application.port.outgoing.studydata.AddSubjectInfoOutPort
import researchstack.backend.domain.subject.SubjectInfo

@Component
class AddSubjectInfoMongoAdapter(
    private val subjectInfoRepository: SubjectInfoRepository
) : AddSubjectInfoOutPort {
    override suspend fun addSubjectInfo(subjectInfo: SubjectInfo): SubjectInfo {
        return subjectInfoRepository.existsByStudyIdAndSubjectNumber(
            studyId = subjectInfo.studyId,
            subjectNumber = subjectInfo.subjectNumber
        ).flatMap { exist ->
            if (exist) return@flatMap Mono.error(AlreadyExistsException("SubjectInfo already exists."))
            subjectInfoRepository.save(subjectInfo.toEntity())
        }.map {
            it.toDomain()
        }.awaitSingle()
    }
}
