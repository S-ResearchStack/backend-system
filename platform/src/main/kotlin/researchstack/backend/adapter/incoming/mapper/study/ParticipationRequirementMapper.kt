package researchstack.backend.adapter.incoming.mapper.study

import researchstack.backend.adapter.incoming.mapper.task.toGrpc
import researchstack.backend.adapter.incoming.mapper.task.toResponse
import researchstack.backend.adapter.incoming.mapper.toGrpc
import researchstack.backend.application.port.incoming.study.ParticipationRequirementResponse
import researchstack.backend.application.port.incoming.study.ParticipationRequirementResponse.EligibilityTestResponse
import researchstack.backend.application.port.incoming.study.ParticipationRequirementResponse.InformedConsentResponse
import researchstack.backend.domain.study.DataSpec
import researchstack.backend.domain.study.EligibilityTest
import researchstack.backend.domain.study.InformedConsent
import researchstack.backend.grpc.EligibilityTest as GrpcEligibilityTest
import researchstack.backend.grpc.InformedConsent as GrpcInformedConsent

fun EligibilityTest.toResponse(): EligibilityTestResponse =
    EligibilityTestResponse(
        surveyTask = surveyTask.toResponse(),
        answers = answers.map { answer ->
            EligibilityTestResponse.Answer(
                questionId = answer.questionId,
                item = answer.item.toResponse()
            )
        }
    )

private fun EligibilityTest.AnswerItem.toResponse(): EligibilityTestResponse.AnswerItem =
    when (this) {
        is EligibilityTest.ChoiceAnswer -> toResponse()
        is EligibilityTest.ScaleAnswer -> toResponse()
        is EligibilityTest.TextAnswer -> toResponse()
        is EligibilityTest.RankingAnswer -> toResponse()
        is EligibilityTest.DateTimeAnswer -> toResponse()
    }

private fun EligibilityTest.ChoiceAnswer.toResponse(): EligibilityTestResponse.ChoiceAnswer =
    EligibilityTestResponse.ChoiceAnswer(
        options = options.map { option ->
            EligibilityTestResponse.ChoiceAnswer.Option(
                value = option.value,
                label = option.label
            )
        }
    )

private fun EligibilityTest.ScaleAnswer.toResponse(): EligibilityTestResponse.ScaleAnswer =
    EligibilityTestResponse.ScaleAnswer(
        from = from,
        to = to
    )

private fun EligibilityTest.TextAnswer.toResponse(): EligibilityTestResponse.TextAnswer =
    EligibilityTestResponse.TextAnswer(
        answers = answers
    )

private fun EligibilityTest.RankingAnswer.toResponse(): EligibilityTestResponse.RankingAnswer =
    EligibilityTestResponse.RankingAnswer(
        answers = answers
    )

private fun EligibilityTest.DateTimeAnswer.toResponse(): EligibilityTestResponse.DateTimeAnswer =
    EligibilityTestResponse.DateTimeAnswer(
        fromDate = fromDate,
        toDate = toDate,
        fromTime = fromTime,
        toTime = toTime
    )

fun InformedConsent.toResponse(): InformedConsentResponse =
    InformedConsentResponse(
        imagePath = imagePath
    )

fun DataSpec.toResponse(): ParticipationRequirementResponse.DataSpecResponse =
    ParticipationRequirementResponse.DataSpecResponse(
        dataId = dataId,
        dataName = dataName,
        dataDescription = dataDescription,
        collectionMethod = collectionMethod,
        targetTrialNumber = targetTrialNumber,
        durationThreshold = durationThreshold
    )

fun EligibilityTestResponse.toGrpc(): GrpcEligibilityTest =
    GrpcEligibilityTest.newBuilder()
        .setSurveyTask(surveyTask.toGrpc())
        .addAllAnswers(
            answers.map {
                val res = GrpcEligibilityTest.Answer.newBuilder().setQuestionId(it.questionId)
                when (it.item) {
                    is EligibilityTestResponse.ChoiceAnswer -> res.choiceAnswers = it.item.toGrpc()
                    is EligibilityTestResponse.ScaleAnswer -> res.scaleAnswers = it.item.toGrpc()
                    is EligibilityTestResponse.TextAnswer -> res.textAnswers = it.item.toGrpc()
                    is EligibilityTestResponse.RankingAnswer -> res.rankingAnswers = it.item.toGrpc()
                    is EligibilityTestResponse.DateTimeAnswer -> res.dateTimeAnswers = it.item.toGrpc()
                }
                res.build()
            }
        ).build()

private fun EligibilityTestResponse.ChoiceAnswer.toGrpc(): GrpcEligibilityTest.ChoiceAnswers =
    GrpcEligibilityTest.ChoiceAnswers.newBuilder()
        .addAllOptions(
            options.map { option ->
                GrpcEligibilityTest.Option.newBuilder()
                    .setValue(option.value)
                    .setLabel(option.label)
                    .build()
            }
        ).build()

private fun EligibilityTestResponse.ScaleAnswer.toGrpc(): GrpcEligibilityTest.ScaleAnswers =
    GrpcEligibilityTest.ScaleAnswers.newBuilder()
        .setFrom(from)
        .setTo(to)
        .build()

private fun EligibilityTestResponse.TextAnswer.toGrpc(): GrpcEligibilityTest.TextAnswers =
    GrpcEligibilityTest.TextAnswers.newBuilder()
        .addAllAnswers(answers)
        .build()

private fun EligibilityTestResponse.RankingAnswer.toGrpc(): GrpcEligibilityTest.RankingAnswers =
    GrpcEligibilityTest.RankingAnswers.newBuilder()
        .addAllAnswers(answers)
        .build()

private fun EligibilityTestResponse.DateTimeAnswer.toGrpc(): GrpcEligibilityTest.DateTimeAnswers =
    GrpcEligibilityTest.DateTimeAnswers.newBuilder()
        .setFromDate(fromDate.toGrpc())
        .setToDate(toDate.toGrpc())
        .setFromTime(fromTime.toGrpc())
        .setToTime(toTime.toGrpc()).build()

fun InformedConsentResponse.toGrpc(): GrpcInformedConsent =
    GrpcInformedConsent.newBuilder()
        .setImagePath(imagePath)
        .build()
