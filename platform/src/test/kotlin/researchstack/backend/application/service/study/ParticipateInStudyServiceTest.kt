package researchstack.backend.application.service.study

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.exception.AlreadyExistsException
import researchstack.backend.application.exception.NotFoundException
import researchstack.backend.application.port.incoming.study.ParticipateInStudyCommand
import researchstack.backend.application.port.incoming.study.ParticipateInStudyCommand.EligibilityTestResultCommand
import researchstack.backend.application.port.incoming.study.ParticipateInStudyCommand.SignedInformedConsentCommand
import researchstack.backend.application.port.incoming.task.TaskResultCommand.SurveyResult
import researchstack.backend.application.port.outgoing.casbin.AddRoleOutPort
import researchstack.backend.application.port.outgoing.study.CreateSubjectNumberOutPort
import researchstack.backend.application.port.outgoing.study.ParticipateInStudyOutPort
import researchstack.backend.application.port.outgoing.studydata.AddSubjectInfoOutPort
import researchstack.backend.application.port.outgoing.subject.GetSubjectProfileOutPort
import researchstack.backend.domain.subject.Subject
import researchstack.backend.domain.subject.SubjectInfo
import researchstack.backend.enums.SubjectStatus
import kotlin.random.Random
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class ParticipateInStudyServiceTest {
    private val participateInStudyOutPort = mockk<ParticipateInStudyOutPort>()
    private val createSubjectNumberOutPort = mockk<CreateSubjectNumberOutPort>()
    private val getSubjectProfileOutPort = mockk<GetSubjectProfileOutPort>()
    private val addSubjectInfoOutPort = mockk<AddSubjectInfoOutPort>()
    private val addRoleOutPort = mockk<AddRoleOutPort>()
    private val participateInStudyService = ParticipateInStudyService(
        createSubjectNumberOutPort,
        getSubjectProfileOutPort,
        participateInStudyOutPort,
        addSubjectInfoOutPort,
        addRoleOutPort
    )

    private val testSubjectId = "test-subject-id"
    private val testStudyId = "test-study-id"
    private val subject = Subject.new(
        subjectId = testSubjectId,
        firstName = "tester",
        lastName = "park",
        birthdayYear = 1996,
        birthdayMonth = 2,
        birthdayDay = 27,
        email = "tester.park@samsung.com",
        phoneNumber = "010-1234-5678",
        address = "seoul"
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `participateInStudy should return subject id`() = runTest {
        val subjectNumber = Random.nextLong(1, 100).toString()
        val subjectInfo = SubjectInfo(
            studyId = testStudyId,
            subjectNumber = subjectNumber,
            status = SubjectStatus.PARTICIPATING,
            subjectId = testSubjectId
        )
        coEvery { getSubjectProfileOutPort.getSubjectProfile(Subject.SubjectId.from(testSubjectId)) } returns subject
        coEvery { createSubjectNumberOutPort.createSubjectNumber(testStudyId, testSubjectId) } returns subjectNumber
        coEvery {
            participateInStudyOutPort.participateInStudy(
                testSubjectId,
                testStudyId,
                subjectNumber,
                SubjectStatus.PARTICIPATING,
                any(),
                any(),
                any()
            )
        } returns Unit
        coEvery { addSubjectInfoOutPort.addSubjectInfo(subjectInfo) } returns subjectInfo
        coEvery { addRoleOutPort.addParticipantRole(testSubjectId, testStudyId) } returns true

        val actualSubjectNumber = tryToParticipateToStudy()

        assertEquals(subjectNumber, actualSubjectNumber)
        coVerify {
            participateInStudyOutPort.participateInStudy(
                testSubjectId,
                testStudyId,
                subjectNumber,
                SubjectStatus.PARTICIPATING,
                any(),
                any(),
                any()
            )
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `participateInStudy should throw AlreadyExistsException when user already participated target study`() =
        runTest {
            coEvery { getSubjectProfileOutPort.getSubjectProfile(Subject.SubjectId.from(testSubjectId)) } returns subject
            coEvery {
                createSubjectNumberOutPort.createSubjectNumber(
                    testStudyId,
                    testSubjectId
                )
            } throws AlreadyExistsException()
            coEvery { addRoleOutPort.addParticipantRole(testSubjectId, testStudyId) } returns true

            assertThrows<AlreadyExistsException> {
                tryToParticipateToStudy()
            }
        }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `participateInStudy should throw NotFoundException when user profile is not registered yet`() = runTest {
        coEvery {
            getSubjectProfileOutPort.getSubjectProfile(Subject.SubjectId.from(testSubjectId))
        } throws NoSuchElementException()
        coEvery { addRoleOutPort.addParticipantRole(testSubjectId, testStudyId) } returns true

        assertThrows<NotFoundException> {
            tryToParticipateToStudy()
        }
    }

    private suspend fun tryToParticipateToStudy() = participateInStudyService.participateInStudy(
        testSubjectId,
        testStudyId,
        ParticipateInStudyCommand(
            EligibilityTestResultCommand(SurveyResult(emptyList())),
            SignedInformedConsentCommand("imagepath")
        )
    )
}
