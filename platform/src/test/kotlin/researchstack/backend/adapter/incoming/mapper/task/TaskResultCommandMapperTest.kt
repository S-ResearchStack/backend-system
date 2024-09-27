package researchstack.backend.adapter.incoming.mapper.task

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.TaskResultTestUtil.Companion.getActivityTaskResultRestCommand
import researchstack.backend.TaskResultTestUtil.Companion.getSurveyTaskResultRestCommand
import researchstack.backend.TaskResultTestUtil.Companion.studyId
import researchstack.backend.TaskResultTestUtil.Companion.taskId
import researchstack.backend.application.port.incoming.task.TaskResultCommand
import researchstack.backend.enums.ActivityType
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class TaskResultCommandMapperTest {
    @ParameterizedTest
    @Tag(POSITIVE_TEST)
    @EnumSource(
        value = ActivityType::class,
        mode = EnumSource.Mode.INCLUDE,
        names = ["TAPPING_SPEED", "REACTION_TIME", "GUIDED_BREATHING", "RANGE_OF_MOTION", "GAIT_AND_BALANCE", "STROOP_TEST", "SPEECH_RECOGNITION", "MOBILE_SPIROMETRY", "SUSTAINED_PHONATION", "SHAPE_PAINTING", "CATCH_LADYBUG", "MEMORIZE", "MEMORIZE_WORDS_START", "MEMORIZE_WORDS_END", "DESCRIBE_IMAGE", "READ_TEXT_ALOUD", "ANSWER_VERBALLY", "ANSWER_WRITTEN"]
    )
    fun `should map taskResultRestCommand to taskResultCommand properly when result is activity`(activityType: ActivityType) =
        runTest {
            val taskResultRestCommand = getActivityTaskResultRestCommand(activityType, "result")
            val taskResultCommand = taskResultRestCommand.toTaskResultCommand(studyId, taskId)

            assertEquals(studyId, taskResultCommand.studyId)
            assertEquals(taskId, taskResultCommand.taskId)
            assertEquals(taskResultRestCommand.startedAt, taskResultCommand.startedAt)
            assertEquals(taskResultRestCommand.finishedAt, taskResultCommand.finishedAt)
            assertEquals(taskResultRestCommand.timeOffset, taskResultCommand.timeOffset)
            val activityResult = taskResultCommand.result as TaskResultCommand.ActivityResult
            assertEquals(activityType.value, activityResult.data::class.simpleName)
        }

    @ParameterizedTest
    @Tag(POSITIVE_TEST)
    @EnumSource(
        value = ActivityType::class,
        mode = EnumSource.Mode.INCLUDE,
        names = ["FIVE_METER_WALK_TEST", "STATE_BALANCE_TEST", "ROMBERG_TEST", "SIT_TO_STAND", "ORTHOSTATIC_BP", "BIA_MEASUREMENT", "BP_MEASUREMENT", "ECG_MEASUREMENT", "PPG_MEASUREMENT", "SPO2_MEASUREMENT", "BP_AND_BIA_MEASUREMENT", "STABLE_MEASUREMENT"]
    )
    fun `should map taskResultRestCommand to taskResultCommand properly when result is activity and has startTime and endTime`(
        activityType: ActivityType
    ) = runTest {
        val taskResultRestCommand = getActivityTaskResultRestCommand(
            activityType,
            """
            {
                "startTime": "2023-01-01T12:00:00Z",
                "endTime": "2023-01-01T12:10:00Z"
            }
            """.trimIndent()
        )
        val taskResultCommand = taskResultRestCommand.toTaskResultCommand(studyId, taskId)

        assertEquals(studyId, taskResultCommand.studyId)
        assertEquals(taskId, taskResultCommand.taskId)
        assertEquals(taskResultRestCommand.startedAt, taskResultCommand.startedAt)
        assertEquals(taskResultRestCommand.finishedAt, taskResultCommand.finishedAt)
        assertEquals(taskResultRestCommand.timeOffset, taskResultCommand.timeOffset)
        val activityResult = taskResultCommand.result as TaskResultCommand.ActivityResult
        assertEquals(activityType.value, activityResult.data::class.simpleName)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @EnumSource(
        value = ActivityType::class,
        mode = EnumSource.Mode.INCLUDE,
        names = ["UNSPECIFIED"]
    )
    fun `should throw error when activityType is unsupported`(activityType: ActivityType) = runTest {
        val taskResultRestCommand = getActivityTaskResultRestCommand(activityType, "result")

        assertThrows<IllegalArgumentException> {
            taskResultRestCommand.toTaskResultCommand(studyId, taskId)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should map taskResultRestCommand to taskResultCommand properly when result is survey`() = runTest {
        val taskResultRestCommand = getSurveyTaskResultRestCommand()
        val taskRestCommand = taskResultRestCommand.toTaskResultCommand(studyId, taskId)

        assertEquals(studyId, taskRestCommand.studyId)
        assertEquals(taskId, taskRestCommand.taskId)
        assertEquals(taskResultRestCommand.startedAt, taskRestCommand.startedAt)
        assertEquals(taskResultRestCommand.finishedAt, taskRestCommand.finishedAt)
        assertEquals(taskResultRestCommand.timeOffset, taskRestCommand.timeOffset)
        val surveyResult = taskRestCommand.result as TaskResultCommand.SurveyResult
        assertEquals("question-id", surveyResult.questionResults[0].id)
        assertEquals("question-result", surveyResult.questionResults[0].result)
    }
}
