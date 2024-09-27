package researchstack.backend.adapter.incoming.mapper.task

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.TaskResultTestUtil.Companion.getGrpcTaskResult
import researchstack.backend.TaskResultTestUtil.Companion.studyId
import researchstack.backend.TaskResultTestUtil.Companion.taskId
import researchstack.backend.application.port.incoming.task.TaskResultCommand
import researchstack.backend.enums.ActivityType
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class UploadTaskResultMapperTest {
    @ParameterizedTest
    @Tag(POSITIVE_TEST)
    @EnumSource(
        value = ActivityType::class,
        mode = EnumSource.Mode.INCLUDE,
        names = ["TAPPING_SPEED", "REACTION_TIME", "GUIDED_BREATHING", "RANGE_OF_MOTION", "GAIT_AND_BALANCE", "STROOP_TEST", "SPEECH_RECOGNITION", "MOBILE_SPIROMETRY", "SUSTAINED_PHONATION", "SHAPE_PAINTING", "CATCH_LADYBUG", "MEMORIZE", "MEMORIZE_WORDS_START", "MEMORIZE_WORDS_END", "DESCRIBE_IMAGE", "READ_TEXT_ALOUD", "ANSWER_VERBALLY", "ANSWER_WRITTEN"]
    )
    fun `should map grpcTaskResult properly when result is activity and value consists of string only`(activityType: ActivityType) =
        runTest {
            val grpcTaskResult = getGrpcTaskResult(activityType)
            val taskResultCommand = grpcTaskResult.toCommand()

            assertEquals(studyId, taskResultCommand.studyId)
            assertEquals(taskId, taskResultCommand.taskId)
            assertEquals(grpcTaskResult.timeOffset, taskResultCommand.timeOffset)
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
    fun `should map grpcTaskResult properly when result is activity and value contains timestamp`(activityType: ActivityType) =
        runTest {
            val grpcTaskResult = getGrpcTaskResult(activityType)
            val taskResultCommand = grpcTaskResult.toCommand()

            assertEquals(studyId, taskResultCommand.studyId)
            assertEquals(taskId, taskResultCommand.taskId)
            assertEquals(grpcTaskResult.timeOffset, taskResultCommand.timeOffset)
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
        assertThrows<IllegalArgumentException> {
            getGrpcTaskResult(activityType)
        }
    }
}
