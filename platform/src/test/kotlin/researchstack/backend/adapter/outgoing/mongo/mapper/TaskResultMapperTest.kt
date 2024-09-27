package researchstack.backend.adapter.outgoing.mongo.mapper

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.TaskResultTestUtil
import researchstack.backend.adapter.incoming.rest.common.JsonHandler
import researchstack.backend.adapter.outgoing.mongo.entity.task.TaskResultEntity
import researchstack.backend.enums.TaskType
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class TaskResultMapperTest {
    @Test
    @Tag(POSITIVE_TEST)
    fun `should map ActivityTaskResult to TaskResultEntity`() = runTest {
        val taskResult = TaskResultTestUtil.getActivityTaskResult()
        val result = taskResult.toEntity(subjectId = "test-subject")

        assertEquals(taskResult.studyId, result.studyId)
        assertEquals(taskResult.taskId, result.taskId)
        assertEquals(taskResult.startedAt, result.startedAt)
        assertEquals(taskResult.finishedAt, result.finishedAt)
        assertEquals(taskResult.type, result.type)
        assertEquals(JsonHandler.toJson(taskResult.result), JsonHandler.toJson(result.result))
        assertEquals(taskResult.timeOffset, result.timeOffset)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should map SurveyTaskResult to TaskResultEntity`() = runTest {
        val taskResult = TaskResultTestUtil.getSurveyTaskResult()
        val result = taskResult.toEntity(subjectId = "test-subject")

        assertEquals(taskResult.studyId, result.studyId)
        assertEquals(taskResult.taskId, result.taskId)
        assertEquals(taskResult.startedAt, result.startedAt)
        assertEquals(taskResult.finishedAt, result.finishedAt)
        assertEquals(taskResult.type, result.type)
        assertEquals(JsonHandler.toJson(taskResult.result), JsonHandler.toJson(result.result))
        assertEquals(taskResult.timeOffset, result.timeOffset)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should map TaskResultEntity to TaskResult properly`() = runTest {
        var taskResultEntity: TaskResultEntity
        val results = listOf(
            TaskResultTestUtil.surveyResultEntity,
            TaskResultTestUtil.tappingSpeedEntity,
            TaskResultTestUtil.reactionTimeEntity,
            TaskResultTestUtil.guidedBreathingEntity,
            TaskResultTestUtil.rangeOfMotionEntity,
            TaskResultTestUtil.gaitAndBalanceEntity,
            TaskResultTestUtil.stroopTestEntity,
            TaskResultTestUtil.speechRecognitionEntity,
            TaskResultTestUtil.mobileSpirometryEntity,
            TaskResultTestUtil.sustainedPhonationEntity,
            TaskResultTestUtil.fiveMeterWalkTestEntity,
            TaskResultTestUtil.stateBalanceTestEntity,
            TaskResultTestUtil.rombergTestEntity,
            TaskResultTestUtil.sitToStandEntity,
            TaskResultTestUtil.orthostaticBpEntity,
            TaskResultTestUtil.biaMeasurementEntity,
            TaskResultTestUtil.bpMeasurementEntity,
            TaskResultTestUtil.ecgMeasurementEntity,
            TaskResultTestUtil.ppgMeasurementEntity,
            TaskResultTestUtil.spo2MeasurementEntity,
            TaskResultTestUtil.bpAndBiaMeasurementEntity,
            TaskResultTestUtil.stableMeasurementEntity,
            TaskResultTestUtil.shapePaintingEntity,
            TaskResultTestUtil.catchLadybugEntity,
            TaskResultTestUtil.memorizeEntity,
            TaskResultTestUtil.memorizeWordsStartEntity,
            TaskResultTestUtil.memorizeWordsEndEntity,
            TaskResultTestUtil.describeImageEntity,
            TaskResultTestUtil.readTextAloudEntity,
            TaskResultTestUtil.answerVerballyEntity,
            TaskResultTestUtil.answerWrittenEntity
        )

        results.forEach {
            if (it is TaskResultEntity.ActivityResult) {
                taskResultEntity =
                    TaskResultTestUtil.getTaskResultEntity(TaskType.ACTIVITY, it)
            } else {
                taskResultEntity =
                    TaskResultTestUtil.getTaskResultEntity(TaskType.SURVEY, it as TaskResultEntity.SurveyResult)
            }
            val taskResult = taskResultEntity.toDomain()
            assertEquals(taskResultEntity.studyId, taskResult.studyId)
            assertEquals(taskResultEntity.taskId, taskResult.taskId)
            assertEquals(taskResultEntity.startedAt, taskResult.startedAt)
            assertEquals(taskResultEntity.finishedAt, taskResult.finishedAt)
            assertEquals(taskResultEntity.type, taskResult.type)
            assertEquals(JsonHandler.toJson(taskResultEntity.result), JsonHandler.toJson(taskResult.result))
            assertEquals(taskResultEntity.timeOffset, taskResult.timeOffset)
        }
    }
}
