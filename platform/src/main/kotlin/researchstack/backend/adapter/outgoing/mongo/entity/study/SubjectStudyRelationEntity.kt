package researchstack.backend.adapter.outgoing.mongo.entity.study

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("subjectStudyRelation")
data class SubjectStudyRelationEntity(
    @Id
    val id: String? = null,
    val subjectId: String,
    val subjectNumber: String,
    val studyId: String,
    val signedInformedConsentImagePath: String,
    val eligibilityTestResult: EligibilityTestResult?
) {
    data class EligibilityTestResult(
        val result: SurveyResult
    )

    data class SurveyResult(val questionResults: List<QuestionResult>) {
        data class QuestionResult(
            val id: String,
            val result: String
        )
    }
}
