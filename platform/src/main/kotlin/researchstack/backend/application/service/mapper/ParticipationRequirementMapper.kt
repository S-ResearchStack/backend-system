package researchstack.backend.application.service.mapper

import kotlinx.serialization.json.Json
import org.json.JSONObject
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import researchstack.backend.application.port.incoming.study.CreateParticipationRequirementCommand
import researchstack.backend.application.port.incoming.study.UpdateParticipationRequirementCommand
import researchstack.backend.domain.study.EligibilityTest
import researchstack.backend.domain.study.ParticipationRequirement
import researchstack.backend.domain.task.Question
import researchstack.backend.enums.QuestionType

@Mapper(componentModel = "spring")
abstract class ParticipationRequirementMapper {
    companion object {
        val INSTANCE: ParticipationRequirementMapper = Mappers.getMapper(ParticipationRequirementMapper::class.java)
    }

    private val json = Json { ignoreUnknownKeys = true }

    abstract fun toDomain(command: CreateParticipationRequirementCommand): ParticipationRequirement
    abstract fun toDomain(command: UpdateParticipationRequirementCommand): ParticipationRequirement

    @Mapping(target = "surveyTask.sections", source = "sections")
    abstract fun mapEligibilityTest(data: CreateParticipationRequirementCommand.EligibilityTest): EligibilityTest

    fun mapQuestion(question: CreateParticipationRequirementCommand.EligibilityTest.Section.Question): Question =
        Question(
            question.id,
            question.title,
            question.explanation,
            question.tag,
            mapQuestionProperties(question.type, JSONObject(question.properties).toString()),
            question.required,
            question.type
        )

    fun mapAnswer(answer: CreateParticipationRequirementCommand.EligibilityTest.Answer): EligibilityTest.Answer =
        EligibilityTest.Answer(
            answer.questionId,
            answer.type,
            mapAnswerProperties(answer.type, JSONObject(answer.properties).toString())
        )

    private fun mapQuestionProperties(type: QuestionType, properties: String): Question.ItemProperties =
        when (type) {
            QuestionType.SCALE ->
                json.decodeFromString<Question.ScaleProperties>(properties)

            QuestionType.CHOICE ->
                json.decodeFromString<Question.ChoiceProperties>(properties)

            QuestionType.DATETIME ->
                json.decodeFromString<Question.DateTimeProperties>(properties)

            QuestionType.TEXT ->
                Question.TextProperties()

            QuestionType.RANKING ->
                json.decodeFromString<Question.RankingProperties>(properties)

            else ->
                throw IllegalArgumentException("Unsupported Survey Type: $type")
        }

    private fun mapAnswerProperties(type: QuestionType, properties: String): EligibilityTest.AnswerItem =
        when (type) {
            QuestionType.SCALE ->
                json.decodeFromString<EligibilityTest.ScaleAnswer>(properties)

            QuestionType.CHOICE ->
                json.decodeFromString<EligibilityTest.ChoiceAnswer>(properties)

            QuestionType.DATETIME ->
                json.decodeFromString<EligibilityTest.DateTimeAnswer>(properties)

            QuestionType.TEXT ->
                json.decodeFromString<EligibilityTest.TextAnswer>(properties)

            QuestionType.RANKING ->
                json.decodeFromString<EligibilityTest.RankingAnswer>(properties)

            else ->
                throw IllegalArgumentException("Unsupported Survey Type: $type")
        }
}
