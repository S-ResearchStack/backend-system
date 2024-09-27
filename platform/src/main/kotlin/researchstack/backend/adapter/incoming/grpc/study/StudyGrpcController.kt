package researchstack.backend.adapter.incoming.grpc.study

import com.google.protobuf.Empty
import com.linecorp.armeria.server.ServiceRequestContext
import org.springframework.stereotype.Component
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.adapter.incoming.mapper.study.toCommand
import researchstack.backend.adapter.incoming.mapper.study.toGrpc
import researchstack.backend.adapter.incoming.mapper.toGrpc
import researchstack.backend.application.port.incoming.study.GetParticipationRequirementUseCase
import researchstack.backend.application.port.incoming.study.GetStudyUseCase
import researchstack.backend.application.port.incoming.study.ParticipateInStudyCommand
import researchstack.backend.application.port.incoming.study.ParticipateInStudyUseCase
import researchstack.backend.application.port.incoming.study.WithdrawFromStudyUseCase
import researchstack.backend.config.getUserId
import researchstack.backend.grpc.GetEligibilityTestRequest
import researchstack.backend.grpc.GetEligibilityTestResponse
import researchstack.backend.grpc.GetEligibilityTestResultRequest
import researchstack.backend.grpc.GetEligibilityTestResultResponse
import researchstack.backend.grpc.GetInformedConsentRequest
import researchstack.backend.grpc.GetInformedConsentResponse
import researchstack.backend.grpc.GetParticipatedStudyListResponse
import researchstack.backend.grpc.GetParticipationRequirementListRequest
import researchstack.backend.grpc.GetParticipationRequirementListResponse
import researchstack.backend.grpc.GetPublicStudyListResponse
import researchstack.backend.grpc.GetRequiredHealthDataTypeListRequest
import researchstack.backend.grpc.GetRequiredHealthDataTypeListResponse
import researchstack.backend.grpc.GetSignedInformedConsentRequest
import researchstack.backend.grpc.GetSignedInformedConsentResponse
import researchstack.backend.grpc.GetStudyByParticipationCodeRequest
import researchstack.backend.grpc.GetStudyByParticipationCodeResponse
import researchstack.backend.grpc.GetStudyListResponse
import researchstack.backend.grpc.GetStudyRequest
import researchstack.backend.grpc.GetStudyResponse
import researchstack.backend.grpc.ParticipateInStudyRequest
import researchstack.backend.grpc.ParticipateInStudyResponse
import researchstack.backend.grpc.StudyServiceGrpcKt
import researchstack.backend.grpc.WithdrawFromStudyRequest
import researchstack.backend.util.validateContext

@Component
class StudyGrpcController(
    private val getStudyUseCase: GetStudyUseCase,
    private val getParticipationRequirementUseCase: GetParticipationRequirementUseCase,
    private val participateInStudyUseCase: ParticipateInStudyUseCase,
    private val withdrawFromStudyUseCase: WithdrawFromStudyUseCase
) : StudyServiceGrpcKt.StudyServiceCoroutineImplBase() {

    override suspend fun getStudy(request: GetStudyRequest): GetStudyResponse {
        validateContext(request.studyId, ExceptionMessage.EMPTY_STUDY_ID)
        val response = getStudyUseCase.getStudy(request.studyId)
        return GetStudyResponse.newBuilder().setStudy(response.toGrpc()).build()
    }

    override suspend fun getStudyByParticipationCode(request: GetStudyByParticipationCodeRequest): GetStudyByParticipationCodeResponse {
        validateContext(request.participationCode, ExceptionMessage.EMPTY_PARTICIPATION_CODE)
        val response = getStudyUseCase.getStudyByParticipationCode(request.participationCode)
        return GetStudyByParticipationCodeResponse.newBuilder().setStudy(response.toGrpc()).build()
    }

    override suspend fun getStudyList(request: Empty): GetStudyListResponse {
        val response = getStudyUseCase.getStudyList()
        val studyList = response.map { it.toGrpc() }
        return GetStudyListResponse.newBuilder().addAllStudies(studyList).build()
    }

    override suspend fun getPublicStudyList(request: Empty): GetPublicStudyListResponse {
        val response = getStudyUseCase.getPublicStudyList()
        val studyList = response.map { it.toGrpc() }
        return GetPublicStudyListResponse.newBuilder().addAllStudies(studyList).build()
    }

    override suspend fun getParticipatedStudyList(request: Empty): GetParticipatedStudyListResponse {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        val response = getStudyUseCase.getParticipatedStudyList(userId)
        val studyList = response.map { it.toGrpc() }
        return GetParticipatedStudyListResponse.newBuilder().addAllStudies(studyList).build()
    }

    override suspend fun getParticipationRequirementList(request: GetParticipationRequirementListRequest): GetParticipationRequirementListResponse {
        validateContext(request.studyId, ExceptionMessage.EMPTY_STUDY_ID)
        val response = getParticipationRequirementUseCase.getParticipationRequirement(request.studyId)
        val participationRequirementListResponseBuilder = GetParticipationRequirementListResponse.newBuilder()
        if (response.eligibilityTest != null) {
            participationRequirementListResponseBuilder.setEligibilityTest(response.eligibilityTest.toGrpc())
        }
        return participationRequirementListResponseBuilder
            .setInformedConsent(response.informedConsent.toGrpc())
            .addAllDataTypes(response.healthDataTypeList.map { it.toGrpc() })
            .build()
    }

    override suspend fun getEligibilityTest(request: GetEligibilityTestRequest): GetEligibilityTestResponse {
        validateContext(request.studyId, ExceptionMessage.EMPTY_STUDY_ID)
        val response = getParticipationRequirementUseCase.getEligibilityTest(request.studyId)
        val eligibilityTestResponseBuilder = GetEligibilityTestResponse.newBuilder()
        if (response != null) {
            eligibilityTestResponseBuilder.setEligibilityTest(response.toGrpc())
        }
        return eligibilityTestResponseBuilder.build()
    }

    override suspend fun getInformedConsent(request: GetInformedConsentRequest): GetInformedConsentResponse {
        validateContext(request.studyId, ExceptionMessage.EMPTY_STUDY_ID)
        val response = getParticipationRequirementUseCase.getInformedConsent(request.studyId)
        return GetInformedConsentResponse
            .newBuilder()
            .setInformedConsent(response.toGrpc())
            .build()
    }

    override suspend fun getRequiredHealthDataTypeList(request: GetRequiredHealthDataTypeListRequest): GetRequiredHealthDataTypeListResponse {
        validateContext(request.studyId, ExceptionMessage.EMPTY_STUDY_ID)
        val response = getParticipationRequirementUseCase.getRequiredHealthDataTypeList(request.studyId)
        return GetRequiredHealthDataTypeListResponse
            .newBuilder()
            .addAllDataTypes(response.map { it.toGrpc() })
            .build()
    }

    override suspend fun getEligibilityTestResult(request: GetEligibilityTestResultRequest): GetEligibilityTestResultResponse {
        // TODO: not required yet
        return GetEligibilityTestResultResponse.newBuilder().build()
    }

    override suspend fun getSignedInformedConsent(request: GetSignedInformedConsentRequest): GetSignedInformedConsentResponse {
        // TODO: not required yet
        return GetSignedInformedConsentResponse.newBuilder().build()
    }

    override suspend fun participateInStudy(request: ParticipateInStudyRequest): ParticipateInStudyResponse {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        validateContext(request.studyId, ExceptionMessage.EMPTY_STUDY_ID)
        val eligibilityTestResultCommand =
            if (request.hasEligibilityTestResult()) request.eligibilityTestResult.toCommand() else null
        val subjectNumber = participateInStudyUseCase.participateInStudy(
            userId,
            request.studyId,
            ParticipateInStudyCommand(
                eligibilityTestResultCommand = eligibilityTestResultCommand,
                signedInformedConsentCommand = request.signedInformedConsent.toCommand()
            )
        )
        return ParticipateInStudyResponse.newBuilder()
            .setSubjectNumber(subjectNumber)
            .build()
    }

    override suspend fun withdrawFromStudy(request: WithdrawFromStudyRequest): Empty {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        validateContext(request.studyId, ExceptionMessage.EMPTY_STUDY_ID)
        withdrawFromStudyUseCase.withdrawFromStudy(userId, request.studyId)
        return Empty.newBuilder().build()
    }
}
