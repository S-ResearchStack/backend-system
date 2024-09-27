package researchstack.backend.adapter.outgoing.mongo.study.studyrelation

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.entity.study.SubjectStudyRelationEntity
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.SubjectStudyRelationRepository
import researchstack.backend.application.port.outgoing.study.ParticipateInStudyOutPort
import researchstack.backend.domain.study.EligibilityTestResult
import researchstack.backend.domain.study.SignedInformedConsent
import researchstack.backend.enums.SubjectStatus

@ConditionalOnProperty(name = ["database-type"], havingValue = "MONGO")
@Component
class CreateUserStudyRelationMongoAdapter(
    private val subjectStudyRelationRepository: SubjectStudyRelationRepository
) : ParticipateInStudyOutPort {
    override suspend fun participateInStudy(
        subjectId: String,
        studyId: String,
        subjectNumber: String,
        subjectStatus: SubjectStatus,
        sessionId: String,
        eligibilityTestResult: EligibilityTestResult?,
        signedInformedConsent: SignedInformedConsent
    ) {
        subjectStudyRelationRepository.save(
            SubjectStudyRelationEntity(
                null,
                subjectId,
                subjectNumber,
                studyId,
                signedInformedConsent.imagePath,
                eligibilityTestResult?.toEntity()
            )
        ).awaitSingle()
    }
}
