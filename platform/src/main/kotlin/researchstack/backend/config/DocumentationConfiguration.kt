package researchstack.backend.config

import com.google.type.Date
import com.linecorp.armeria.common.HttpHeaderNames
import com.linecorp.armeria.common.HttpHeaders
import com.linecorp.armeria.server.docs.DocService
import com.linecorp.armeria.server.docs.DocServiceFilter
import io.grpc.reflection.v1alpha.ServerReflectionGrpc
import researchstack.backend.grpc.EligibilityTestResult
import researchstack.backend.grpc.GetStudyByParticipationCodeRequest
import researchstack.backend.grpc.GetStudyRequest
import researchstack.backend.grpc.ParticipateInStudyRequest
import researchstack.backend.grpc.RegisterSubjectRequest
import researchstack.backend.grpc.SignedInformedConsent
import researchstack.backend.grpc.StudyServiceGrpcKt
import researchstack.backend.grpc.SubjectGrpcKt
import researchstack.backend.grpc.SubjectProfile
import researchstack.backend.grpc.TaskResult
import researchstack.backend.grpc.TaskServiceGrpcKt
import researchstack.backend.grpc.TaskSpecRequest

private val signedInformedConsent = SignedInformedConsent.newBuilder()
    .setImagePath("http://example.com/ic.png")
    .build()

private val firstQuestionResult = TaskResult.QuestionResult.newBuilder()
    .setId("1")
    .setResult("12345")
    .build()

private val secondQuestionResult = TaskResult.QuestionResult.newBuilder()
    .setId("2")
    .setResult("pass")
    .build()

private val eligibilityTestResult = EligibilityTestResult.newBuilder()
    .setResult(
        TaskResult.SurveyResult.newBuilder()
            .addAllQuestionResults(
                listOf(firstQuestionResult, secondQuestionResult)
            )
            .build()
    )
    .build()

object RequestExample {
    val registerUser = RegisterSubjectRequest.newBuilder()
        .setSubjectProfile(
            SubjectProfile.newBuilder()
                .setAddress("testAddress")
                .setBirthday(
                    Date.newBuilder()
                        .setDay(1)
                        .setMonth(1)
                        .setYear(2000)
                )
                .setEmail("test@test.com")
                .setFirstName("testFirst")
                .setLastName("testLast")
                .setPhoneNumber("01000000000")
                .build()
        )
        .build()

    val getStudy = GetStudyRequest.newBuilder()
        .setStudyId("heartStudy")
        .build()

    val getStudyByParticipationCode = GetStudyByParticipationCodeRequest.newBuilder()
        .setParticipationCode("secret")
        .build()

    val participateInStudy = ParticipateInStudyRequest.newBuilder()
        .setEligibilityTestResult(eligibilityTestResult)
        .setSignedInformedConsent(signedInformedConsent)
        .setStudyId("mentalCareStudy")
        .build()

    val getTaskSpecs = TaskSpecRequest.newBuilder()
        .setStudyId("heartStudy")
        .build()
}

val httpHeaders = HttpHeaders.builder()
    .add(HttpHeaderNames.AUTHORIZATION, "Bearer PUT_YOUR_TOKEN")
    .add("jwt-issuer", "samsung-account")
    .build()

val docService = DocService.builder()
    .exampleHeaders(httpHeaders)
    .exampleRequests(SubjectGrpcKt.SERVICE_NAME, "RegisterUser", RequestExample.registerUser)
    .exampleRequests(StudyServiceGrpcKt.SERVICE_NAME, "GetStudy", RequestExample.getStudy)
    .exampleRequests(
        StudyServiceGrpcKt.SERVICE_NAME,
        "GetStudyByParticipationCode",
        RequestExample.getStudyByParticipationCode
    )
    .exampleRequests(StudyServiceGrpcKt.SERVICE_NAME, "ParticipateInStudy", RequestExample.participateInStudy)
    .exampleRequests(TaskServiceGrpcKt.SERVICE_NAME, "GetTaskSpecs", RequestExample.getTaskSpecs)
    .exclude(DocServiceFilter.ofServiceName(ServerReflectionGrpc.SERVICE_NAME))
    .build()
