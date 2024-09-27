package researchstack.backend.application.service.mapper

import researchstack.backend.application.port.incoming.task.TaskResultCommand
import researchstack.backend.domain.task.TaskResult
import researchstack.backend.enums.ActivityType
import researchstack.backend.enums.TaskType

fun TaskResultCommand.toDomain(): TaskResult {
    val (type, result) = when (result) {
        is TaskResultCommand.SurveyResult -> TaskType.SURVEY to result.toDomain()
        is TaskResultCommand.ActivityResult -> TaskType.ACTIVITY to result.toDomain()
    }

    return TaskResult(
        studyId = studyId,
        taskId = taskId,
        startedAt = startedAt,
        finishedAt = finishedAt,
        type = type,
        result = result,
        timeOffset = timeOffset
    )
}

private fun TaskResultCommand.SurveyResult.toDomain(): TaskResult.SurveyResult =
    TaskResult.SurveyResult(questionResults = questionResults.map { it.toDomain() })

private fun TaskResultCommand.SurveyResult.QuestionResult.toDomain(): TaskResult.SurveyResult.QuestionResult =
    TaskResult.SurveyResult.QuestionResult(id = id, result = result)

private fun TaskResultCommand.ActivityResult.toDomain(): TaskResult.ActivityResult {
    val (resultType, resultData) = when (data) {
        is TaskResultCommand.ActivityResult.TappingSpeed -> ActivityType.TAPPING_SPEED to data.toDomain()
        is TaskResultCommand.ActivityResult.ReactionTime -> ActivityType.REACTION_TIME to data.toDomain()
        is TaskResultCommand.ActivityResult.GuidedBreathing -> ActivityType.GUIDED_BREATHING to data.toDomain()
        is TaskResultCommand.ActivityResult.RangeOfMotion -> ActivityType.RANGE_OF_MOTION to data.toDomain()
        is TaskResultCommand.ActivityResult.GaitAndBalance -> ActivityType.GAIT_AND_BALANCE to data.toDomain()
        is TaskResultCommand.ActivityResult.StroopTest -> ActivityType.STROOP_TEST to data.toDomain()
        is TaskResultCommand.ActivityResult.SpeechRecognition -> ActivityType.SPEECH_RECOGNITION to data.toDomain()
        is TaskResultCommand.ActivityResult.MobileSpirometry -> ActivityType.MOBILE_SPIROMETRY to data.toDomain()
        is TaskResultCommand.ActivityResult.SustainedPhonation -> ActivityType.SUSTAINED_PHONATION to data.toDomain()
        is TaskResultCommand.ActivityResult.FiveMeterWalkTest -> ActivityType.FIVE_METER_WALK_TEST to data.toDomain()
        is TaskResultCommand.ActivityResult.StateBalanceTest -> ActivityType.STATE_BALANCE_TEST to data.toDomain()
        is TaskResultCommand.ActivityResult.RombergTest -> ActivityType.ROMBERG_TEST to data.toDomain()
        is TaskResultCommand.ActivityResult.SitToStand -> ActivityType.SIT_TO_STAND to data.toDomain()
        is TaskResultCommand.ActivityResult.OrthostaticBp -> ActivityType.ORTHOSTATIC_BP to data.toDomain()
        is TaskResultCommand.ActivityResult.BiaMeasurement -> ActivityType.BIA_MEASUREMENT to data.toDomain()
        is TaskResultCommand.ActivityResult.BpMeasurement -> ActivityType.BP_MEASUREMENT to data.toDomain()
        is TaskResultCommand.ActivityResult.EcgMeasurement -> ActivityType.ECG_MEASUREMENT to data.toDomain()
        is TaskResultCommand.ActivityResult.PpgMeasurement -> ActivityType.PPG_MEASUREMENT to data.toDomain()
        is TaskResultCommand.ActivityResult.Spo2Measurement -> ActivityType.SPO2_MEASUREMENT to data.toDomain()
        is TaskResultCommand.ActivityResult.BpAndBiaMeasurement -> ActivityType.BP_AND_BIA_MEASUREMENT to data.toDomain()
        is TaskResultCommand.ActivityResult.StableMeasurement -> ActivityType.STABLE_MEASUREMENT to data.toDomain()
        is TaskResultCommand.ActivityResult.ShapePainting -> ActivityType.SHAPE_PAINTING to data.toDomain()
        is TaskResultCommand.ActivityResult.CatchLadyBug -> ActivityType.CATCH_LADYBUG to data.toDomain()
        is TaskResultCommand.ActivityResult.Memorize -> ActivityType.MEMORIZE to data.toDomain()
        is TaskResultCommand.ActivityResult.MemorizeWordsStart -> ActivityType.MEMORIZE_WORDS_START to data.toDomain()
        is TaskResultCommand.ActivityResult.MemorizeWordsEnd -> ActivityType.MEMORIZE_WORDS_END to data.toDomain()
        is TaskResultCommand.ActivityResult.DescribeImage -> ActivityType.DESCRIBE_IMAGE to data.toDomain()
        is TaskResultCommand.ActivityResult.ReadTextAloud -> ActivityType.READ_TEXT_ALOUD to data.toDomain()
        is TaskResultCommand.ActivityResult.AnswerVerbally -> ActivityType.ANSWER_VERBALLY to data.toDomain()
        is TaskResultCommand.ActivityResult.AnswerWritten -> ActivityType.ANSWER_WRITTEN to data.toDomain()
    }
    return TaskResult.ActivityResult(type = resultType, data = resultData)
}

private fun TaskResultCommand.ActivityResult.TappingSpeed.toDomain(): TaskResult.ActivityResult.TappingSpeed =
    TaskResult.ActivityResult.TappingSpeed(result = result)

private fun TaskResultCommand.ActivityResult.ReactionTime.toDomain(): TaskResult.ActivityResult.ReactionTime =
    TaskResult.ActivityResult.ReactionTime(result = result)

private fun TaskResultCommand.ActivityResult.GuidedBreathing.toDomain(): TaskResult.ActivityResult.GuidedBreathing =
    TaskResult.ActivityResult.GuidedBreathing(result = result)

private fun TaskResultCommand.ActivityResult.RangeOfMotion.toDomain(): TaskResult.ActivityResult.RangeOfMotion =
    TaskResult.ActivityResult.RangeOfMotion(result = result)

private fun TaskResultCommand.ActivityResult.GaitAndBalance.toDomain(): TaskResult.ActivityResult.GaitAndBalance =
    TaskResult.ActivityResult.GaitAndBalance(result = result)

private fun TaskResultCommand.ActivityResult.StroopTest.toDomain(): TaskResult.ActivityResult.StroopTest =
    TaskResult.ActivityResult.StroopTest(result = result)

private fun TaskResultCommand.ActivityResult.SpeechRecognition.toDomain(): TaskResult.ActivityResult.SpeechRecognition =
    TaskResult.ActivityResult.SpeechRecognition(result = result)

private fun TaskResultCommand.ActivityResult.MobileSpirometry.toDomain(): TaskResult.ActivityResult.MobileSpirometry =
    TaskResult.ActivityResult.MobileSpirometry(filePath = filePath)

private fun TaskResultCommand.ActivityResult.SustainedPhonation.toDomain(): TaskResult.ActivityResult.SustainedPhonation =
    TaskResult.ActivityResult.SustainedPhonation(result = result)

private fun TaskResultCommand.ActivityResult.FiveMeterWalkTest.toDomain(): TaskResult.ActivityResult.FiveMeterWalkTest =
    TaskResult.ActivityResult.FiveMeterWalkTest(startTime = startTime, endTime = endTime)

private fun TaskResultCommand.ActivityResult.StateBalanceTest.toDomain(): TaskResult.ActivityResult.StateBalanceTest =
    TaskResult.ActivityResult.StateBalanceTest(startTime = startTime, endTime = endTime)

private fun TaskResultCommand.ActivityResult.RombergTest.toDomain(): TaskResult.ActivityResult.RombergTest =
    TaskResult.ActivityResult.RombergTest(startTime = startTime, endTime = endTime)

private fun TaskResultCommand.ActivityResult.SitToStand.toDomain(): TaskResult.ActivityResult.SitToStand =
    TaskResult.ActivityResult.SitToStand(startTime = startTime, endTime = endTime)

private fun TaskResultCommand.ActivityResult.OrthostaticBp.toDomain(): TaskResult.ActivityResult.OrthostaticBp =
    TaskResult.ActivityResult.OrthostaticBp(startTime = startTime, endTime = endTime)

private fun TaskResultCommand.ActivityResult.BiaMeasurement.toDomain(): TaskResult.ActivityResult.BiaMeasurement =
    TaskResult.ActivityResult.BiaMeasurement(startTime = startTime, endTime = endTime)

private fun TaskResultCommand.ActivityResult.BpMeasurement.toDomain(): TaskResult.ActivityResult.BpMeasurement =
    TaskResult.ActivityResult.BpMeasurement(startTime = startTime, endTime = endTime)

private fun TaskResultCommand.ActivityResult.EcgMeasurement.toDomain(): TaskResult.ActivityResult.EcgMeasurement =
    TaskResult.ActivityResult.EcgMeasurement(startTime = startTime, endTime = endTime)

private fun TaskResultCommand.ActivityResult.PpgMeasurement.toDomain(): TaskResult.ActivityResult.PpgMeasurement =
    TaskResult.ActivityResult.PpgMeasurement(startTime = startTime, endTime = endTime)

private fun TaskResultCommand.ActivityResult.Spo2Measurement.toDomain(): TaskResult.ActivityResult.Spo2Measurement =
    TaskResult.ActivityResult.Spo2Measurement(startTime = startTime, endTime = endTime)

private fun TaskResultCommand.ActivityResult.BpAndBiaMeasurement.toDomain(): TaskResult.ActivityResult.BpAndBiaMeasurement =
    TaskResult.ActivityResult.BpAndBiaMeasurement(startTime = startTime, endTime = endTime)

private fun TaskResultCommand.ActivityResult.StableMeasurement.toDomain(): TaskResult.ActivityResult.StableMeasurement =
    TaskResult.ActivityResult.StableMeasurement(startTime = startTime, endTime = endTime)

private fun TaskResultCommand.ActivityResult.ShapePainting.toDomain(): TaskResult.ActivityResult.ShapePainting =
    TaskResult.ActivityResult.ShapePainting(result = result)

private fun TaskResultCommand.ActivityResult.CatchLadyBug.toDomain(): TaskResult.ActivityResult.CatchLadybug =
    TaskResult.ActivityResult.CatchLadybug(result = result)

private fun TaskResultCommand.ActivityResult.Memorize.toDomain(): TaskResult.ActivityResult.Memorize =
    TaskResult.ActivityResult.Memorize(result = result)

private fun TaskResultCommand.ActivityResult.MemorizeWordsStart.toDomain(): TaskResult.ActivityResult.MemorizeWordsStart =
    TaskResult.ActivityResult.MemorizeWordsStart(result = result)

private fun TaskResultCommand.ActivityResult.MemorizeWordsEnd.toDomain(): TaskResult.ActivityResult.MemorizeWordsEnd =
    TaskResult.ActivityResult.MemorizeWordsEnd(result = result)

private fun TaskResultCommand.ActivityResult.DescribeImage.toDomain(): TaskResult.ActivityResult.DescribeImage =
    TaskResult.ActivityResult.DescribeImage(result = result)

private fun TaskResultCommand.ActivityResult.ReadTextAloud.toDomain(): TaskResult.ActivityResult.ReadTextAloud =
    TaskResult.ActivityResult.ReadTextAloud(result = result)

private fun TaskResultCommand.ActivityResult.AnswerVerbally.toDomain(): TaskResult.ActivityResult.AnswerVerbally =
    TaskResult.ActivityResult.AnswerVerbally(result = result)

private fun TaskResultCommand.ActivityResult.AnswerWritten.toDomain(): TaskResult.ActivityResult.AnswerWritten =
    TaskResult.ActivityResult.AnswerWritten(result = result)
