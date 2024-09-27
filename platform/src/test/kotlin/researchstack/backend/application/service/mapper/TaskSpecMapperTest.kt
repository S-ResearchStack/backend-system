package researchstack.backend.application.service.mapper

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.port.incoming.task.CreateTaskSpecCommand
import researchstack.backend.domain.task.ActivityTask
import researchstack.backend.domain.task.Section
import researchstack.backend.domain.task.SurveyTask
import researchstack.backend.enums.ActivityType
import researchstack.backend.enums.TaskStatus
import researchstack.backend.enums.TaskType
import java.time.LocalDateTime
import kotlin.test.assertEquals

@ExperimentalSerializationApi
internal class TaskSpecMapperTest {
    @Test
    @Tag(NEGATIVE_TEST)
    fun `mapTask throws IllegalArgumentException when it received unsupported task type`() {
        assertThrows<IllegalArgumentException>("Unsupported Task Type: UNSPECIFIED") {
            TaskSpecMapper.INSTANCE.mapTask(
                createCommand(
                    TaskType.UNSPECIFIED,
                    mapOf(
                        "completionTitle" to "ct",
                        "completionDescription" to "cd",
                        "type" to "STROOP_TEST"
                    )
                )
            )
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `mapTask throws MissingFieldException when it failed to receive required fields from task`() {
        assertThrows<MissingFieldException> {
            TaskSpecMapper.INSTANCE.mapTask(
                createCommand(TaskType.ACTIVITY, mapOf())
            )
        }

        assertThrows<MissingFieldException> {
            TaskSpecMapper.INSTANCE.mapTask(
                createCommand(TaskType.SURVEY, mapOf())
            )
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `mapTask should work properly`() {
        val activityTask = TaskSpecMapper.INSTANCE.mapTask(
            createCommand(
                TaskType.ACTIVITY,
                mapOf(
                    "completionTitle" to "ct",
                    "completionDescription" to "cd",
                    "type" to "STROOP_TEST"
                )
            )
        )
        assertEquals(ActivityTask("ct", "cd", ActivityType.STROOP_TEST), activityTask)

        val surveyTask = TaskSpecMapper.INSTANCE.mapTask(
            createCommand(
                TaskType.SURVEY,
                mapOf("sections" to listOf<Section>())
            )
        )
        assertEquals(SurveyTask(listOf()), surveyTask)
    }

    private fun createCommand(type: TaskType, task: Map<String, Any>): CreateTaskSpecCommand {
        return CreateTaskSpecCommand(
            "test title",
            "test description",
            TaskStatus.CREATED,
            "0 0 0 * * ? *",
            LocalDateTime.now(),
            LocalDateTime.now(),
            0,
            "",
            null,
            true,
            type,
            task
        )
    }
}
