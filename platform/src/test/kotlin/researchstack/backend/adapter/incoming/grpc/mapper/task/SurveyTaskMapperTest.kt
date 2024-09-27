package researchstack.backend.adapter.incoming.grpc.mapper.task

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.incoming.mapper.task.toGrpc
import researchstack.backend.adapter.incoming.mapper.task.toResponse
import researchstack.backend.adapter.incoming.mapper.toGrpc
import researchstack.backend.application.port.incoming.task.Section
import researchstack.backend.application.port.incoming.task.SurveyTaskResponse
import researchstack.backend.domain.task.Question
import researchstack.backend.enums.QuestionTag
import researchstack.backend.grpc.SurveyTask
import kotlin.test.assertEquals
import researchstack.backend.application.port.incoming.task.Question as IncomingQuestion

internal class SurveyTaskMapperTest {
    private val id = "test-id"
    private val title = "Lorem"
    private val explanation = "Lorem ipsum dolor sit amet."

    @Test
    @Tag(POSITIVE_TEST)
    fun `SurveyTask's toResponse should work properly`() {
        assertEquals(
            createSurveyTaskResponse(
                QuestionTag.RADIO,
                IncomingQuestion.ChoiceProperties(
                    listOf(
                        IncomingQuestion.Option("v", "l")
                    )
                )
            ).toGrpc(),
            createSurveyTask(
                QuestionTag.RADIO,
                Question.ChoiceProperties(
                    listOf(
                        Question.Option("v", "l")
                    )
                )
            ).toResponse().toGrpc()
        )
        assertEquals(
            createSurveyTaskResponse(
                QuestionTag.SLIDER,
                IncomingQuestion.ScaleProperties(0, 10, "ll", "hl")
            ).toGrpc(),
            createSurveyTask(
                QuestionTag.SLIDER,
                Question.ScaleProperties(0, 10, "ll", "hl")
            ).toResponse().toGrpc()
        )
        assertEquals(
            createSurveyTaskResponse(
                QuestionTag.TEXT,
                IncomingQuestion.TextProperties()
            ).toGrpc(),
            createSurveyTask(
                QuestionTag.TEXT,
                Question.TextProperties()
            ).toResponse().toGrpc()
        )
        assertEquals(
            createSurveyTaskResponse(
                QuestionTag.RANKING,
                IncomingQuestion.RankingProperties(listOf())
            ).toGrpc(),
            createSurveyTask(
                QuestionTag.RANKING,
                Question.RankingProperties(listOf())
            ).toResponse().toGrpc()
        )
        assertEquals(
            createSurveyTaskResponse(
                QuestionTag.DATETIME,
                IncomingQuestion.DateTimeProperties(isTime = true, isDate = false, isRange = true)
            ).toGrpc(),
            createSurveyTask(
                QuestionTag.DATETIME,
                Question.DateTimeProperties(isTime = true, isDate = false, isRange = true)
            ).toResponse().toGrpc()
        )
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `SurveyTaskResponse's toGrpc should work properly`() {
        assertEquals(
            createGrpcSurveyTask(
                QuestionTag.RADIO,
                Question.ChoiceProperties(
                    listOf(
                        Question.Option("v", "l")
                    )
                )
            ),
            createSurveyTaskResponse(
                QuestionTag.RADIO,
                IncomingQuestion.ChoiceProperties(
                    listOf(
                        IncomingQuestion.Option("v", "l")
                    )
                )
            ).toGrpc()
        )
        assertEquals(
            createGrpcSurveyTask(QuestionTag.SLIDER, Question.ScaleProperties(0, 10, "ll", "hl")),
            createSurveyTaskResponse(QuestionTag.SLIDER, IncomingQuestion.ScaleProperties(0, 10, "ll", "hl")).toGrpc()
        )
        assertEquals(
            createGrpcSurveyTask(QuestionTag.TEXT, Question.TextProperties()),
            createSurveyTaskResponse(QuestionTag.TEXT, IncomingQuestion.TextProperties()).toGrpc()
        )
        assertEquals(
            createGrpcSurveyTask(QuestionTag.RANKING, Question.RankingProperties(listOf())),
            createSurveyTaskResponse(QuestionTag.RANKING, IncomingQuestion.RankingProperties(listOf())).toGrpc()
        )
        assertEquals(
            createGrpcSurveyTask(
                QuestionTag.DATETIME,
                Question.DateTimeProperties(isTime = true, isDate = false, isRange = true)
            ),
            createSurveyTaskResponse(
                QuestionTag.DATETIME,
                IncomingQuestion.DateTimeProperties(isTime = true, isDate = false, isRange = true)
            ).toGrpc()
        )
    }

    private fun createSurveyTask(questionTag: QuestionTag, properties: Question.ItemProperties) =
        researchstack.backend.domain.task.SurveyTask(
            listOf(
                researchstack.backend.domain.task.Section(
                    listOf(
                        Question(
                            id,
                            title,
                            explanation,
                            questionTag,
                            properties,
                            true,
                            null
                        )
                    )
                )
            )
        )

    private fun createSurveyTaskResponse(questionTag: QuestionTag, properties: IncomingQuestion.ItemProperties) =
        SurveyTaskResponse(
            listOf(
                Section(
                    listOf(
                        IncomingQuestion(
                            id,
                            title,
                            explanation,
                            questionTag,
                            properties,
                            true
                        )
                    )
                )
            )
        )

    private fun createGrpcSurveyTask(questionTag: QuestionTag, itemProperties: Question.ItemProperties): SurveyTask {
        val questionBuilder = researchstack.backend.grpc.Question.newBuilder()
            .setId(id)
            .setTitle(title)
            .setExplanation(explanation)
            .setTag(questionTag.toGrpc())
            .setRequired(true)

        when (itemProperties) {
            is Question.ChoiceProperties ->
                questionBuilder.choiceProperties =
                    researchstack.backend.grpc.Question.ChoiceProperties.newBuilder()
                        .addAllOptions(
                            itemProperties.options.map {
                                researchstack.backend.grpc.Question.Option.newBuilder()
                                    .setValue(it.value)
                                    .setLabel(it.label)
                                    .build()
                            }
                        ).build()

            is Question.ScaleProperties ->
                questionBuilder.scaleProperties =
                    researchstack.backend.grpc.Question.ScaleProperties.newBuilder()
                        .setLow(itemProperties.low)
                        .setHigh(itemProperties.high)
                        .setLowLabel(itemProperties.lowLabel)
                        .setHighLabel(itemProperties.highLabel)
                        .build()

            is Question.TextProperties ->
                questionBuilder.textProperties =
                    researchstack.backend.grpc.Question.TextProperties.newBuilder().build()

            is Question.RankingProperties ->
                questionBuilder.rankingProperties =
                    researchstack.backend.grpc.Question.RankingProperties.newBuilder()
                        .addAllOptions(
                            itemProperties.options.map {
                                researchstack.backend.grpc.Question.Option.newBuilder()
                                    .setValue(it.value)
                                    .setLabel(it.label)
                                    .build()
                            }
                        ).build()

            is Question.DateTimeProperties ->
                questionBuilder.dateTimeProperties =
                    researchstack.backend.grpc.Question.DateTimeProperties.newBuilder()
                        .setIsDate(itemProperties.isDate)
                        .setIsRange(itemProperties.isRange)
                        .setIsTime(itemProperties.isTime)
                        .build()
        }

        return SurveyTask.newBuilder()
            .addAllSections(
                listOf(
                    researchstack.backend.grpc.Section.newBuilder()
                        .addAllQuestions(
                            listOf(
                                questionBuilder.build()
                            )
                        ).build()
                )
            ).build()
    }
}
