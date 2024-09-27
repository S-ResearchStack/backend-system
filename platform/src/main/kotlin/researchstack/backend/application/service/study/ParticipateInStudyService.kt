package researchstack.backend.application.service.study

import org.springframework.stereotype.Service
import researchstack.backend.application.exception.NotFoundException
import researchstack.backend.application.port.incoming.study.ParticipateInStudyCommand
import researchstack.backend.application.port.incoming.study.ParticipateInStudyUseCase
import researchstack.backend.application.port.outgoing.casbin.AddRoleOutPort
import researchstack.backend.application.port.outgoing.study.CreateSubjectNumberOutPort
import researchstack.backend.application.port.outgoing.study.ParticipateInStudyOutPort
import researchstack.backend.application.port.outgoing.studydata.AddSubjectInfoOutPort
import researchstack.backend.application.port.outgoing.subject.GetSubjectProfileOutPort
import researchstack.backend.domain.subject.Subject
import researchstack.backend.domain.subject.SubjectInfo
import researchstack.backend.enums.SubjectStatus

@Service
class ParticipateInStudyService(
    private val createSubjectNumberOutPort: CreateSubjectNumberOutPort,
    private val getSubjectProfileOutPort: GetSubjectProfileOutPort,
    private val participateInStudyOutPort: ParticipateInStudyOutPort,
    private val addSubjectInfoOutPort: AddSubjectInfoOutPort,
    private val addRoleOutPort: AddRoleOutPort
) : ParticipateInStudyUseCase {

    override suspend fun participateInStudy(
        subjectId: String,
        studyId: String,
        command: ParticipateInStudyCommand
    ): String {
        try {
            getSubjectProfileOutPort.getSubjectProfile(Subject.SubjectId.from(subjectId))
        } catch (e: NoSuchElementException) {
            throw NotFoundException("Subject($subjectId}) is not registered yet. Please register subject's profile first.")
        }

        val subjectNumber = createSubjectNumberOutPort.createSubjectNumber(studyId, subjectId)
        val sessionId = System.currentTimeMillis().toString()
        val subjectStatus = SubjectStatus.PARTICIPATING

        participateInStudyOutPort.participateInStudy(
            subjectId = subjectId,
            studyId = studyId,
            subjectNumber = subjectNumber,
            subjectStatus = subjectStatus,
            sessionId = sessionId,
            eligibilityTestResult = command.eligibilityTestResultCommand?.toDomain(),
            signedInformedConsent = command.signedInformedConsentCommand.toDomain()
        )

        if (!addRoleOutPort.addParticipantRole(subjectId, studyId)) {
            throw IllegalArgumentException("failed to add participant role ($subjectId, $studyId)")
        }

        addSubjectInfoOutPort.addSubjectInfo(
            SubjectInfo(
                studyId = studyId,
                subjectNumber = subjectNumber,
                status = subjectStatus,
                subjectId = subjectId
            )
        )

        return subjectNumber
    }
}
