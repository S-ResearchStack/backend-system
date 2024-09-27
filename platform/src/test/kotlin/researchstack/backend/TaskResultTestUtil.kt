package researchstack.backend

import com.google.protobuf.Timestamp
import researchstack.backend.adapter.outgoing.mongo.entity.task.TaskResultEntity
import researchstack.backend.application.port.incoming.task.TaskResultRestCommand
import researchstack.backend.domain.task.TaskResult
import researchstack.backend.enums.ActivityType
import researchstack.backend.enums.TaskType
import researchstack.backend.grpc.TaskResult.ActivityResult
import researchstack.backend.grpc.TaskResult.ActivityResult.ReactionTime
import researchstack.backend.grpc.TaskResult.ActivityResult.TappingSpeed
import java.time.LocalDateTime
import java.time.ZoneOffset

class TaskResultTestUtil {
    companion object {
        const val studyId = "testStudy"
        const val taskId = "testTask"
        private val startedAt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        private val finishedAt = LocalDateTime.now().plusDays(5).toEpochSecond(ZoneOffset.UTC)
        private val startTime = Timestamp.newBuilder().setSeconds(1701411670).build()
        private val endTime = Timestamp.newBuilder().setSeconds(1987654321).build()
        private val activityType = TaskType.ACTIVITY
        private val surveyType = TaskType.SURVEY
        private val activityResult = TaskResult.ActivityResult(
            type = ActivityType.TAPPING_SPEED,
            data = TaskResult.ActivityResult.TappingSpeed(
                result = "tapping-result"
            )
        )

        private val surveyResult = TaskResult.SurveyResult(
            listOf(
                TaskResult.SurveyResult.QuestionResult(
                    id = "question-id",
                    result = "question-result"
                )
            )
        )

        private const val timeOffset = 0
        private const val userId = "testUser"

        fun getActivityTaskResult() = TaskResult(
            studyId = studyId,
            taskId = taskId,
            startedAt = startedAt,
            finishedAt = finishedAt,
            type = activityType,
            result = activityResult,
            timeOffset = timeOffset
        )

        fun getSurveyTaskResult() = TaskResult(
            studyId = studyId,
            taskId = taskId,
            startedAt = startedAt,
            finishedAt = finishedAt,
            type = surveyType,
            result = surveyResult,
            timeOffset = timeOffset
        )

        fun getTaskResultEntity(type: TaskType, resultEntity: TaskResultEntity.Result) = TaskResultEntity(
            studyId = studyId,
            taskId = taskId,
            subjectId = userId,
            startedAt = startedAt,
            finishedAt = finishedAt,
            type = type,
            result = resultEntity,
            timeOffset = timeOffset
        )

        val surveyResultEntity = TaskResultEntity.SurveyResult(
            listOf(
                TaskResultEntity.SurveyResult.QuestionResult(
                    id = "question-id",
                    result = "question-result"
                )
            )
        )
        val tappingSpeedEntity = TaskResultEntity.ActivityResult(
            type = ActivityType.TAPPING_SPEED,
            data = TaskResultEntity.ActivityResult.TappingSpeed("result")
        )
        val reactionTimeEntity = TaskResultEntity.ActivityResult(
            type = ActivityType.REACTION_TIME,
            data = TaskResultEntity.ActivityResult.ReactionTime("result")
        )
        val guidedBreathingEntity = TaskResultEntity.ActivityResult(
            type = ActivityType.REACTION_TIME,
            data = TaskResultEntity.ActivityResult.GuidedBreathing("result")
        )
        val rangeOfMotionEntity = TaskResultEntity.ActivityResult(
            type = ActivityType.REACTION_TIME,
            data = TaskResultEntity.ActivityResult.RangeOfMotion("result")
        )
        val gaitAndBalanceEntity = TaskResultEntity.ActivityResult(
            type = ActivityType.REACTION_TIME,
            data = TaskResultEntity.ActivityResult.GaitAndBalance("result")
        )
        val stroopTestEntity = TaskResultEntity.ActivityResult(
            type = ActivityType.REACTION_TIME,
            data = TaskResultEntity.ActivityResult.StroopTest("result")
        )
        val speechRecognitionEntity = TaskResultEntity.ActivityResult(
            type = ActivityType.REACTION_TIME,
            data = TaskResultEntity.ActivityResult.SpeechRecognition("result")
        )
        val mobileSpirometryEntity = TaskResultEntity.ActivityResult(
            type = ActivityType.REACTION_TIME,
            data = TaskResultEntity.ActivityResult.MobileSpirometry("filePath")
        )
        val sustainedPhonationEntity = TaskResultEntity.ActivityResult(
            type = ActivityType.REACTION_TIME,
            data = TaskResultEntity.ActivityResult.SustainedPhonation("result")
        )
        val fiveMeterWalkTestEntity = TaskResultEntity.ActivityResult(
            type = ActivityType.REACTION_TIME,
            data = TaskResultEntity.ActivityResult.FiveMeterWalkTest(
                "2024-04-01T16:36:44+09:00",
                "2024-04-26T04:18:00+09:00"
            )
        )
        val stateBalanceTestEntity = TaskResultEntity.ActivityResult(
            type = ActivityType.REACTION_TIME,
            data = TaskResultEntity.ActivityResult.StateBalanceTest(
                "2024-04-01T16:36:44+09:00",
                "2024-04-26T04:18:00+09:00"
            )
        )
        val rombergTestEntity = TaskResultEntity.ActivityResult(
            type = ActivityType.REACTION_TIME,
            data = TaskResultEntity.ActivityResult.RombergTest("2024-04-01T16:36:44+09:00", "2024-04-26T04:18:00+09:00")
        )
        val sitToStandEntity = TaskResultEntity.ActivityResult(
            type = ActivityType.REACTION_TIME,
            data = TaskResultEntity.ActivityResult.SitToStand("2024-04-01T16:36:44+09:00", "2024-04-26T04:18:00+09:00")
        )
        val orthostaticBpEntity = TaskResultEntity.ActivityResult(
            type = ActivityType.REACTION_TIME,
            data = TaskResultEntity.ActivityResult.OrthostaticBp(
                "2024-04-01T16:36:44+09:00",
                "2024-04-26T04:18:00+09:00"
            )
        )
        val biaMeasurementEntity = TaskResultEntity.ActivityResult(
            type = ActivityType.REACTION_TIME,
            data = TaskResultEntity.ActivityResult.BiaMeasurement(
                "2024-04-01T16:36:44+09:00",
                "2024-04-26T04:18:00+09:00"
            )
        )
        val bpMeasurementEntity = TaskResultEntity.ActivityResult(
            type = ActivityType.REACTION_TIME,
            data = TaskResultEntity.ActivityResult.BpMeasurement(
                "2024-04-01T16:36:44+09:00",
                "2024-04-26T04:18:00+09:00"
            )
        )
        val ecgMeasurementEntity = TaskResultEntity.ActivityResult(
            type = ActivityType.REACTION_TIME,
            data = TaskResultEntity.ActivityResult.EcgMeasurement(
                "2024-04-01T16:36:44+09:00",
                "2024-04-26T04:18:00+09:00"
            )
        )
        val ppgMeasurementEntity = TaskResultEntity.ActivityResult(
            type = ActivityType.REACTION_TIME,
            data = TaskResultEntity.ActivityResult.PpgMeasurement(
                "2024-04-01T16:36:44+09:00",
                "2024-04-26T04:18:00+09:00"
            )
        )
        val spo2MeasurementEntity = TaskResultEntity.ActivityResult(
            type = ActivityType.REACTION_TIME,
            data = TaskResultEntity.ActivityResult.Spo2Measurement(
                "2024-04-01T16:36:44+09:00",
                "2024-04-26T04:18:00+09:00"
            )
        )
        val bpAndBiaMeasurementEntity = TaskResultEntity.ActivityResult(
            type = ActivityType.REACTION_TIME,
            data = TaskResultEntity.ActivityResult.BpAndBiaMeasurement(
                "2024-04-01T16:36:44+09:00",
                "2024-04-26T04:18:00+09:00"
            )
        )
        val stableMeasurementEntity = TaskResultEntity.ActivityResult(
            type = ActivityType.REACTION_TIME,
            data = TaskResultEntity.ActivityResult.StableMeasurement(
                "2024-04-01T16:36:44+09:00",
                "2024-04-26T04:18:00+09:00"
            )
        )
        val shapePaintingEntity = TaskResultEntity.ActivityResult(
            type = ActivityType.REACTION_TIME,
            data = TaskResultEntity.ActivityResult.ShapePainting("result")
        )
        val catchLadybugEntity = TaskResultEntity.ActivityResult(
            type = ActivityType.REACTION_TIME,
            data = TaskResultEntity.ActivityResult.CatchLadybug("result")
        )
        val memorizeEntity = TaskResultEntity.ActivityResult(
            type = ActivityType.REACTION_TIME,
            data = TaskResultEntity.ActivityResult.Memorize("result")
        )
        val memorizeWordsStartEntity = TaskResultEntity.ActivityResult(
            type = ActivityType.REACTION_TIME,
            data = TaskResultEntity.ActivityResult.MemorizeWordsStart("result")
        )
        val memorizeWordsEndEntity = TaskResultEntity.ActivityResult(
            type = ActivityType.REACTION_TIME,
            data = TaskResultEntity.ActivityResult.MemorizeWordsEnd("result")
        )
        val describeImageEntity = TaskResultEntity.ActivityResult(
            type = ActivityType.REACTION_TIME,
            data = TaskResultEntity.ActivityResult.DescribeImage("result")
        )
        val readTextAloudEntity = TaskResultEntity.ActivityResult(
            type = ActivityType.REACTION_TIME,
            data = TaskResultEntity.ActivityResult.ReadTextAloud("result")
        )
        val answerVerballyEntity = TaskResultEntity.ActivityResult(
            type = ActivityType.REACTION_TIME,
            data = TaskResultEntity.ActivityResult.AnswerVerbally("result")
        )
        val answerWrittenEntity = TaskResultEntity.ActivityResult(
            type = ActivityType.REACTION_TIME,
            data = TaskResultEntity.ActivityResult.AnswerWritten("result")
        )

        fun getActivityTaskResultRestCommand(activityType: ActivityType, result: String) = TaskResultRestCommand(
            startedAt = startedAt,
            finishedAt = finishedAt,
            timeOffset = timeOffset,
            activityType = activityType,
            activityResult = result,
            surveyResult = null
        )

        fun getSurveyTaskResultRestCommand() = TaskResultRestCommand(
            startedAt = startedAt,
            finishedAt = finishedAt,
            timeOffset = timeOffset,
            activityType = null,
            activityResult = null,
            surveyResult = TaskResultRestCommand.SurveyResult(
                listOf(
                    TaskResultRestCommand.SurveyResult.QuestionResult(
                        id = "question-id",
                        result = "question-result"
                    )
                )
            )
        )

        fun getGrpcTaskResult(activityType: ActivityType): researchstack.backend.grpc.TaskResult {
            return researchstack.backend.grpc.TaskResult.newBuilder()
                .setStudyId(studyId)
                .setTaskId(taskId)
                .setStartedAt(startTime)
                .setFinishedAt(endTime)
                .setActivityResult(createGrpcActivityResult(activityType))
                .build()
        }

        private fun createGrpcActivityResult(activityType: ActivityType): ActivityResult? {
            val result = "result"
            val filePath = "filePath"

            return when (activityType) {
                ActivityType.TAPPING_SPEED -> ActivityResult.newBuilder()
                    .setTappingSpeed(TappingSpeed.newBuilder().setResult(result).build())
                    .build()

                ActivityType.REACTION_TIME -> ActivityResult.newBuilder()
                    .setReactionTime(ReactionTime.newBuilder().setResult(result).build())
                    .build()

                ActivityType.GUIDED_BREATHING -> ActivityResult.newBuilder()
                    .setGuidedBreathing(ActivityResult.GuidedBreathing.newBuilder().setResult(result).build())
                    .build()

                ActivityType.RANGE_OF_MOTION -> ActivityResult.newBuilder()
                    .setRangeOfMotion(ActivityResult.RangeOfMotion.newBuilder().setResult(result).build())
                    .build()

                ActivityType.GAIT_AND_BALANCE -> ActivityResult.newBuilder()
                    .setGaitAndBalance(ActivityResult.GaitAndBalance.newBuilder().setResult(result).build())
                    .build()

                ActivityType.STROOP_TEST -> ActivityResult.newBuilder()
                    .setStroopTest(ActivityResult.StroopTest.newBuilder().setResult(result).build())
                    .build()

                ActivityType.SPEECH_RECOGNITION -> ActivityResult.newBuilder()
                    .setSpeechRecognition(ActivityResult.SpeechRecognition.newBuilder().setResult(result).build())
                    .build()

                ActivityType.MOBILE_SPIROMETRY -> ActivityResult.newBuilder()
                    .setMobileSpirometry(ActivityResult.MobileSpirometry.newBuilder().setFilePath(filePath).build())
                    .build()

                ActivityType.SUSTAINED_PHONATION -> ActivityResult.newBuilder()
                    .setSustainedPhonation(ActivityResult.SustainedPhonation.newBuilder().setResult(result).build())
                    .build()

                ActivityType.FIVE_METER_WALK_TEST -> ActivityResult.newBuilder()
                    .setFiveMeterWalkTest(
                        ActivityResult.FiveMeterWalkTest.newBuilder()
                            .setStartTime(startTime)
                            .setEndTime(endTime)
                            .build()
                    )
                    .build()

                ActivityType.STATE_BALANCE_TEST -> ActivityResult.newBuilder()
                    .setStateBalanceTest(
                        ActivityResult.StateBalanceTest.newBuilder()
                            .setStartTime(startTime)
                            .setEndTime(endTime)
                            .build()
                    )
                    .build()

                ActivityType.ROMBERG_TEST -> ActivityResult.newBuilder()
                    .setRombergTest(
                        ActivityResult.RombergTest.newBuilder()
                            .setStartTime(startTime)
                            .setEndTime(endTime)
                            .build()
                    )
                    .build()

                ActivityType.SIT_TO_STAND -> ActivityResult.newBuilder()
                    .setSitToStand(
                        ActivityResult.SitToStand.newBuilder()
                            .setStartTime(startTime)
                            .setEndTime(endTime)
                            .build()
                    )
                    .build()

                ActivityType.ORTHOSTATIC_BP -> ActivityResult.newBuilder()
                    .setOrthostaticBp(
                        ActivityResult.OrthostaticBp.newBuilder()
                            .setStartTime(startTime)
                            .setEndTime(endTime)
                            .build()
                    )
                    .build()

                ActivityType.BIA_MEASUREMENT -> ActivityResult.newBuilder()
                    .setBiaMeasurement(
                        ActivityResult.BiaMeasurement.newBuilder()
                            .setStartTime(startTime)
                            .setEndTime(endTime)
                            .build()
                    )
                    .build()

                ActivityType.BP_MEASUREMENT -> ActivityResult.newBuilder()
                    .setBpMeasurement(
                        ActivityResult.BpMeasurement.newBuilder()
                            .setStartTime(startTime)
                            .setEndTime(endTime)
                            .build()
                    )
                    .build()

                ActivityType.ECG_MEASUREMENT -> ActivityResult.newBuilder()
                    .setEcgMeasurement(
                        ActivityResult.EcgMeasurement.newBuilder()
                            .setStartTime(startTime)
                            .setEndTime(endTime)
                            .build()
                    )
                    .build()

                ActivityType.PPG_MEASUREMENT -> ActivityResult.newBuilder()
                    .setPpgMeasurement(
                        ActivityResult.PpgMeasurement.newBuilder()
                            .setStartTime(startTime)
                            .setEndTime(endTime)
                            .build()
                    )
                    .build()

                ActivityType.SPO2_MEASUREMENT -> ActivityResult.newBuilder()
                    .setSpo2Measurement(
                        ActivityResult.Spo2Measurement.newBuilder()
                            .setStartTime(startTime)
                            .setEndTime(endTime)
                            .build()
                    )
                    .build()

                ActivityType.BP_AND_BIA_MEASUREMENT -> ActivityResult.newBuilder()
                    .setBpAndBiaMeasurement(
                        ActivityResult.BpAndBiaMeasurement.newBuilder()
                            .setStartTime(startTime)
                            .setEndTime(endTime)
                            .build()
                    )
                    .build()

                ActivityType.STABLE_MEASUREMENT -> ActivityResult.newBuilder()
                    .setStableMeasurement(
                        ActivityResult.StableMeasurement.newBuilder()
                            .setStartTime(startTime)
                            .setEndTime(endTime)
                            .build()
                    )
                    .build()

                ActivityType.SHAPE_PAINTING -> ActivityResult.newBuilder()
                    .setShapePainting(ActivityResult.ShapePainting.newBuilder().setResult(result).build())
                    .build()

                ActivityType.CATCH_LADYBUG -> ActivityResult.newBuilder()
                    .setCatchLadybug(ActivityResult.CatchLadybug.newBuilder().setResult(result).build())
                    .build()

                ActivityType.MEMORIZE -> ActivityResult.newBuilder()
                    .setMemorize(ActivityResult.Memorize.newBuilder().setResult(result).build())
                    .build()

                ActivityType.MEMORIZE_WORDS_START -> ActivityResult.newBuilder()
                    .setMemorizeWordsStart(ActivityResult.MemorizeWordsStart.newBuilder().setResult(result).build())
                    .build()

                ActivityType.MEMORIZE_WORDS_END -> ActivityResult.newBuilder()
                    .setMemorizeWordsEnd(ActivityResult.MemorizeWordsEnd.newBuilder().setResult(result).build())
                    .build()

                ActivityType.DESCRIBE_IMAGE -> ActivityResult.newBuilder()
                    .setDescribeImage(ActivityResult.DescribeImage.newBuilder().setResult(result).build())
                    .build()

                ActivityType.READ_TEXT_ALOUD -> ActivityResult.newBuilder()
                    .setReadTextAloud(ActivityResult.ReadTextAloud.newBuilder().setResult(result).build())
                    .build()

                ActivityType.ANSWER_VERBALLY -> ActivityResult.newBuilder()
                    .setAnswerVerbally(ActivityResult.AnswerVerbally.newBuilder().setResult(result).build())
                    .build()

                ActivityType.ANSWER_WRITTEN -> ActivityResult.newBuilder()
                    .setAnswerWritten(ActivityResult.AnswerWritten.newBuilder().setResult(result).build())
                    .build()

                ActivityType.UNSPECIFIED -> throw IllegalArgumentException("Unsupported activity result type")
            }
        }
    }
}
