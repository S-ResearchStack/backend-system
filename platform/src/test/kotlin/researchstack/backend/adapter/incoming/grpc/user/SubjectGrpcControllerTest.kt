package researchstack.backend.adapter.incoming.grpc.user

import com.google.protobuf.Empty
import com.google.type.Date
import com.linecorp.armeria.server.ServiceRequestContext
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.incoming.grpc.subject.SubjectGrpcController
import researchstack.backend.application.port.incoming.subject.DeregisterSubjectUseCase
import researchstack.backend.application.port.incoming.subject.GetSubjectUseCase
import researchstack.backend.application.port.incoming.subject.RegisterSubjectCommand
import researchstack.backend.application.port.incoming.subject.RegisterSubjectUseCase
import researchstack.backend.application.port.incoming.subject.UpdateSubjectProfileCommand
import researchstack.backend.application.port.incoming.subject.UpdateSubjectProfileUseCase
import researchstack.backend.config.getUserId
import researchstack.backend.enums.SubjectStatus
import researchstack.backend.grpc.GetSubjectStatusRequest
import researchstack.backend.grpc.RegisterSubjectRequest
import researchstack.backend.grpc.SubjectProfile
import researchstack.backend.grpc.UpdateSubjectProfileRequest

@ExperimentalCoroutinesApi
internal class SubjectGrpcControllerTest {
    private val registerSubjectUseCase = mockk<RegisterSubjectUseCase>()
    private val getSubjectProfileUseCase = mockk<GetSubjectUseCase>()
    private val updateSubjectProfileUseCase = mockk<UpdateSubjectProfileUseCase>()
    private val deregisterSubjectUseCase = mockk<DeregisterSubjectUseCase>()
    private val serviceRequestContext = mockk<ServiceRequestContext>()
    private val subjectGrpcController = SubjectGrpcController(
        deregisterSubjectUseCase,
        getSubjectProfileUseCase,
        registerSubjectUseCase,
        updateSubjectProfileUseCase
    )

    private val userId = "7e3ba225-0e3c-4c87-9d02-b1fbe0802239"
    private val birthdayYear = 2000
    private val birthdayMonth = 11
    private val birthdayDay = 1
    private val date: Date = Date.newBuilder()
        .setYear(birthdayYear)
        .setMonth(birthdayMonth)
        .setDay(birthdayDay)
        .build()
    private val firstName = "Kevin"
    private val lastName = "Kim"
    private val email = "kevin.kim@example.com"
    private val phoneNumber = "010-1234-0000"
    private val address = "Seoul"
    private val officePhoneNumber = "02-0000-0000"
    private val company = "Samsung"
    private val team = "Data Intelligence"
    private val subjectProfile: SubjectProfile = SubjectProfile.newBuilder()
        .setFirstName(firstName)
        .setLastName(lastName)
        .setBirthday(date)
        .setEmail(email)
        .setPhoneNumber(phoneNumber)
        .setAddress(address)
        .setOfficePhoneNumber(officePhoneNumber)
        .setCompany(company)
        .setTeam(team)
        .build()
    private val registerUserRequest: RegisterSubjectRequest = RegisterSubjectRequest.newBuilder()
        .setSubjectProfile(subjectProfile)
        .build()
    private val updateUserProfileRequest: UpdateSubjectProfileRequest = UpdateSubjectProfileRequest.newBuilder()
        .setSubjectProfile(subjectProfile)
        .build()
    private val registerSubjectCommand = RegisterSubjectCommand(
        userId,
        firstName,
        lastName,
        birthdayYear,
        birthdayMonth,
        birthdayDay,
        email,
        phoneNumber,
        address,
        officePhoneNumber,
        company,
        team
    )
    private val updateSubjectProfileCommand = UpdateSubjectProfileCommand(
        firstName,
        lastName,
        birthdayYear,
        birthdayMonth,
        birthdayDay,
        email,
        phoneNumber,
        address,
        officePhoneNumber,
        company,
        team
    )
    private val subjectDomain = researchstack.backend.domain.subject.Subject(
        researchstack.backend.domain.subject.Subject.SubjectId.from(userId),
        firstName,
        lastName,
        birthdayYear,
        birthdayMonth,
        birthdayDay,
        email,
        phoneNumber,
        address,
        officePhoneNumber,
        company,
        team
    )
    private val studyId = "test-study-id"

    @BeforeEach
    fun setup() {
        mockkStatic(ServiceRequestContext::class)
        coEvery {
            ServiceRequestContext.current()
        } returns serviceRequestContext
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `registerSubject should work properly`() = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        coEvery {
            registerSubjectUseCase.registerSubject(registerSubjectCommand)
        } returns Unit

        assertDoesNotThrow {
            subjectGrpcController.registerSubject(registerUserRequest)
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `registerSubject should throw IllegalArgumentException if userId is empty`(userId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        assertThrows<IllegalArgumentException> {
            subjectGrpcController.registerSubject(registerUserRequest)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `deregisterSubject should work properly`() = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        coEvery {
            deregisterSubjectUseCase.deregisterSubject(
                researchstack.backend.domain.subject.Subject.SubjectId.from(
                    userId
                )
            )
        } returns Unit

        assertDoesNotThrow {
            subjectGrpcController.deregisterSubject(Empty.newBuilder().build())
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `deregisterSubject should throw IllegalArgumentException if userId is empty`(userId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        assertThrows<IllegalArgumentException> {
            subjectGrpcController.deregisterSubject(Empty.newBuilder().build())
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `updateSubjectProfile should work properly`() = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        coEvery {
            updateSubjectProfileUseCase.updateSubjectProfile(
                researchstack.backend.domain.subject.Subject.SubjectId.from(userId),
                updateSubjectProfileCommand
            )
        } returns Unit

        assertDoesNotThrow {
            subjectGrpcController.updateSubjectProfile(updateUserProfileRequest)
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `updateSubjectProfile should throw IllegalArgumentException if userId is empty`(userId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        assertThrows<IllegalArgumentException> {
            subjectGrpcController.updateSubjectProfile(updateUserProfileRequest)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getSubjectProfile should work properly`() = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        coEvery {
            getSubjectProfileUseCase.getSubjectProfile(
                researchstack.backend.domain.subject.Subject.SubjectId.from(userId)
            )
        } returns subjectDomain

        val response = subjectGrpcController.getSubjectProfile(Empty.newBuilder().build())
        Assertions.assertEquals(firstName, response.subjectProfile.firstName)
        Assertions.assertEquals(lastName, response.subjectProfile.lastName)
        Assertions.assertEquals(birthdayYear, response.subjectProfile.birthday.year)
        Assertions.assertEquals(birthdayMonth, response.subjectProfile.birthday.month)
        Assertions.assertEquals(birthdayDay, response.subjectProfile.birthday.day)
        Assertions.assertEquals(email, response.subjectProfile.email)
        Assertions.assertEquals(phoneNumber, response.subjectProfile.phoneNumber)
        Assertions.assertEquals(address, response.subjectProfile.address)
        Assertions.assertEquals(officePhoneNumber, response.subjectProfile.officePhoneNumber)
        Assertions.assertEquals(company, response.subjectProfile.company)
        Assertions.assertEquals(team, response.subjectProfile.team)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getSubjectProfile should throw IllegalArgumentException if userId is empty`(userId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        assertThrows<IllegalArgumentException> {
            subjectGrpcController.getSubjectProfile(Empty.newBuilder().build())
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getSubjectStatus should work properly`() = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        coEvery {
            getSubjectProfileUseCase.getSubjectStatus(
                researchstack.backend.domain.subject.Subject.SubjectId.from(userId),
                studyId
            )
        } returns researchstack.backend.domain.subject.SubjectStatusInfo(SubjectStatus.DROP)

        val response = subjectGrpcController.getSubjectStatus(
            GetSubjectStatusRequest.newBuilder()
                .setStudyId(studyId)
                .build()
        )
        Assertions.assertEquals(
            researchstack.backend.grpc.SubjectStatus.SUBJECT_STATUS_DROP,
            response.subjectStatus
        )
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getSubjectStatus should throw IllegalArgumentException if userId is empty`(userId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        assertThrows<IllegalArgumentException> {
            subjectGrpcController.getSubjectStatus(
                GetSubjectStatusRequest.newBuilder()
                    .setStudyId(studyId)
                    .build()
            )
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `getSubjectStatus should throw IllegalArgumentException if studyId is empty`(studyId: String) = runTest {
        coEvery {
            serviceRequestContext.getUserId()
        } returns userId

        assertThrows<IllegalArgumentException> {
            subjectGrpcController.getSubjectStatus(
                GetSubjectStatusRequest.newBuilder()
                    .setStudyId(studyId)
                    .build()
            )
        }
    }
}
