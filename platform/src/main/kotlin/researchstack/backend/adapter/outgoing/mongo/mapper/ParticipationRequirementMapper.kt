package researchstack.backend.adapter.outgoing.mongo.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import researchstack.backend.adapter.outgoing.mongo.entity.study.ParticipationRequirementEntity
import researchstack.backend.adapter.outgoing.mongo.entity.task.TaskSpecEntity
import researchstack.backend.domain.study.EligibilityTest
import researchstack.backend.domain.study.ParticipationRequirement
import researchstack.backend.domain.task.SurveyTask

@Mapper
abstract class ParticipationRequirementMapper {
    abstract fun toEntity(
        participationRequirement: ParticipationRequirement,
        studyId: String
    ): ParticipationRequirementEntity

    abstract fun toDomain(participationRequirementEntity: ParticipationRequirementEntity): ParticipationRequirement

    fun mapAnswer(answer: EligibilityTest.Answer): ParticipationRequirementEntity.EligibilityTest.Answer {
        return ParticipationRequirementEntity.EligibilityTest.Answer(
            answer.questionId,
            answer.type,
            when (answer.item) {
                is EligibilityTest.ScaleAnswer -> toEntity(answer.item)
                is EligibilityTest.ChoiceAnswer -> toEntity(answer.item)
                is EligibilityTest.DateTimeAnswer -> toEntity(answer.item)
                is EligibilityTest.TextAnswer -> toEntity(answer.item)
                is EligibilityTest.RankingAnswer -> toEntity(answer.item)
            }
        )
    }

    abstract fun toEntity(scaleAnswer: EligibilityTest.ScaleAnswer): ParticipationRequirementEntity.EligibilityTest.ScaleAnswer

    abstract fun toEntity(choiceAnswer: EligibilityTest.ChoiceAnswer): ParticipationRequirementEntity.EligibilityTest.ChoiceAnswer

    abstract fun toEntity(dateTimeAnswer: EligibilityTest.DateTimeAnswer): ParticipationRequirementEntity.EligibilityTest.DateTimeAnswer

    abstract fun toEntity(textAnswer: EligibilityTest.TextAnswer): ParticipationRequirementEntity.EligibilityTest.TextAnswer

    abstract fun toEntity(rankingAnswer: EligibilityTest.RankingAnswer): ParticipationRequirementEntity.EligibilityTest.RankingAnswer

    fun toEntity(surveyTask: SurveyTask): TaskSpecEntity.SurveyTask {
        return Mappers.getMapper(TaskSpecMapper::class.java).toEntity(surveyTask)
    }

    fun mapAnswer(answer: ParticipationRequirementEntity.EligibilityTest.Answer): EligibilityTest.Answer {
        return EligibilityTest.Answer(
            answer.questionId,
            answer.type,
            when (answer.item) {
                is ParticipationRequirementEntity.EligibilityTest.ScaleAnswer -> toDomain(answer.item)
                is ParticipationRequirementEntity.EligibilityTest.ChoiceAnswer -> toDomain(answer.item)
                is ParticipationRequirementEntity.EligibilityTest.DateTimeAnswer -> toDomain(answer.item)
                is ParticipationRequirementEntity.EligibilityTest.TextAnswer -> toDomain(answer.item)
                is ParticipationRequirementEntity.EligibilityTest.RankingAnswer -> toDomain(answer.item)
            }
        )
    }

    abstract fun toDomain(scaleAnswer: ParticipationRequirementEntity.EligibilityTest.ScaleAnswer): EligibilityTest.ScaleAnswer

    abstract fun toDomain(choiceAnswer: ParticipationRequirementEntity.EligibilityTest.ChoiceAnswer): EligibilityTest.ChoiceAnswer

    abstract fun toDomain(dateTimeAnswer: ParticipationRequirementEntity.EligibilityTest.DateTimeAnswer): EligibilityTest.DateTimeAnswer

    abstract fun toDomain(textAnswer: ParticipationRequirementEntity.EligibilityTest.TextAnswer): EligibilityTest.TextAnswer

    abstract fun toDomain(rankingAnswer: ParticipationRequirementEntity.EligibilityTest.RankingAnswer): EligibilityTest.RankingAnswer

    fun toDomain(surveyTask: TaskSpecEntity.SurveyTask): SurveyTask {
        return Mappers.getMapper(TaskSpecMapper::class.java).toDomain(surveyTask)
    }
}

private val converter = Mappers.getMapper(ParticipationRequirementMapper::class.java)

fun ParticipationRequirement.toEntity(studyId: String): ParticipationRequirementEntity =
    converter.toEntity(this, studyId)

fun ParticipationRequirementEntity.toDomain(): ParticipationRequirement = converter.toDomain(this)
