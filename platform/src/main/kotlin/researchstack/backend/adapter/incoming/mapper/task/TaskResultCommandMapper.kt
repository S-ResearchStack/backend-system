package researchstack.backend.adapter.incoming.mapper.task

import kotlinx.serialization.json.Json
import researchstack.backend.application.port.incoming.task.TaskResultCommand
import researchstack.backend.application.port.incoming.task.TaskResultRestCommand
import researchstack.backend.enums.ActivityType

fun TaskResultRestCommand.toTaskResultCommand(studyId: String, taskId: String): TaskResultCommand {
    val result = if (activityType != null) {
        createActivityResult(activityType, activityResult!!)
    } else {
        createSurveyResult(surveyResult!!)
    }

    return TaskResultCommand(
        studyId = studyId,
        taskId = taskId,
        startedAt = startedAt,
        finishedAt = finishedAt,
        result = result,
        timeOffset = timeOffset
    )
}

private fun createActivityResult(type: ActivityType, result: String): TaskResultCommand.ActivityResult {
    return when (type) {
        ActivityType.TAPPING_SPEED -> createTappingSpeed(result)
        ActivityType.REACTION_TIME -> createReactionTime(result)
        ActivityType.GUIDED_BREATHING -> createGuidedBreathing(result)
        ActivityType.RANGE_OF_MOTION -> createRangeOfMotion(result)
        ActivityType.GAIT_AND_BALANCE -> createGaitAndBalance(result)
        ActivityType.STROOP_TEST -> createStroopTest(result)
        ActivityType.SPEECH_RECOGNITION -> createSpeechRecognition(result)
        ActivityType.MOBILE_SPIROMETRY -> createMobileSpirometry(result)
        ActivityType.SUSTAINED_PHONATION -> createSustainedPhonation(result)
        ActivityType.FIVE_METER_WALK_TEST -> createFiveMeterWalkTest(result)
        ActivityType.STATE_BALANCE_TEST -> createStateBalanceTest(result)
        ActivityType.ROMBERG_TEST -> createRombergTest(result)
        ActivityType.SIT_TO_STAND -> createSitToStand(result)
        ActivityType.ORTHOSTATIC_BP -> createOrthostaticBp(result)
        ActivityType.BIA_MEASUREMENT -> createBiaMeasurement(result)
        ActivityType.BP_MEASUREMENT -> createBpMeasurement(result)
        ActivityType.ECG_MEASUREMENT -> createEcgMeasurement(result)
        ActivityType.PPG_MEASUREMENT -> createPpgMeasurement(result)
        ActivityType.SPO2_MEASUREMENT -> createSpo2Measurement(result)
        ActivityType.BP_AND_BIA_MEASUREMENT -> createBpAndBiaMeasurement(result)
        ActivityType.STABLE_MEASUREMENT -> createStableMeasurement(result)
        ActivityType.SHAPE_PAINTING -> createShapePainting(result)
        ActivityType.CATCH_LADYBUG -> createCatchLadyBug(result)
        ActivityType.MEMORIZE -> createMemorize(result)
        ActivityType.MEMORIZE_WORDS_START -> createMemorizeWordsStart(result)
        ActivityType.MEMORIZE_WORDS_END -> createMemorizeWordsEnd(result)
        ActivityType.DESCRIBE_IMAGE -> createDescribeImage(result)
        ActivityType.READ_TEXT_ALOUD -> createReadTextAloud(result)
        ActivityType.ANSWER_VERBALLY -> createAnswerVerbally(result)
        ActivityType.ANSWER_WRITTEN -> createAnswerWritten(result)
        else -> throw IllegalArgumentException("unsupported activity result type")
    }
}

private fun createSurveyResult(result: TaskResultRestCommand.SurveyResult): TaskResultCommand.SurveyResult {
    return TaskResultCommand.SurveyResult(
        questionResults = result.questionResults.map {
            TaskResultCommand.SurveyResult.QuestionResult(
                id = it.id,
                result = it.result
            )
        }
    )
}

private fun createTappingSpeed(result: String) =
    TaskResultCommand.ActivityResult(TaskResultCommand.ActivityResult.TappingSpeed(result))

private fun createReactionTime(result: String) =
    TaskResultCommand.ActivityResult(TaskResultCommand.ActivityResult.ReactionTime(result))

private fun createGuidedBreathing(result: String) =
    TaskResultCommand.ActivityResult(TaskResultCommand.ActivityResult.GuidedBreathing(result))

private fun createRangeOfMotion(result: String) =
    TaskResultCommand.ActivityResult(TaskResultCommand.ActivityResult.RangeOfMotion(result))

private fun createGaitAndBalance(result: String) =
    TaskResultCommand.ActivityResult(TaskResultCommand.ActivityResult.GaitAndBalance(result))

private fun createStroopTest(result: String) =
    TaskResultCommand.ActivityResult(TaskResultCommand.ActivityResult.StroopTest(result))

private fun createSpeechRecognition(result: String) =
    TaskResultCommand.ActivityResult(TaskResultCommand.ActivityResult.SpeechRecognition(result))

private fun createMobileSpirometry(result: String) =
    TaskResultCommand.ActivityResult(TaskResultCommand.ActivityResult.MobileSpirometry(result))

private fun createSustainedPhonation(result: String) =
    TaskResultCommand.ActivityResult(TaskResultCommand.ActivityResult.SustainedPhonation(result))

private fun createFiveMeterWalkTest(result: String): TaskResultCommand.ActivityResult {
    val timeData = extractStartTimeAndEndTime(result)
    return TaskResultCommand.ActivityResult(
        TaskResultCommand.ActivityResult.FiveMeterWalkTest(
            timeData.first,
            timeData.second
        )
    )
}

private fun createStateBalanceTest(result: String): TaskResultCommand.ActivityResult {
    val timeData = extractStartTimeAndEndTime(result)
    return TaskResultCommand.ActivityResult(
        TaskResultCommand.ActivityResult.StateBalanceTest(
            timeData.first,
            timeData.second
        )
    )
}

private fun createRombergTest(result: String): TaskResultCommand.ActivityResult {
    val timeData = extractStartTimeAndEndTime(result)
    return TaskResultCommand.ActivityResult(
        TaskResultCommand.ActivityResult.RombergTest(
            timeData.first,
            timeData.second
        )
    )
}

private fun createSitToStand(result: String): TaskResultCommand.ActivityResult {
    val timeData = extractStartTimeAndEndTime(result)
    return TaskResultCommand.ActivityResult(
        TaskResultCommand.ActivityResult.SitToStand(
            timeData.first,
            timeData.second
        )
    )
}

private fun createOrthostaticBp(result: String): TaskResultCommand.ActivityResult {
    val timeData = extractStartTimeAndEndTime(result)
    return TaskResultCommand.ActivityResult(
        TaskResultCommand.ActivityResult.OrthostaticBp(
            timeData.first,
            timeData.second
        )
    )
}

private fun createBiaMeasurement(result: String): TaskResultCommand.ActivityResult {
    val timeData = extractStartTimeAndEndTime(result)
    return TaskResultCommand.ActivityResult(
        TaskResultCommand.ActivityResult.BiaMeasurement(
            timeData.first,
            timeData.second
        )
    )
}

private fun createBpMeasurement(result: String): TaskResultCommand.ActivityResult {
    val timeData = extractStartTimeAndEndTime(result)
    return TaskResultCommand.ActivityResult(
        TaskResultCommand.ActivityResult.BpMeasurement(
            timeData.first,
            timeData.second
        )
    )
}

private fun createEcgMeasurement(result: String): TaskResultCommand.ActivityResult {
    val timeData = extractStartTimeAndEndTime(result)
    return TaskResultCommand.ActivityResult(
        TaskResultCommand.ActivityResult.EcgMeasurement(
            timeData.first,
            timeData.second
        )
    )
}

private fun createPpgMeasurement(result: String): TaskResultCommand.ActivityResult {
    val timeData = extractStartTimeAndEndTime(result)
    return TaskResultCommand.ActivityResult(
        TaskResultCommand.ActivityResult.PpgMeasurement(
            timeData.first,
            timeData.second
        )
    )
}

private fun createSpo2Measurement(result: String): TaskResultCommand.ActivityResult {
    val timeData = extractStartTimeAndEndTime(result)
    return TaskResultCommand.ActivityResult(
        TaskResultCommand.ActivityResult.Spo2Measurement(
            timeData.first,
            timeData.second
        )
    )
}

private fun createBpAndBiaMeasurement(result: String): TaskResultCommand.ActivityResult {
    val timeData = extractStartTimeAndEndTime(result)
    return TaskResultCommand.ActivityResult(
        TaskResultCommand.ActivityResult.BpAndBiaMeasurement(
            timeData.first,
            timeData.second
        )
    )
}

private fun createStableMeasurement(result: String): TaskResultCommand.ActivityResult {
    val timeData = extractStartTimeAndEndTime(result)
    return TaskResultCommand.ActivityResult(
        TaskResultCommand.ActivityResult.StableMeasurement(
            timeData.first,
            timeData.second
        )
    )
}

private fun createShapePainting(result: String) =
    TaskResultCommand.ActivityResult(TaskResultCommand.ActivityResult.ShapePainting(result))

private fun createCatchLadyBug(result: String) =
    TaskResultCommand.ActivityResult(TaskResultCommand.ActivityResult.CatchLadyBug(result))

private fun createMemorize(result: String) =
    TaskResultCommand.ActivityResult(TaskResultCommand.ActivityResult.Memorize(result))

private fun createMemorizeWordsStart(result: String) =
    TaskResultCommand.ActivityResult(TaskResultCommand.ActivityResult.MemorizeWordsStart(result))

private fun createMemorizeWordsEnd(result: String) =
    TaskResultCommand.ActivityResult(TaskResultCommand.ActivityResult.MemorizeWordsEnd(result))

private fun createDescribeImage(result: String) =
    TaskResultCommand.ActivityResult(TaskResultCommand.ActivityResult.DescribeImage(result))

private fun createReadTextAloud(result: String) =
    TaskResultCommand.ActivityResult(TaskResultCommand.ActivityResult.ReadTextAloud(result))

private fun createAnswerVerbally(result: String) =
    TaskResultCommand.ActivityResult(TaskResultCommand.ActivityResult.AnswerVerbally(result))

private fun createAnswerWritten(result: String) =
    TaskResultCommand.ActivityResult(TaskResultCommand.ActivityResult.AnswerWritten(result))

private fun extractStartTimeAndEndTime(result: String): Pair<String, String> {
    val jsonResult = Json.decodeFromString<Map<String, String>>(result)
    requireNotNull(jsonResult["startTime"])
    requireNotNull(jsonResult["endTime"])
    return Pair(jsonResult["startTime"]!!, jsonResult["endTime"]!!)
}
