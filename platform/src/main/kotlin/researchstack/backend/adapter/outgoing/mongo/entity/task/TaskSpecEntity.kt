package researchstack.backend.adapter.outgoing.mongo.entity.task

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import researchstack.backend.enums.ActivityType
import researchstack.backend.enums.QuestionTag
import researchstack.backend.enums.QuestionType
import researchstack.backend.enums.TaskStatus
import researchstack.backend.enums.TaskType
import java.time.LocalDateTime

@Document("taskSpec")
data class TaskSpecEntity(
    @Id
    val id: String,
    val studyId: String,
    val title: String,
    val description: String,
    val status: TaskStatus,
    val schedule: String,
    val createdAt: LocalDateTime? = null,
    val publishedAt: LocalDateTime? = null,
    val startTime: LocalDateTime? = null,
    val endTime: LocalDateTime? = null,
    val validMin: Long,
    val duration: String,
    val iconUrl: String? = null,
    val taskType: TaskType,
    val task: Task
) {
    sealed interface Task

    data class ActivityTask(
        val completionTitle: String,
        val completionDescription: String,
        val type: ActivityType
    ) : Task

    data class SurveyTask(val sections: List<Section>) : Task {
        data class Section(val questions: List<Question>) {
            data class Question(
                val id: String,
                val title: String,
                val explanation: String,
                val tag: QuestionTag,
                val itemProperties: ItemProperties,
                val required: Boolean,
                val type: QuestionType?
            ) {
                sealed interface ItemProperties

                data class ChoiceProperties(val options: List<Option>) : ItemProperties

                data class ScaleProperties(
                    val low: Int,
                    val high: Int,
                    val lowLabel: String,
                    val highLabel: String
                ) : ItemProperties

                class TextProperties : ItemProperties

                data class RankingProperties(val options: List<Option>) : ItemProperties

                data class DateTimeProperties(
                    val isTime: Boolean,
                    val isDate: Boolean,
                    val isRange: Boolean
                ) : ItemProperties

                data class Option(
                    val value: String,
                    val label: String
                )
            }
        }
    }
}
