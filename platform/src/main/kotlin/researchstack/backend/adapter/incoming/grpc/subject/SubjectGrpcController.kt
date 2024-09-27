package researchstack.backend.adapter.incoming.grpc.subject

import com.google.protobuf.Empty
import com.google.type.Date
import com.linecorp.armeria.server.ServiceRequestContext
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.adapter.incoming.mapper.toGrpc
import researchstack.backend.application.port.incoming.subject.DeregisterSubjectUseCase
import researchstack.backend.application.port.incoming.subject.GetSubjectUseCase
import researchstack.backend.application.port.incoming.subject.RegisterSubjectCommand
import researchstack.backend.application.port.incoming.subject.RegisterSubjectUseCase
import researchstack.backend.application.port.incoming.subject.UpdateSubjectProfileCommand
import researchstack.backend.application.port.incoming.subject.UpdateSubjectProfileUseCase
import researchstack.backend.config.getUserId
import researchstack.backend.domain.subject.Subject.SubjectId
import researchstack.backend.grpc.GetSubjectProfileResponse
import researchstack.backend.grpc.GetSubjectStatusRequest
import researchstack.backend.grpc.GetSubjectStatusResponse
import researchstack.backend.grpc.RegisterSubjectRequest
import researchstack.backend.grpc.SubjectGrpcKt
import researchstack.backend.grpc.SubjectProfile
import researchstack.backend.grpc.UpdateSubjectProfileRequest
import researchstack.backend.util.validateContext

@Component
class SubjectGrpcController(
    private val deregisterSubjectUseCase: DeregisterSubjectUseCase,
    private val getSubjectUseCase: GetSubjectUseCase,
    private val registerSubjectUseCase: RegisterSubjectUseCase,
    private val updateSubjectProfileUseCase: UpdateSubjectProfileUseCase
) : SubjectGrpcKt.SubjectCoroutineImplBase() {
    private val logger = LoggerFactory.getLogger(SubjectGrpcController::class.java)
    override suspend fun registerSubject(request: RegisterSubjectRequest): Empty {
        val userId = ServiceRequestContext.current().getUserId()
        logger.info("User ID: $userId")
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        val registerSubjectCommand = RegisterSubjectCommand(
            userId,
            request.subjectProfile.firstName,
            request.subjectProfile.lastName,
            request.subjectProfile.birthday.year,
            request.subjectProfile.birthday.month,
            request.subjectProfile.birthday.day,
            request.subjectProfile.email,
            request.subjectProfile.phoneNumber,
            request.subjectProfile.address,
            request.subjectProfile.officePhoneNumber,
            request.subjectProfile.company,
            request.subjectProfile.team
        )
        registerSubjectUseCase.registerSubject(registerSubjectCommand)
        return Empty.newBuilder().build()
    }

    override suspend fun deregisterSubject(request: Empty): Empty {
        val userId = ServiceRequestContext.current().getUserId()
        logger.info("User ID: $userId")
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        deregisterSubjectUseCase.deregisterSubject(SubjectId.from(userId))
        return Empty.newBuilder().build()
    }

    override suspend fun getSubjectProfile(request: Empty): GetSubjectProfileResponse {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        val subjectProfile = getSubjectUseCase.getSubjectProfile(SubjectId.from(userId))

        val birthday = Date.newBuilder()
            .setYear(subjectProfile.birthdayYear)
            .setMonth(subjectProfile.birthdayMonth)
            .setDay(subjectProfile.birthdayDay)
            .build()

        val subjectProfileResponse = SubjectProfile.newBuilder()
            .setFirstName(subjectProfile.firstName)
            .setLastName(subjectProfile.lastName)
            .setBirthday(birthday)
            .setEmail(subjectProfile.email)
            .setPhoneNumber(subjectProfile.phoneNumber)
            .setAddress(subjectProfile.address)
            .setOfficePhoneNumber(subjectProfile.officePhoneNumber)
            .setCompany(subjectProfile.company)
            .setTeam(subjectProfile.team)
            .build()

        val response = GetSubjectProfileResponse.newBuilder()
            .setSubjectProfile(subjectProfileResponse)
            .build()
        return response
    }

    override suspend fun getSubjectStatus(request: GetSubjectStatusRequest): GetSubjectStatusResponse {
        validateContext(request.studyId, ExceptionMessage.EMPTY_STUDY_ID)
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)

        val subjectStatus = getSubjectUseCase.getSubjectStatus(SubjectId.from(userId), request.studyId)

        return GetSubjectStatusResponse.newBuilder()
            .setSubjectStatus(subjectStatus.status.toGrpc())
            .build()
    }

    override suspend fun updateSubjectProfile(request: UpdateSubjectProfileRequest): Empty {
        val userId = ServiceRequestContext.current().getUserId()
        validateContext(userId, ExceptionMessage.EMPTY_USER_ID)
        val updateSubjectProfileCommand = UpdateSubjectProfileCommand(
            request.subjectProfile.firstName,
            request.subjectProfile.lastName,
            request.subjectProfile.birthday.year,
            request.subjectProfile.birthday.month,
            request.subjectProfile.birthday.day,
            request.subjectProfile.email,
            request.subjectProfile.phoneNumber,
            request.subjectProfile.address,
            request.subjectProfile.officePhoneNumber,
            request.subjectProfile.company,
            request.subjectProfile.team
        )
        updateSubjectProfileUseCase.updateSubjectProfile(SubjectId.from(userId), updateSubjectProfileCommand)
        return Empty.newBuilder().build()
    }
}
