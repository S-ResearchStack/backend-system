package researchstack.backend.adapter.incoming.mapper.study

import researchstack.backend.application.port.incoming.study.ParticipateInStudyCommand.EligibilityTestResultCommand
import researchstack.backend.application.port.incoming.study.ParticipateInStudyCommand.SignedInformedConsentCommand
import researchstack.backend.application.port.incoming.task.TaskResultCommand.SurveyResult
import researchstack.backend.grpc.EligibilityTestResult as GrpcEligibilityTestResult
import researchstack.backend.grpc.SignedInformedConsent as GrpcSignedInformedConsentResult

fun GrpcEligibilityTestResult.toCommand(): EligibilityTestResultCommand {
    return EligibilityTestResultCommand(
        result = SurveyResult(
            questionResults = result.questionResultsList.map {
                SurveyResult.QuestionResult(
                    id = it.id,
                    result = it.result
                )
            }
        )
    )
}

fun GrpcSignedInformedConsentResult.toCommand(): SignedInformedConsentCommand {
    return SignedInformedConsentCommand(imagePath = imagePath)
}
