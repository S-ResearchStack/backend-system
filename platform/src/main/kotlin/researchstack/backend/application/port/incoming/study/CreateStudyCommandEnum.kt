package researchstack.backend.application.port.incoming.study

import researchstack.backend.enums.IrbDecisionType
import researchstack.backend.enums.StudyParticipationApprovalType
import researchstack.backend.enums.StudyScope
import researchstack.backend.enums.StudyStage

data class CreateStudyCommandEnum(
    val participationApprovalType: List<StudyParticipationApprovalType> = StudyParticipationApprovalType.entries.map { it },
    val scope: List<StudyScope> = StudyScope.entries.map { it },
    val stage: List<StudyStage> = StudyStage.entries.map { it },
    val irbDecisionType: List<IrbDecisionType> = IrbDecisionType.entries.map { it }
)
