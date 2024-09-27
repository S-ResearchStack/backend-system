package researchstack.backend.application.port.incoming.task

data class TaskResultCommand(
    val studyId: String,
    val taskId: String,
    val result: Result,
    val startedAt: Long,
    val finishedAt: Long,
    val timeOffset: Int
) {
    sealed interface Result

    class SurveyResult(val questionResults: List<QuestionResult>) : Result {
        data class QuestionResult(
            val id: String,
            val result: String
        )
    }

    class ActivityResult(val data: Data) : Result {
        sealed interface Data

        data class TappingSpeed(val result: String) : Data

        data class ReactionTime(val result: String) : Data

        data class GuidedBreathing(val result: String) : Data

        data class RangeOfMotion(val result: String) : Data

        data class GaitAndBalance(val result: String) : Data

        data class StroopTest(val result: String) : Data

        data class SpeechRecognition(val result: String) : Data

        data class MobileSpirometry(val filePath: String) : Data

        data class SustainedPhonation(val result: String) : Data

        data class FiveMeterWalkTest(val startTime: String, val endTime: String) : Data

        data class StateBalanceTest(val startTime: String, val endTime: String) : Data

        data class RombergTest(val startTime: String, val endTime: String) : Data

        data class SitToStand(val startTime: String, val endTime: String) : Data

        data class OrthostaticBp(val startTime: String, val endTime: String) : Data

        data class BiaMeasurement(val startTime: String, val endTime: String) : Data

        data class BpMeasurement(val startTime: String, val endTime: String) : Data

        data class EcgMeasurement(val startTime: String, val endTime: String) : Data

        data class PpgMeasurement(val startTime: String, val endTime: String) : Data

        data class Spo2Measurement(val startTime: String, val endTime: String) : Data

        data class BpAndBiaMeasurement(val startTime: String, val endTime: String) : Data

        data class StableMeasurement(val startTime: String, val endTime: String) : Data

        data class ShapePainting(val result: String) : Data

        data class CatchLadyBug(val result: String) : Data

        data class Memorize(val result: String) : Data

        data class MemorizeWordsStart(val result: String) : Data

        data class MemorizeWordsEnd(val result: String) : Data

        data class DescribeImage(val result: String) : Data

        data class ReadTextAloud(val result: String) : Data

        data class AnswerVerbally(val result: String) : Data

        data class AnswerWritten(val result: String) : Data
    }
}
