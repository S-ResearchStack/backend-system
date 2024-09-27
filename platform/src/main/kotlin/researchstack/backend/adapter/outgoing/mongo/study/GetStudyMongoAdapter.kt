package researchstack.backend.adapter.outgoing.mongo.study

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import researchstack.backend.adapter.outgoing.mongo.mapper.toDomain
import researchstack.backend.adapter.outgoing.mongo.repository.StudyRepository
import researchstack.backend.adapter.outgoing.mongo.repository.SubjectStudyRelationRepository
import researchstack.backend.application.port.outgoing.study.GetStudyOutPort
import researchstack.backend.domain.study.Study
import researchstack.backend.enums.StudyScope
import researchstack.backend.enums.StudyStage

@ConditionalOnProperty(name = ["database-type"], havingValue = "MONGO")
@Component
class GetStudyMongoAdapter(
    private val studyRepository: StudyRepository,
    private val subjectStudyRelationRepository: SubjectStudyRelationRepository
) : GetStudyOutPort {
    override suspend fun getStudy(studyId: String): Study {
        return studyRepository
            .findById(studyId)
            .map {
                it.toDomain()
            }
            .awaitSingle()
    }

    override suspend fun getStudyByParticipationCode(participationCode: String): Study {
        return studyRepository
            .findByParticipationCodeAndStudyInfoStage(participationCode, StudyStage.STARTED_OPEN)
            .map {
                it.toDomain()
            }
            .awaitSingle()
    }

    override suspend fun getStudyList(): List<Study> {
        return studyRepository
            .findByStudyInfoStage(StudyStage.STARTED_OPEN)
            .map {
                it.toDomain()
            }
            .collectList()
            .awaitSingle()
    }

    override suspend fun getPublicStudyList(): List<Study> {
        return studyRepository
            .findByStudyInfoScopeAndStudyInfoStage(StudyScope.PUBLIC, StudyStage.STARTED_OPEN)
            .map {
                it.toDomain()
            }
            .collectList()
            .awaitSingle()
    }

    override suspend fun getParticipatedStudyList(subjectId: String): List<Study> {
        return Mono.zip(
            studyRepository.findByStudyInfoStage(StudyStage.STARTED_OPEN).collectList(),
            subjectStudyRelationRepository.findBySubjectId(subjectId).collectList()
        ) { studies, relations ->
            studies
                .filter { studyEntity ->
                    relations.any {
                        it.studyId == studyEntity.id
                    }
                }
                .map {
                    it.toDomain()
                }
        }.awaitSingle()
    }
}
