package researchstack.backend.adapter.incoming.mapper.task

import researchstack.backend.adapter.incoming.mapper.toEpochMilli
import researchstack.backend.adapter.incoming.mapper.toStringWithTimeZone
import researchstack.backend.application.port.incoming.task.TaskResultCommand
import researchstack.backend.application.port.incoming.task.TaskResultCommand.Result
import researchstack.backend.grpc.TaskResult as GrpcTaskResult

fun GrpcTaskResult.toCommand(): TaskResultCommand {
    val result: Result = if (hasActivityResult()) {
        activityResult.toCommand()
    } else {
        surveyResult.toCommand()
    }
    return TaskResultCommand(
        studyId = studyId,
        taskId = taskId,
        startedAt = startedAt.toEpochMilli(),
        finishedAt = finishedAt.toEpochMilli(),
        result = result,
        timeOffset = timeOffset
    )
}

private fun GrpcTaskResult.ActivityResult.toCommand(): TaskResultCommand.ActivityResult {
    return TaskResultCommand.ActivityResult(
        data = when {
            hasTappingSpeed() -> tappingSpeed.toCommand()
            hasReactionTime() -> reactionTime.toCommand()
            hasGuidedBreathing() -> guidedBreathing.toCommand()
            hasRangeOfMotion() -> rangeOfMotion.toCommand()
            hasGaitAndBalance() -> gaitAndBalance.toCommand()
            hasStroopTest() -> stroopTest.toCommand()
            hasSpeechRecognition() -> speechRecognition.toCommand()
            hasMobileSpirometry() -> mobileSpirometry.toCommand()
            hasSustainedPhonation() -> sustainedPhonation.toCommand()
            hasFiveMeterWalkTest() -> fiveMeterWalkTest.toCommand()
            hasStateBalanceTest() -> stateBalanceTest.toCommand()
            hasRombergTest() -> rombergTest.toCommand()
            hasSitToStand() -> sitToStand.toCommand()
            hasOrthostaticBp() -> orthostaticBp.toCommand()
            hasBiaMeasurement() -> biaMeasurement.toCommand()
            hasBpMeasurement() -> bpMeasurement.toCommand()
            hasEcgMeasurement() -> ecgMeasurement.toCommand()
            hasPpgMeasurement() -> ppgMeasurement.toCommand()
            hasSpo2Measurement() -> spo2Measurement.toCommand()
            hasBpAndBiaMeasurement() -> bpAndBiaMeasurement.toCommand()
            hasStableMeasurement() -> stableMeasurement.toCommand()
            hasShapePainting() -> shapePainting.toCommand()
            hasCatchLadybug() -> catchLadybug.toCommand()
            hasMemorize() -> memorize.toCommand()
            hasMemorizeWordsStart() -> memorizeWordsStart.toCommand()
            hasMemorizeWordsEnd() -> memorizeWordsEnd.toCommand()
            hasDescribeImage() -> describeImage.toCommand()
            hasReadTextAloud() -> readTextAloud.toCommand()
            hasAnswerVerbally() -> answerVerbally.toCommand()
            hasAnswerWritten() -> answerWritten.toCommand()
            else -> throw IllegalArgumentException("unsupported activity result type")
        }
    )
}

// TODO: It must be modified according to each data type
private fun GrpcTaskResult.ActivityResult.TappingSpeed.toCommand(): TaskResultCommand.ActivityResult.TappingSpeed =
    TaskResultCommand.ActivityResult.TappingSpeed(result = result)

private fun GrpcTaskResult.ActivityResult.ReactionTime.toCommand(): TaskResultCommand.ActivityResult.ReactionTime =
    TaskResultCommand.ActivityResult.ReactionTime(result = result)

private fun GrpcTaskResult.ActivityResult.GuidedBreathing.toCommand(): TaskResultCommand.ActivityResult.GuidedBreathing =
    TaskResultCommand.ActivityResult.GuidedBreathing(result = result)

private fun GrpcTaskResult.ActivityResult.RangeOfMotion.toCommand(): TaskResultCommand.ActivityResult.RangeOfMotion =
    TaskResultCommand.ActivityResult.RangeOfMotion(result = result)

private fun GrpcTaskResult.ActivityResult.GaitAndBalance.toCommand(): TaskResultCommand.ActivityResult.GaitAndBalance =
    TaskResultCommand.ActivityResult.GaitAndBalance(result = result)

private fun GrpcTaskResult.ActivityResult.StroopTest.toCommand(): TaskResultCommand.ActivityResult.StroopTest =
    TaskResultCommand.ActivityResult.StroopTest(result = result)

private fun GrpcTaskResult.ActivityResult.SpeechRecognition.toCommand(): TaskResultCommand.ActivityResult.SpeechRecognition =
    TaskResultCommand.ActivityResult.SpeechRecognition(result = result)

private fun GrpcTaskResult.ActivityResult.MobileSpirometry.toCommand(): TaskResultCommand.ActivityResult.MobileSpirometry =
    TaskResultCommand.ActivityResult.MobileSpirometry(filePath = filePath)

private fun GrpcTaskResult.ActivityResult.SustainedPhonation.toCommand(): TaskResultCommand.ActivityResult.SustainedPhonation =
    TaskResultCommand.ActivityResult.SustainedPhonation(result = result)

private fun GrpcTaskResult.ActivityResult.FiveMeterWalkTest.toCommand(): TaskResultCommand.ActivityResult.FiveMeterWalkTest =
    TaskResultCommand.ActivityResult.FiveMeterWalkTest(
        startTime = startTime.toStringWithTimeZone(),
        endTime = endTime.toStringWithTimeZone()
    )

private fun GrpcTaskResult.ActivityResult.StateBalanceTest.toCommand(): TaskResultCommand.ActivityResult.StateBalanceTest =
    TaskResultCommand.ActivityResult.StateBalanceTest(
        startTime = startTime.toStringWithTimeZone(),
        endTime = endTime.toStringWithTimeZone()
    )

private fun GrpcTaskResult.ActivityResult.RombergTest.toCommand(): TaskResultCommand.ActivityResult.RombergTest =
    TaskResultCommand.ActivityResult.RombergTest(
        startTime = startTime.toStringWithTimeZone(),
        endTime = endTime.toStringWithTimeZone()
    )

private fun GrpcTaskResult.ActivityResult.SitToStand.toCommand(): TaskResultCommand.ActivityResult.SitToStand =
    TaskResultCommand.ActivityResult.SitToStand(
        startTime = startTime.toStringWithTimeZone(),
        endTime = endTime.toStringWithTimeZone()
    )

private fun GrpcTaskResult.ActivityResult.OrthostaticBp.toCommand(): TaskResultCommand.ActivityResult.OrthostaticBp =
    TaskResultCommand.ActivityResult.OrthostaticBp(
        startTime = startTime.toStringWithTimeZone(),
        endTime = endTime.toStringWithTimeZone()
    )

private fun GrpcTaskResult.ActivityResult.BiaMeasurement.toCommand(): TaskResultCommand.ActivityResult.BiaMeasurement =
    TaskResultCommand.ActivityResult.BiaMeasurement(
        startTime = startTime.toStringWithTimeZone(),
        endTime = endTime.toStringWithTimeZone()
    )

private fun GrpcTaskResult.ActivityResult.BpMeasurement.toCommand(): TaskResultCommand.ActivityResult.BpMeasurement =
    TaskResultCommand.ActivityResult.BpMeasurement(
        startTime = startTime.toStringWithTimeZone(),
        endTime = endTime.toStringWithTimeZone()
    )

private fun GrpcTaskResult.ActivityResult.EcgMeasurement.toCommand(): TaskResultCommand.ActivityResult.EcgMeasurement =
    TaskResultCommand.ActivityResult.EcgMeasurement(
        startTime = startTime.toStringWithTimeZone(),
        endTime = endTime.toStringWithTimeZone()
    )

private fun GrpcTaskResult.ActivityResult.PpgMeasurement.toCommand(): TaskResultCommand.ActivityResult.PpgMeasurement =
    TaskResultCommand.ActivityResult.PpgMeasurement(
        startTime = startTime.toStringWithTimeZone(),
        endTime = endTime.toStringWithTimeZone()
    )

private fun GrpcTaskResult.ActivityResult.Spo2Measurement.toCommand(): TaskResultCommand.ActivityResult.Spo2Measurement =
    TaskResultCommand.ActivityResult.Spo2Measurement(
        startTime = startTime.toStringWithTimeZone(),
        endTime = endTime.toStringWithTimeZone()
    )

private fun GrpcTaskResult.ActivityResult.BpAndBiaMeasurement.toCommand(): TaskResultCommand.ActivityResult.BpAndBiaMeasurement =
    TaskResultCommand.ActivityResult.BpAndBiaMeasurement(
        startTime = startTime.toStringWithTimeZone(),
        endTime = endTime.toStringWithTimeZone()
    )

private fun GrpcTaskResult.ActivityResult.StableMeasurement.toCommand(): TaskResultCommand.ActivityResult.StableMeasurement =
    TaskResultCommand.ActivityResult.StableMeasurement(
        startTime = startTime.toStringWithTimeZone(),
        endTime = endTime.toStringWithTimeZone()
    )

private fun GrpcTaskResult.ActivityResult.ShapePainting.toCommand(): TaskResultCommand.ActivityResult.ShapePainting =
    TaskResultCommand.ActivityResult.ShapePainting(result = result)

private fun GrpcTaskResult.ActivityResult.CatchLadybug.toCommand(): TaskResultCommand.ActivityResult.CatchLadyBug =
    TaskResultCommand.ActivityResult.CatchLadyBug(result = result)

private fun GrpcTaskResult.ActivityResult.Memorize.toCommand(): TaskResultCommand.ActivityResult.Memorize =
    TaskResultCommand.ActivityResult.Memorize(result = result)

private fun GrpcTaskResult.ActivityResult.MemorizeWordsStart.toCommand(): TaskResultCommand.ActivityResult.MemorizeWordsStart =
    TaskResultCommand.ActivityResult.MemorizeWordsStart(result = result)

private fun GrpcTaskResult.ActivityResult.MemorizeWordsEnd.toCommand(): TaskResultCommand.ActivityResult.MemorizeWordsEnd =
    TaskResultCommand.ActivityResult.MemorizeWordsEnd(result = result)

private fun GrpcTaskResult.ActivityResult.DescribeImage.toCommand(): TaskResultCommand.ActivityResult.DescribeImage =
    TaskResultCommand.ActivityResult.DescribeImage(result = result)

private fun GrpcTaskResult.ActivityResult.ReadTextAloud.toCommand(): TaskResultCommand.ActivityResult.ReadTextAloud =
    TaskResultCommand.ActivityResult.ReadTextAloud(result = result)

private fun GrpcTaskResult.ActivityResult.AnswerVerbally.toCommand(): TaskResultCommand.ActivityResult.AnswerVerbally =
    TaskResultCommand.ActivityResult.AnswerVerbally(result = result)

private fun GrpcTaskResult.ActivityResult.AnswerWritten.toCommand(): TaskResultCommand.ActivityResult.AnswerWritten =
    TaskResultCommand.ActivityResult.AnswerWritten(result = result)

private fun GrpcTaskResult.SurveyResult.toCommand(): TaskResultCommand.SurveyResult {
    return TaskResultCommand.SurveyResult(
        questionResults = questionResultsList.map {
            TaskResultCommand.SurveyResult.QuestionResult(
                id = it.id,
                result = it.result
            )
        }
    )
}
