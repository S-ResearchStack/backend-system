package researchstack.backend.application.service.mapper

import kotlinx.serialization.json.Json
import org.json.JSONObject
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import researchstack.backend.application.port.incoming.task.CreateTaskSpecCommand
import researchstack.backend.application.port.incoming.task.UpdateTaskSpecCommand
import researchstack.backend.domain.task.ActivityTask
import researchstack.backend.domain.task.SurveyTask
import researchstack.backend.domain.task.TaskSpec
import researchstack.backend.enums.TaskType

@Mapper(componentModel = "spring")
abstract class TaskSpecMapper {
    companion object {
        val INSTANCE: TaskSpecMapper = Mappers.getMapper(TaskSpecMapper::class.java)
    }

    private val json = Json { ignoreUnknownKeys = true }

    @Mapping(target = "task", source = "command")
    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(
        target = "publishedAt",
        expression = "java(java.time.LocalDateTime.now())"
    ) // should be manually assigned later
    abstract fun toDomain(command: CreateTaskSpecCommand, studyId: String): TaskSpec

    fun mapTask(command: CreateTaskSpecCommand): TaskSpec.Task {
        return when (command.taskType) {
            TaskType.ACTIVITY ->
                json.decodeFromString<ActivityTask>(JSONObject(command.task).toString())

            TaskType.SURVEY ->
                json.decodeFromString<SurveyTask>(JSONObject(command.task).toString())

            else ->
                throw IllegalArgumentException("Unsupported Task Type: ${command.taskType}")
        }
    }

    @Mapping(target = "task", source = "command")
    @Mapping(target = "id", source = "taskId")
    abstract fun toDomain(command: UpdateTaskSpecCommand, taskId: String, studyId: String): TaskSpec

    fun mapTask(command: UpdateTaskSpecCommand): TaskSpec.Task {
        return when (command.taskType) {
            TaskType.ACTIVITY ->
                json.decodeFromString<ActivityTask>(JSONObject(command.task).toString())

            TaskType.SURVEY ->
                json.decodeFromString<SurveyTask>(JSONObject(command.task).toString())

            else ->
                throw IllegalArgumentException("Unsupported Task Type: ${command.taskType}")
        }
    }
}
