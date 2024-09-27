package researchstack.backend.adapter.incoming.mapper.study

import researchstack.backend.adapter.incoming.mapper.toGrpc
import researchstack.backend.application.port.incoming.study.StudyResponse
import researchstack.backend.application.port.incoming.study.StudyResponse.IrbInfoResponse
import researchstack.backend.application.port.incoming.study.StudyResponse.StudyInfoResponse
import researchstack.backend.domain.study.Study
import researchstack.backend.domain.study.Study.IrbInfo
import researchstack.backend.domain.study.Study.StudyInfo
import researchstack.backend.grpc.IrbInfo as GrpcIrbInfo
import researchstack.backend.grpc.Study as GrpcStudy
import researchstack.backend.grpc.StudyInfo as GrpcStudyInfo

fun Study.toResponse(): StudyResponse =
    StudyResponse(
        id = id,
        participationCode = participationCode,
        studyInfoResponse = studyInfo.toResponse(),
        irbInfoResponse = irbInfo.toResponse()
    )

private fun StudyInfo.toResponse(): StudyInfoResponse =
    StudyInfoResponse(
        name = name,
        description = description,
        participationApprovalType = participationApprovalType,
        scope = scope,
        stage = stage,
        logoUrl = logoUrl,
        imageUrl = imageUrl,
        organization = organization,
        duration = duration,
        period = period,
        requirements = requirements,
        targetSubject = targetSubject,
        startDate = startDate,
        endDate = endDate
    )

private fun IrbInfo.toResponse(): IrbInfoResponse =
    IrbInfoResponse(
        decisionType = irbDecisionType,
        decidedAt = decidedAt,
        expiredAt = expiredAt
    )

fun StudyResponse.toGrpc(): GrpcStudy {
    val grpcStudyBuilder = GrpcStudy.newBuilder()
    if (!participationCode.isNullOrEmpty()) {
        grpcStudyBuilder.setParticipationCode(participationCode)
    }
    return grpcStudyBuilder
        .setId(id)
        .setStudyInfo(studyInfoResponse.toGrpc())
        .setIrbInfo(irbInfoResponse.toGrpc())
        .build()
}

private fun StudyInfoResponse.toGrpc(): GrpcStudyInfo {
    return GrpcStudyInfo.newBuilder()
        .setName(name)
        .setDescription(description)
        .setParticipationApprovalType(participationApprovalType.toGrpc())
        .setScope(scope.toGrpc())
        .setStage(stage.toGrpc())
        .setLogoUrl(logoUrl)
        .setImageUrl(imageUrl)
        .setOrganization(organization)
        .setDuration(duration)
        .setPeriod(period)
        .addAllRequirements(requirements)
        .build()
}

private fun IrbInfoResponse.toGrpc(): GrpcIrbInfo {
    val builder = GrpcIrbInfo.newBuilder()
        .setDecisionType(decisionType.toGrpc())
    if (decidedAt != null) {
        builder.setDecidedAt(decidedAt.toGrpc())
    }
    if (expiredAt != null) {
        builder.setExpiredAt(expiredAt.toGrpc())
    }
    return builder.build()
}
