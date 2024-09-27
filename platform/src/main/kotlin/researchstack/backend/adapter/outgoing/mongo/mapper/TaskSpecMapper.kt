package researchstack.backend.adapter.outgoing.mongo.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import researchstack.backend.adapter.outgoing.mongo.entity.task.TaskSpecEntity
import researchstack.backend.domain.task.ActivityTask
import researchstack.backend.domain.task.Question
import researchstack.backend.domain.task.SurveyTask
import researchstack.backend.domain.task.TaskSpec

@Mapper
abstract class TaskSpecMapper {
    @Mapping(target = "task", source = "taskSpec")
    abstract fun toEntity(taskSpec: TaskSpec): TaskSpecEntity

    @Mapping(target = "task", source = "taskSpecEntity")
    abstract fun toDomain(taskSpecEntity: TaskSpecEntity): TaskSpec

    fun mapTask(taskSpec: TaskSpec): TaskSpecEntity.Task {
        return when (taskSpec.task) {
            is ActivityTask -> toEntity(taskSpec.task)
            is SurveyTask -> toEntity(taskSpec.task)
        }
    }

    abstract fun toEntity(activityTask: ActivityTask): TaskSpecEntity.ActivityTask

    abstract fun toEntity(surveyTask: SurveyTask): TaskSpecEntity.SurveyTask

    fun mapQuestion(question: Question): TaskSpecEntity.SurveyTask.Section.Question {
        return TaskSpecEntity.SurveyTask.Section.Question(
            question.id,
            question.title,
            question.explanation,
            question.tag,
            when (question.itemProperties) {
                is Question.ChoiceProperties -> toEntity(question.itemProperties)
                is Question.ScaleProperties -> toEntity(question.itemProperties)
                is Question.TextProperties -> toEntity(question.itemProperties)
                is Question.RankingProperties -> toEntity(question.itemProperties)
                is Question.DateTimeProperties -> toEntity(question.itemProperties)
            },
            question.required,
            question.type
        )
    }

    abstract fun toEntity(properties: Question.ChoiceProperties): TaskSpecEntity.SurveyTask.Section.Question.ChoiceProperties

    abstract fun toEntity(properties: Question.ScaleProperties): TaskSpecEntity.SurveyTask.Section.Question.ScaleProperties

    abstract fun toEntity(properties: Question.TextProperties): TaskSpecEntity.SurveyTask.Section.Question.TextProperties

    abstract fun toEntity(properties: Question.RankingProperties): TaskSpecEntity.SurveyTask.Section.Question.RankingProperties

    abstract fun toEntity(properties: Question.DateTimeProperties): TaskSpecEntity.SurveyTask.Section.Question.DateTimeProperties

    fun mapTask(taskSpecEntity: TaskSpecEntity): TaskSpec.Task {
        return when (taskSpecEntity.task) {
            is TaskSpecEntity.ActivityTask -> toDomain(taskSpecEntity.task)
            is TaskSpecEntity.SurveyTask -> toDomain(taskSpecEntity.task)
        }
    }

    abstract fun toDomain(activityTask: TaskSpecEntity.ActivityTask): ActivityTask

    abstract fun toDomain(surveyTask: TaskSpecEntity.SurveyTask): SurveyTask

    fun mapQuestion(question: TaskSpecEntity.SurveyTask.Section.Question): Question {
        return Question(
            question.id,
            question.title,
            question.explanation,
            question.tag,
            when (question.itemProperties) {
                is TaskSpecEntity.SurveyTask.Section.Question.ChoiceProperties -> toDomain(question.itemProperties)
                is TaskSpecEntity.SurveyTask.Section.Question.ScaleProperties -> toDomain(question.itemProperties)
                is TaskSpecEntity.SurveyTask.Section.Question.TextProperties -> toDomain(question.itemProperties)
                is TaskSpecEntity.SurveyTask.Section.Question.RankingProperties -> toDomain(question.itemProperties)
                is TaskSpecEntity.SurveyTask.Section.Question.DateTimeProperties -> toDomain(question.itemProperties)
            },
            question.required,
            question.type
        )
    }

    abstract fun toDomain(properties: TaskSpecEntity.SurveyTask.Section.Question.ChoiceProperties): Question.ChoiceProperties

    abstract fun toDomain(properties: TaskSpecEntity.SurveyTask.Section.Question.ScaleProperties): Question.ScaleProperties

    abstract fun toDomain(properties: TaskSpecEntity.SurveyTask.Section.Question.TextProperties): Question.TextProperties

    abstract fun toDomain(properties: TaskSpecEntity.SurveyTask.Section.Question.RankingProperties): Question.RankingProperties

    abstract fun toDomain(properties: TaskSpecEntity.SurveyTask.Section.Question.DateTimeProperties): Question.DateTimeProperties
}

private val converter = Mappers.getMapper(TaskSpecMapper::class.java)

fun TaskSpec.toEntity(): TaskSpecEntity = converter.toEntity(this)

fun TaskSpecEntity.toDomain(): TaskSpec = converter.toDomain(this)
