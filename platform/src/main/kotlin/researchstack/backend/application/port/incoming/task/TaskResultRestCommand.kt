package researchstack.backend.application.port.incoming.task

import researchstack.backend.enums.ActivityType

data class TaskResultRestCommand(
    val startedAt: Long,
    val finishedAt: Long,
    val timeOffset: Int,
    val activityType: ActivityType?,
    val activityResult: String?,
    val surveyResult: SurveyResult?
) {
    class SurveyResult(val questionResults: List<QuestionResult>) {
        data class QuestionResult(
            val id: String,
            val result: String
        )
    }
}
