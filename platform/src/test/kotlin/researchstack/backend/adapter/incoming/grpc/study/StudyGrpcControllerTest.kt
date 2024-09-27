package researchstack.backend.adapter.incoming.grpc.study

import com.google.protobuf.Empty
import com.linecorp.armeria.server.ServiceRequestContext
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.incoming.mapper.study.toCommand
import researchstack.backend.adapter.incoming.mapper.toGrpc
import researchstack.backend.application.port.incoming.study.GetParticipationRequirementUseCase
import researchstack.backend.application.port.incoming.study.GetStudyUseCase
import researchstack.backend.application.port.incoming.study.ParticipateInStudyCommand
import researchstack.backend.application.port.incoming.study.ParticipateInStudyUseCase
import researchstack.backend.application.port.incoming.study.ParticipationRequirementResponse
import researchstack.backend.application.port.incoming.study.StudyResponse
import researchstack.backend.application.port.incoming.study.WithdrawFromStudyUseCase
import researchstack.backend.application.port.incoming.task.SurveyTaskResponse
import researchstack.backend.config.getUserId
import researchstack.backend.enums.HealthDataType
import researchstack.backend.enums.IrbDecisionType
import researchstack.backend.enums.QuestionTag
import researchstack.backend.enums.StudyScope
import researchstack.backend.grpc.EligibilityTestResult
import researchstack.backend.grpc.GetEligibilityTestRequest
import researchstack.backend.grpc.GetEligibilityTestResultRequest
import researchstack.backend.grpc.GetInformedConsentRequest
import researchstack.backend.grpc.GetParticipationRequirementListRequest
import researchstack.backend.grpc.GetRequiredHealthDataTypeListRequest
import researchstack.backend.grpc.GetSignedInformedConsentRequest
import researchstack.backend.grpc.GetStudyByParticipationCodeRequest
import researchstack.backend.grpc.GetStudyRequest
import researchstack.backend.grpc.ParticipateInStudyRequest
import researchstack.backend.grpc.SignedInformedConsent
import researchstack.backend.grpc.TaskResult.QuestionResult
import researchstack.backend.grpc.TaskResult.SurveyResult
import researchstack.backend.grpc.WithdrawFromStudyRequest
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
internal class StudyGrpcControllerTest {
    private val getStudyUseCase = mockk<GetStudyUseCase>()
    private val getParticipationRequirementUseCase = mockk<GetParticipationRequirementUseCase>()
    private val participateInStudyUseCase = mockk<ParticipateInStudyUseCase>(relaxed = true)
    private val withdrawFromStudyUseCase = mockk<WithdrawFromStudyUseCase>()
    private val serviceRequestContext = mockk<ServiceRequestContext>()
    private val studyGrpcController = StudyGrpcController(
        getStudyUseCase,
        getParticipationRequirementUseCase,
        participateInStudyUseCase,
        withdrawFromStudyUseCase
    )

    private val studyName = "menatalcare study"
    private val studyDescription = "Study for depression diagnosis algorithm"
    private val participationApprovalType = researchstack.backend.enums.StudyParticipationApprovalType.AUTO
    private val studyScope = StudyScope.PRIVATE
    private val studyStage = researchstack.backend.enums.StudyStage.STARTED_OPEN
    private val logoUrl = "http://example.com/logo"
    private val imageUrl = "http://example.com/image"
    private val organization = "OO Medical Center"
    private val duration = "15m/week"
    private val period = "1 month"
    private val requirements = listOf(
        "Do daily survey",
        "Wear galaxy watch except sleep"
    )
    private val studyInfoResponse = StudyResponse.StudyInfoResponse(
        studyName,
        studyDescription,
        participationApprovalType,
        studyScope,
        studyStage,
        logoUrl,
        imageUrl,
        organization,
        duration,
        period,
        requirements
    )
    private val now = LocalDateTime.now()
    private val decisionType = IrbDecisionType.APPROVED
    private val decidedAt = now
    private val expiredAt = now.plusMonths(6)
    private val irbInfoResponse = StudyResponse.IrbInfoResponse(
        decisionType,
        decidedAt,
        expiredAt
    )
    private val studyId = "mentalCareStudy"
    private val participationCode = "secret"
    private val studyResponse = StudyResponse(
        studyId,
        participationCode,
        studyInfoResponse,
        irbInfoResponse
    )

    private val userId = "7e3ba225-0e3c-4c87-9d02-b1fbe0802239"

    private val surveyTaskResponse = SurveyTaskResponse(
        listOf(
            researchstack.backend.application.port.incoming.task.Section(
                listOf(
                    researchstack.backend.application.port.incoming.task.Question(
                        id = "1",
                        title = "How old are you?",
                        explanation = "aa",
                        tag = QuestionTag.SLIDER,
                        itemProperties = researchstack.backend.application.port.incoming.task.Question.ScaleProperties(
                            0,
                            1100,
                            "ll",
                            "hl"
                        ),
                        required = true
                    )
                )
            )
        )
    )

    private val answer = ParticipationRequirementResponse.EligibilityTestResponse.Answer(
        "1",
        ParticipationRequirementResponse.EligibilityTestResponse.ScaleAnswer(
            0,
            100
        )
    )

    private val eligibilityTestResponse = ParticipationRequirementResponse.EligibilityTestResponse(
        surveyTaskResponse,
        listOf(answer)
    )

    private val imagePath = "http://example.com/image"
    private val informedConsentResponse = ParticipationRequirementResponse.InformedConsentResponse(
        imagePath
    )

    private val healthDataTypeList = listOf(
        HealthDataType.BLOOD_PRESSURE,
        HealthDataType.HEART_RATE,
        HealthDataType.SLEEP_SESSION,
        HealthDataType.SLEEP_STAGE,
        HealthDataType.STEPS,
        HealthDataType.WEIGHT,
        HealthDataType.OXYGEN_SATURATION,
        HealthDataType.HEIGHT,
        HealthDataType.RESPIRATORY_RATE,
        HealthDataType.TOTAL_CALORIES_BURNED,
        HealthDataType.BLOOD_GLUCOSE,
        HealthDataType.LIGHT,
        HealthDataType.ACCELEROMETER,
        HealthDataType.EXERCISE
    )

    private val dataSpec = ParticipationRequirementResponse.DataSpecResponse(
        dataName = "WatchIHRNRaw",
        dataDescription = "WatchIHRNRaw",
        dataId = "WatchIHRNRaw",
        collectionMethod = "Spot Check",
        targetTrialNumber = 10,
        durationThreshold = 30
    )
    private val taskInfo = listOf(
        dataSpec
    )

    private val participationRequirementResponse = ParticipationRequirementResponse(
        eligibilityTestResponse,
        informedConsentResponse,
        healthDataTypeList,
        taskInfo
    )

    private val questionResult = QuestionResult.newBuilder()
        .setId("1")
        .setResult("50")
        .build()

    private val eligibilityTestResult = EligibilityTestResult.newBuilder()
        .setResult(
            SurveyResult.newBuilder()
                .addAllQuestionResults(
                    listOf(questionResult)
                )
                .build()
        )
        .build()

    private val signedInformedConsent = SignedInformedConsent.newBuilder()
        .setImagePath(imagePath)
        .build()

    private val subjectNumber = "12345"

    @Test
    @Tag(POSITIVE_TEST)
    fun `getStudy should work properly`() = runTest {
        val getStudyRequest = GetStudyRequest.newBuilder()
            .setStudyId(studyId)
            .build()
        coEvery {
            getStudyUseCase.getStudy(studyId)
        } returns studyResponse

        val response = studyGrpcController.getStudy(getStudyRequest)
        Assertions.assertEquals(studyId, response.study.id)
        Assertions.assertEquals(studyName, response.study.studyInfo.name)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getStudy should throw IllegalArgumentException if studyId is empty`(studyId: String) = runTest {
        val getStudyRequest = GetStudyRequest.newBuilder()
            .setStudyId(studyId)
            .build()
        assertThrows<IllegalArgumentException> {
            studyGrpcController.getStudy(getStudyRequest)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getStudyByParticipationCode should work properly`() = runTest {
        val getStudyByParticipationCodeRequest = GetStudyByParticipationCodeRequest.newBuilder()
            .setParticipationCode(participationCode)
            .build()
        coEvery {
            getStudyUseCase.getStudyByParticipationCode(participationCode)
        } returns studyResponse

        val response = studyGrpcController.getStudyByParticipationCode(getStudyByParticipationCodeRequest)
        Assertions.assertEquals(studyId, response.study.id)
        Assertions.assertEquals(studyName, response.study.studyInfo.name)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getStudyByParticipationCode should throw IllegalArgumentException if participationCode is empty`(
        participationCode: String
    ) = runTest {
        val getStudyByParticipationCodeRequest = GetStudyByParticipationCodeRequest.newBuilder()
            .setParticipationCode(participationCode)
            .build()
        assertThrows<IllegalArgumentException> {
            studyGrpcController.getStudyByParticipationCode(getStudyByParticipationCodeRequest)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getStudyList should work properly`() = runTest {
        coEvery {
            getStudyUseCase.getStudyList()
        } returns listOf(studyResponse)

        val response = studyGrpcController.getStudyList(Empty.newBuilder().build())
        Assertions.assertEquals(studyId, response.studiesList[0].id)
        Assertions.assertEquals(studyName, response.studiesList[0].studyInfo.name)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getPublicStudyList should work properly`() = runTest {
        coEvery {
            getStudyUseCase.getPublicStudyList()
        } returns listOf(studyResponse)

        val response = studyGrpcController.getPublicStudyList(Empty.newBuilder().build())
        Assertions.assertEquals(studyId, response.studiesList[0].id)
        Assertions.assertEquals(studyName, response.studiesList[0].studyInfo.name)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getParticipatedStudyList should work properly`() = runTest {
        mockkStatic(ServiceRequestContext::class)
        coEvery {
            ServiceRequestContext.current()
        } returns serviceRequestContext
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        coEvery {
            getStudyUseCase.getParticipatedStudyList(userId)
        } returns listOf(studyResponse)

        val response = studyGrpcController.getParticipatedStudyList(Empty.newBuilder().build())
        Assertions.assertEquals(studyId, response.studiesList[0].id)
        Assertions.assertEquals(studyName, response.studiesList[0].studyInfo.name)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getParticipatedStudyList should throw IllegalArgumentException if userId is empty`(userId: String) = runTest {
        mockkStatic(ServiceRequestContext::class)
        coEvery {
            ServiceRequestContext.current()
        } returns serviceRequestContext
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        assertThrows<IllegalArgumentException> {
            studyGrpcController.getParticipatedStudyList(Empty.newBuilder().build())
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getParticipationRequirementList should work properly`() = runTest {
        val getParticipationRequirementListRequest = GetParticipationRequirementListRequest.newBuilder()
            .setStudyId(studyId)
            .build()

        coEvery {
            getParticipationRequirementUseCase.getParticipationRequirement(studyId)
        } returns participationRequirementResponse

        val response = studyGrpcController.getParticipationRequirementList(getParticipationRequirementListRequest)
        Assertions.assertEquals(
            eligibilityTestResponse.answers[0].questionId,
            response.eligibilityTest.answersList[0].questionId
        )
        Assertions.assertEquals(informedConsentResponse.imagePath, response.informedConsent.imagePath)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getParticipationRequirementList should throw IllegalArgumentException if studyId is empty`(studyId: String) =
        runTest {
            val getParticipationRequirementListRequest = GetParticipationRequirementListRequest.newBuilder()
                .setStudyId(studyId)
                .build()

            assertThrows<IllegalArgumentException> {
                studyGrpcController.getParticipationRequirementList(getParticipationRequirementListRequest)
            }
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getEligibilityTest should work properly`() = runTest {
        val getEligibilityTestRequest = GetEligibilityTestRequest.newBuilder()
            .setStudyId(studyId)
            .build()

        coEvery {
            getParticipationRequirementUseCase.getEligibilityTest(studyId)
        } returns eligibilityTestResponse

        val response = studyGrpcController.getEligibilityTest(getEligibilityTestRequest)
        Assertions.assertEquals(
            eligibilityTestResponse.answers[0].questionId,
            response.eligibilityTest.answersList[0].questionId
        )
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getEligibilityTest should throw IllegalArgumentException if studyId is empty`(studyId: String) = runTest {
        val getEligibilityTestRequest = GetEligibilityTestRequest.newBuilder()
            .setStudyId(studyId)
            .build()

        assertThrows<IllegalArgumentException> {
            studyGrpcController.getEligibilityTest(getEligibilityTestRequest)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getInformedConsent should work properly`() = runTest {
        val getInformedConsentRequest = GetInformedConsentRequest.newBuilder()
            .setStudyId(studyId)
            .build()

        coEvery {
            getParticipationRequirementUseCase.getInformedConsent(studyId)
        } returns informedConsentResponse

        val response = studyGrpcController.getInformedConsent(getInformedConsentRequest)
        Assertions.assertEquals(informedConsentResponse.imagePath, response.informedConsent.imagePath)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getInformedConsent should throw IllegalArgumentException if studyId is empty`(studyId: String) = runTest {
        val getInformedConsentRequest = GetInformedConsentRequest.newBuilder()
            .setStudyId(studyId)
            .build()

        assertThrows<IllegalArgumentException> {
            studyGrpcController.getInformedConsent(getInformedConsentRequest)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getRequiredHealthDataTypeList should work properly`() = runTest {
        val getRequiredHealthDataTypeListRequest = GetRequiredHealthDataTypeListRequest.newBuilder()
            .setStudyId(studyId)
            .build()

        coEvery {
            getParticipationRequirementUseCase.getRequiredHealthDataTypeList(studyId)
        } returns healthDataTypeList

        val response = studyGrpcController.getRequiredHealthDataTypeList(getRequiredHealthDataTypeListRequest)
        Assertions.assertEquals(healthDataTypeList[0].toGrpc(), response.getDataTypes(0))
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getRequiredHealthDataTypeList should throw IllegalArgumentException if studyId is empty`(studyId: String) =
        runTest {
            val getRequiredHealthDataTypeRequest = GetRequiredHealthDataTypeListRequest.newBuilder()
                .setStudyId(studyId)
                .build()

            assertThrows<IllegalArgumentException> {
                studyGrpcController.getRequiredHealthDataTypeList(getRequiredHealthDataTypeRequest)
            }
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getEligibilityTestResult should work properly`() = runTest {
        val getEligibilityTestResultRequest = GetEligibilityTestResultRequest.newBuilder()
            .setStudyId(studyId)
            .build()

        assertDoesNotThrow {
            studyGrpcController.getEligibilityTestResult(getEligibilityTestResultRequest)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getSignedInformedConsent should work properly`() = runTest {
        val getSignedInformedConsentRequest = GetSignedInformedConsentRequest.newBuilder()
            .setStudyId(studyId)
            .build()

        assertDoesNotThrow {
            studyGrpcController.getSignedInformedConsent(getSignedInformedConsentRequest)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `participateInStudy should work properly`() = runTest {
        mockkStatic(ServiceRequestContext::class)
        coEvery {
            ServiceRequestContext.current()
        } returns serviceRequestContext
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val participateInStudyRequest = ParticipateInStudyRequest.newBuilder()
            .setEligibilityTestResult(eligibilityTestResult)
            .setSignedInformedConsent(signedInformedConsent)
            .setStudyId(studyId)
            .build()

        coEvery {
            participateInStudyUseCase.participateInStudy(
                userId,
                participateInStudyRequest.studyId,
                ParticipateInStudyCommand(
                    if (participateInStudyRequest.hasEligibilityTestResult()) participateInStudyRequest.eligibilityTestResult.toCommand() else null,
                    participateInStudyRequest.signedInformedConsent.toCommand()
                )
            )
        } returns subjectNumber

        assertDoesNotThrow {
            studyGrpcController.participateInStudy(participateInStudyRequest)
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `participateInStudy should throw IllegalArgumentException if studyId is empty`(studyId: String) = runTest {
        mockkStatic(ServiceRequestContext::class)
        coEvery {
            ServiceRequestContext.current()
        } returns serviceRequestContext
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val participateInStudyRequest = ParticipateInStudyRequest.newBuilder()
            .setEligibilityTestResult(eligibilityTestResult)
            .setSignedInformedConsent(signedInformedConsent)
            .setStudyId(studyId)
            .build()

        assertThrows<IllegalArgumentException> {
            studyGrpcController.participateInStudy(participateInStudyRequest)
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `participateInStudy should throw IllegalArgumentException if userId is empty`(userId: String) = runTest {
        mockkStatic(ServiceRequestContext::class)
        coEvery {
            ServiceRequestContext.current()
        } returns serviceRequestContext
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val participateInStudyRequest = ParticipateInStudyRequest.newBuilder()
            .setEligibilityTestResult(eligibilityTestResult)
            .setSignedInformedConsent(signedInformedConsent)
            .setStudyId(studyId)
            .build()

        assertThrows<IllegalArgumentException> {
            studyGrpcController.participateInStudy(participateInStudyRequest)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `withdrawFromStudy should work properly`() = runTest {
        mockkStatic(ServiceRequestContext::class)
        coEvery {
            ServiceRequestContext.current()
        } returns serviceRequestContext
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val withdrawFromStudyRequest = WithdrawFromStudyRequest.newBuilder()
            .setStudyId(studyId)
            .build()

        coEvery {
            withdrawFromStudyUseCase.withdrawFromStudy(userId, studyId)
        } returns Unit

        assertDoesNotThrow {
            studyGrpcController.withdrawFromStudy(withdrawFromStudyRequest)
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `withdrawFromStudy should throw IllegalArgumentException if studyId is empty`(studyId: String) = runTest {
        mockkStatic(ServiceRequestContext::class)
        coEvery {
            ServiceRequestContext.current()
        } returns serviceRequestContext
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val withdrawFromStudyRequest = WithdrawFromStudyRequest.newBuilder()
            .setStudyId(studyId)
            .build()

        assertThrows<IllegalArgumentException> {
            studyGrpcController.withdrawFromStudy(withdrawFromStudyRequest)
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `withdrawFromStudy should throw IllegalArgumentException if userId is empty`(userId: String) = runTest {
        mockkStatic(ServiceRequestContext::class)
        coEvery {
            ServiceRequestContext.current()
        } returns serviceRequestContext
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        val withdrawFromStudyRequest = WithdrawFromStudyRequest.newBuilder()
            .setStudyId(studyId)
            .build()

        assertThrows<IllegalArgumentException> {
            studyGrpcController.withdrawFromStudy(withdrawFromStudyRequest)
        }
    }
}
