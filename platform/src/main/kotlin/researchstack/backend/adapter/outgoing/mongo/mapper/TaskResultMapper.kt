package researchstack.backend.adapter.outgoing.mongo.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import researchstack.backend.adapter.outgoing.mongo.entity.task.TaskResultEntity
import researchstack.backend.domain.task.TaskResult

@Mapper
abstract class TaskResultMapper {
    abstract fun toEntity(taskResult: TaskResult, subjectId: String): TaskResultEntity

    fun mapResult(result: TaskResult.Result): TaskResultEntity.Result {
        return when (result) {
            is TaskResult.SurveyResult -> toEntity(result)
            is TaskResult.ActivityResult -> toEntity(result)
        }
    }

    abstract fun toEntity(surveyResult: TaskResult.SurveyResult): TaskResultEntity.SurveyResult

    abstract fun toEntity(activityResult: TaskResult.ActivityResult): TaskResultEntity.ActivityResult

    fun mapData(data: TaskResult.ActivityResult.Data): TaskResultEntity.ActivityResult.Data {
        return when (data) {
            is TaskResult.ActivityResult.TappingSpeed -> toEntity(data)
            is TaskResult.ActivityResult.ReactionTime -> toEntity(data)
            is TaskResult.ActivityResult.GuidedBreathing -> toEntity(data)
            is TaskResult.ActivityResult.RangeOfMotion -> toEntity(data)
            is TaskResult.ActivityResult.GaitAndBalance -> toEntity(data)
            is TaskResult.ActivityResult.StroopTest -> toEntity(data)
            is TaskResult.ActivityResult.SpeechRecognition -> toEntity(data)
            is TaskResult.ActivityResult.MobileSpirometry -> toEntity(data)
            is TaskResult.ActivityResult.SustainedPhonation -> toEntity(data)
            is TaskResult.ActivityResult.FiveMeterWalkTest -> toEntity(data)
            is TaskResult.ActivityResult.StateBalanceTest -> toEntity(data)
            is TaskResult.ActivityResult.RombergTest -> toEntity(data)
            is TaskResult.ActivityResult.SitToStand -> toEntity(data)
            is TaskResult.ActivityResult.OrthostaticBp -> toEntity(data)
            is TaskResult.ActivityResult.BiaMeasurement -> toEntity(data)
            is TaskResult.ActivityResult.BpMeasurement -> toEntity(data)
            is TaskResult.ActivityResult.EcgMeasurement -> toEntity(data)
            is TaskResult.ActivityResult.PpgMeasurement -> toEntity(data)
            is TaskResult.ActivityResult.Spo2Measurement -> toEntity(data)
            is TaskResult.ActivityResult.BpAndBiaMeasurement -> toEntity(data)
            is TaskResult.ActivityResult.StableMeasurement -> toEntity(data)
            is TaskResult.ActivityResult.ShapePainting -> toEntity(data)
            is TaskResult.ActivityResult.CatchLadybug -> toEntity(data)
            is TaskResult.ActivityResult.Memorize -> toEntity(data)
            is TaskResult.ActivityResult.MemorizeWordsStart -> toEntity(data)
            is TaskResult.ActivityResult.MemorizeWordsEnd -> toEntity(data)
            is TaskResult.ActivityResult.DescribeImage -> toEntity(data)
            is TaskResult.ActivityResult.ReadTextAloud -> toEntity(data)
            is TaskResult.ActivityResult.AnswerVerbally -> toEntity(data)
            is TaskResult.ActivityResult.AnswerWritten -> toEntity(data)
        }
    }

    abstract fun toEntity(data: TaskResult.ActivityResult.TappingSpeed): TaskResultEntity.ActivityResult.TappingSpeed

    abstract fun toEntity(data: TaskResult.ActivityResult.ReactionTime): TaskResultEntity.ActivityResult.ReactionTime

    abstract fun toEntity(data: TaskResult.ActivityResult.GuidedBreathing): TaskResultEntity.ActivityResult.GuidedBreathing

    abstract fun toEntity(data: TaskResult.ActivityResult.RangeOfMotion): TaskResultEntity.ActivityResult.RangeOfMotion

    abstract fun toEntity(data: TaskResult.ActivityResult.GaitAndBalance): TaskResultEntity.ActivityResult.GaitAndBalance

    abstract fun toEntity(data: TaskResult.ActivityResult.StroopTest): TaskResultEntity.ActivityResult.StroopTest

    abstract fun toEntity(data: TaskResult.ActivityResult.SpeechRecognition): TaskResultEntity.ActivityResult.SpeechRecognition

    abstract fun toEntity(data: TaskResult.ActivityResult.MobileSpirometry): TaskResultEntity.ActivityResult.MobileSpirometry

    abstract fun toEntity(data: TaskResult.ActivityResult.SustainedPhonation): TaskResultEntity.ActivityResult.SustainedPhonation

    abstract fun toEntity(data: TaskResult.ActivityResult.FiveMeterWalkTest): TaskResultEntity.ActivityResult.FiveMeterWalkTest

    abstract fun toEntity(data: TaskResult.ActivityResult.StateBalanceTest): TaskResultEntity.ActivityResult.StateBalanceTest

    abstract fun toEntity(data: TaskResult.ActivityResult.RombergTest): TaskResultEntity.ActivityResult.RombergTest

    abstract fun toEntity(data: TaskResult.ActivityResult.SitToStand): TaskResultEntity.ActivityResult.SitToStand

    abstract fun toEntity(data: TaskResult.ActivityResult.OrthostaticBp): TaskResultEntity.ActivityResult.OrthostaticBp

    abstract fun toEntity(data: TaskResult.ActivityResult.BiaMeasurement): TaskResultEntity.ActivityResult.BiaMeasurement

    abstract fun toEntity(data: TaskResult.ActivityResult.BpMeasurement): TaskResultEntity.ActivityResult.BpMeasurement

    abstract fun toEntity(data: TaskResult.ActivityResult.EcgMeasurement): TaskResultEntity.ActivityResult.EcgMeasurement

    abstract fun toEntity(data: TaskResult.ActivityResult.PpgMeasurement): TaskResultEntity.ActivityResult.PpgMeasurement

    abstract fun toEntity(data: TaskResult.ActivityResult.Spo2Measurement): TaskResultEntity.ActivityResult.Spo2Measurement

    abstract fun toEntity(data: TaskResult.ActivityResult.BpAndBiaMeasurement): TaskResultEntity.ActivityResult.BpAndBiaMeasurement

    abstract fun toEntity(data: TaskResult.ActivityResult.StableMeasurement): TaskResultEntity.ActivityResult.StableMeasurement

    abstract fun toEntity(data: TaskResult.ActivityResult.ShapePainting): TaskResultEntity.ActivityResult.ShapePainting

    abstract fun toEntity(data: TaskResult.ActivityResult.CatchLadybug): TaskResultEntity.ActivityResult.CatchLadybug

    abstract fun toEntity(data: TaskResult.ActivityResult.Memorize): TaskResultEntity.ActivityResult.Memorize

    abstract fun toEntity(data: TaskResult.ActivityResult.MemorizeWordsStart): TaskResultEntity.ActivityResult.MemorizeWordsStart

    abstract fun toEntity(data: TaskResult.ActivityResult.MemorizeWordsEnd): TaskResultEntity.ActivityResult.MemorizeWordsEnd

    abstract fun toEntity(data: TaskResult.ActivityResult.DescribeImage): TaskResultEntity.ActivityResult.DescribeImage

    abstract fun toEntity(data: TaskResult.ActivityResult.ReadTextAloud): TaskResultEntity.ActivityResult.ReadTextAloud

    abstract fun toEntity(data: TaskResult.ActivityResult.AnswerVerbally): TaskResultEntity.ActivityResult.AnswerVerbally

    abstract fun toEntity(data: TaskResult.ActivityResult.AnswerWritten): TaskResultEntity.ActivityResult.AnswerWritten

    abstract fun toDomain(taskResultEntity: TaskResultEntity): TaskResult

    fun mapResult(result: TaskResultEntity.Result): TaskResult.Result {
        return when (result) {
            is TaskResultEntity.SurveyResult -> toDomain(result)
            is TaskResultEntity.ActivityResult -> toDomain(result)
        }
    }

    abstract fun toDomain(surveyResult: TaskResultEntity.SurveyResult): TaskResult.SurveyResult

    abstract fun toDomain(activityResult: TaskResultEntity.ActivityResult): TaskResult.ActivityResult

    fun mapData(data: TaskResultEntity.ActivityResult.Data): TaskResult.ActivityResult.Data {
        return when (data) {
            is TaskResultEntity.ActivityResult.TappingSpeed -> toDomain(data)
            is TaskResultEntity.ActivityResult.ReactionTime -> toDomain(data)
            is TaskResultEntity.ActivityResult.GuidedBreathing -> toDomain(data)
            is TaskResultEntity.ActivityResult.RangeOfMotion -> toDomain(data)
            is TaskResultEntity.ActivityResult.GaitAndBalance -> toDomain(data)
            is TaskResultEntity.ActivityResult.StroopTest -> toDomain(data)
            is TaskResultEntity.ActivityResult.SpeechRecognition -> toDomain(data)
            is TaskResultEntity.ActivityResult.MobileSpirometry -> toDomain(data)
            is TaskResultEntity.ActivityResult.SustainedPhonation -> toDomain(data)
            is TaskResultEntity.ActivityResult.FiveMeterWalkTest -> toDomain(data)
            is TaskResultEntity.ActivityResult.StateBalanceTest -> toDomain(data)
            is TaskResultEntity.ActivityResult.RombergTest -> toDomain(data)
            is TaskResultEntity.ActivityResult.SitToStand -> toDomain(data)
            is TaskResultEntity.ActivityResult.OrthostaticBp -> toDomain(data)
            is TaskResultEntity.ActivityResult.BiaMeasurement -> toDomain(data)
            is TaskResultEntity.ActivityResult.BpMeasurement -> toDomain(data)
            is TaskResultEntity.ActivityResult.EcgMeasurement -> toDomain(data)
            is TaskResultEntity.ActivityResult.PpgMeasurement -> toDomain(data)
            is TaskResultEntity.ActivityResult.Spo2Measurement -> toDomain(data)
            is TaskResultEntity.ActivityResult.BpAndBiaMeasurement -> toDomain(data)
            is TaskResultEntity.ActivityResult.StableMeasurement -> toDomain(data)
            is TaskResultEntity.ActivityResult.ShapePainting -> toDomain(data)
            is TaskResultEntity.ActivityResult.CatchLadybug -> toDomain(data)
            is TaskResultEntity.ActivityResult.Memorize -> toDomain(data)
            is TaskResultEntity.ActivityResult.MemorizeWordsStart -> toDomain(data)
            is TaskResultEntity.ActivityResult.MemorizeWordsEnd -> toDomain(data)
            is TaskResultEntity.ActivityResult.DescribeImage -> toDomain(data)
            is TaskResultEntity.ActivityResult.ReadTextAloud -> toDomain(data)
            is TaskResultEntity.ActivityResult.AnswerVerbally -> toDomain(data)
            is TaskResultEntity.ActivityResult.AnswerWritten -> toDomain(data)
        }
    }

    abstract fun toDomain(data: TaskResultEntity.ActivityResult.TappingSpeed): TaskResult.ActivityResult.TappingSpeed

    abstract fun toDomain(data: TaskResultEntity.ActivityResult.ReactionTime): TaskResult.ActivityResult.ReactionTime

    abstract fun toDomain(data: TaskResultEntity.ActivityResult.GuidedBreathing): TaskResult.ActivityResult.GuidedBreathing

    abstract fun toDomain(data: TaskResultEntity.ActivityResult.RangeOfMotion): TaskResult.ActivityResult.RangeOfMotion

    abstract fun toDomain(data: TaskResultEntity.ActivityResult.GaitAndBalance): TaskResult.ActivityResult.GaitAndBalance

    abstract fun toDomain(data: TaskResultEntity.ActivityResult.StroopTest): TaskResult.ActivityResult.StroopTest

    abstract fun toDomain(data: TaskResultEntity.ActivityResult.SpeechRecognition): TaskResult.ActivityResult.SpeechRecognition

    abstract fun toDomain(data: TaskResultEntity.ActivityResult.MobileSpirometry): TaskResult.ActivityResult.MobileSpirometry

    abstract fun toDomain(data: TaskResultEntity.ActivityResult.SustainedPhonation): TaskResult.ActivityResult.SustainedPhonation

    abstract fun toDomain(data: TaskResultEntity.ActivityResult.FiveMeterWalkTest): TaskResult.ActivityResult.FiveMeterWalkTest

    abstract fun toDomain(data: TaskResultEntity.ActivityResult.StateBalanceTest): TaskResult.ActivityResult.StateBalanceTest

    abstract fun toDomain(data: TaskResultEntity.ActivityResult.RombergTest): TaskResult.ActivityResult.RombergTest

    abstract fun toDomain(data: TaskResultEntity.ActivityResult.SitToStand): TaskResult.ActivityResult.SitToStand

    abstract fun toDomain(data: TaskResultEntity.ActivityResult.OrthostaticBp): TaskResult.ActivityResult.OrthostaticBp

    abstract fun toDomain(data: TaskResultEntity.ActivityResult.BiaMeasurement): TaskResult.ActivityResult.BiaMeasurement

    abstract fun toDomain(data: TaskResultEntity.ActivityResult.BpMeasurement): TaskResult.ActivityResult.BpMeasurement

    abstract fun toDomain(data: TaskResultEntity.ActivityResult.EcgMeasurement): TaskResult.ActivityResult.EcgMeasurement

    abstract fun toDomain(data: TaskResultEntity.ActivityResult.PpgMeasurement): TaskResult.ActivityResult.PpgMeasurement

    abstract fun toDomain(data: TaskResultEntity.ActivityResult.Spo2Measurement): TaskResult.ActivityResult.Spo2Measurement

    abstract fun toDomain(data: TaskResultEntity.ActivityResult.BpAndBiaMeasurement): TaskResult.ActivityResult.BpAndBiaMeasurement

    abstract fun toDomain(data: TaskResultEntity.ActivityResult.StableMeasurement): TaskResult.ActivityResult.StableMeasurement

    abstract fun toDomain(data: TaskResultEntity.ActivityResult.ShapePainting): TaskResult.ActivityResult.ShapePainting

    abstract fun toDomain(data: TaskResultEntity.ActivityResult.CatchLadybug): TaskResult.ActivityResult.CatchLadybug

    abstract fun toDomain(data: TaskResultEntity.ActivityResult.Memorize): TaskResult.ActivityResult.Memorize

    abstract fun toDomain(data: TaskResultEntity.ActivityResult.MemorizeWordsStart): TaskResult.ActivityResult.MemorizeWordsStart

    abstract fun toDomain(data: TaskResultEntity.ActivityResult.MemorizeWordsEnd): TaskResult.ActivityResult.MemorizeWordsEnd

    abstract fun toDomain(data: TaskResultEntity.ActivityResult.DescribeImage): TaskResult.ActivityResult.DescribeImage

    abstract fun toDomain(data: TaskResultEntity.ActivityResult.ReadTextAloud): TaskResult.ActivityResult.ReadTextAloud

    abstract fun toDomain(data: TaskResultEntity.ActivityResult.AnswerVerbally): TaskResult.ActivityResult.AnswerVerbally

    abstract fun toDomain(data: TaskResultEntity.ActivityResult.AnswerWritten): TaskResult.ActivityResult.AnswerWritten
}

private val converter = Mappers.getMapper(TaskResultMapper::class.java)

fun TaskResult.toEntity(subjectId: String): TaskResultEntity = converter.toEntity(this, subjectId)

fun TaskResultEntity.toDomain(): TaskResult = converter.toDomain(this)
