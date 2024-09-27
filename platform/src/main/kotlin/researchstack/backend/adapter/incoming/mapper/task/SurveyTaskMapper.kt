package researchstack.backend.adapter.incoming.mapper.task

import researchstack.backend.adapter.incoming.mapper.toGrpc
import researchstack.backend.application.port.incoming.task.SurveyTaskResponse
import researchstack.backend.domain.task.Question
import researchstack.backend.domain.task.Section
import researchstack.backend.domain.task.SurveyTask
import researchstack.backend.application.port.incoming.task.Question as QuestionResponse
import researchstack.backend.application.port.incoming.task.Section as SectionResponse
import researchstack.backend.grpc.Question as GrpcQuestion
import researchstack.backend.grpc.Section as GrpcSection
import researchstack.backend.grpc.SurveyTask as GrpcSurveyTask

fun SurveyTask.toResponse(): SurveyTaskResponse =
    SurveyTaskResponse(
        sections = sections.map { section -> section.toResponse() }
    )

private fun Section.toResponse(): SectionResponse =
    SectionResponse(
        questions = questions.map { question -> question.toResponse() }
    )

private fun Question.toResponse(): QuestionResponse =
    QuestionResponse(
        id = id,
        title = title,
        explanation = explanation,
        tag = tag,
        required = required,
        itemProperties = when (itemProperties) {
            is Question.ChoiceProperties -> itemProperties.toResponse()
            is Question.ScaleProperties -> itemProperties.toResponse()
            is Question.TextProperties -> QuestionResponse.TextProperties()
            is Question.RankingProperties -> itemProperties.toResponse()
            is Question.DateTimeProperties -> itemProperties.toResponse()
        }
    )

private fun Question.ChoiceProperties.toResponse(): QuestionResponse.ChoiceProperties =
    QuestionResponse.ChoiceProperties(
        options = options.map { option -> option.toResponse() }
    )

private fun Question.ScaleProperties.toResponse(): QuestionResponse.ScaleProperties =
    QuestionResponse.ScaleProperties(
        low = low,
        high = high,
        lowLabel = lowLabel,
        highLabel = highLabel
    )

private fun Question.RankingProperties.toResponse(): QuestionResponse.RankingProperties =
    QuestionResponse.RankingProperties(
        options = options.map { option -> option.toResponse() }
    )

private fun Question.DateTimeProperties.toResponse(): QuestionResponse.DateTimeProperties =
    QuestionResponse.DateTimeProperties(
        isTime = isTime,
        isDate = isDate,
        isRange = isRange
    )

private fun Question.Option.toResponse(): QuestionResponse.Option =
    QuestionResponse.Option(
        value = value,
        label = label
    )

fun SurveyTaskResponse.toGrpc(): GrpcSurveyTask {
    return GrpcSurveyTask.newBuilder()
        .addAllSections(
            sections.map { section -> section.toGrpc() }
        )
        .build()
}

private fun SectionResponse.toGrpc(): GrpcSection {
    return GrpcSection.newBuilder()
        .addAllQuestions(
            questions.map { question -> question.toGrpc() }
        )
        .build()
}

private fun QuestionResponse.toGrpc(): GrpcQuestion {
    val grpcQuestion = GrpcQuestion.newBuilder()
        .setId(id)
        .setTitle(title)
        .setExplanation(explanation)
        .setTag(tag.toGrpc())
        .setRequired(required)

    when (itemProperties) {
        is QuestionResponse.ChoiceProperties -> grpcQuestion.setChoiceProperties(itemProperties.toGrpc())
        is QuestionResponse.ScaleProperties -> grpcQuestion.setScaleProperties(itemProperties.toGrpc())
        is QuestionResponse.TextProperties -> grpcQuestion.setTextProperties(itemProperties.toGrpc())
        is QuestionResponse.RankingProperties -> grpcQuestion.setRankingProperties(itemProperties.toGrpc())
        is QuestionResponse.DateTimeProperties -> grpcQuestion.setDateTimeProperties(itemProperties.toGrpc())
    }
    return grpcQuestion.build()
}

private fun QuestionResponse.ChoiceProperties.toGrpc(): GrpcQuestion.ChoiceProperties {
    return GrpcQuestion.ChoiceProperties.newBuilder()
        .addAllOptions(
            options.map { option -> option.toGrpc() }
        )
        .build()
}

private fun QuestionResponse.Option.toGrpc(): GrpcQuestion.Option {
    return GrpcQuestion.Option.newBuilder()
        .setValue(value)
        .setLabel(label)
        .build()
}

private fun QuestionResponse.ScaleProperties.toGrpc(): GrpcQuestion.ScaleProperties {
    return GrpcQuestion.ScaleProperties.newBuilder()
        .setLow(low)
        .setHigh(high)
        .setLowLabel(lowLabel)
        .setHighLabel(highLabel)
        .build()
}

private fun QuestionResponse.TextProperties.toGrpc(): GrpcQuestion.TextProperties {
    return GrpcQuestion.TextProperties.newBuilder()
        .build()
}

private fun QuestionResponse.RankingProperties.toGrpc(): GrpcQuestion.RankingProperties {
    return GrpcQuestion.RankingProperties.newBuilder()
        .addAllOptions(
            options.map { option -> option.toGrpc() }
        )
        .build()
}

private fun QuestionResponse.DateTimeProperties.toGrpc(): GrpcQuestion.DateTimeProperties =
    GrpcQuestion.DateTimeProperties.newBuilder()
        .setIsTime(isTime)
        .setIsDate(isDate)
        .setIsRange(isRange)
        .build()
