package researchstack.backend.application.service.subject

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.SubjectTestUtil
import researchstack.backend.application.port.outgoing.casbin.DeleteRoleOutPort
import researchstack.backend.application.port.outgoing.subject.DeregisterSubjectOutPort
import researchstack.backend.domain.subject.Subject.SubjectId

@ExperimentalCoroutinesApi
internal class DeregisterSubjectServiceTest {
    private val deregisterSubjectOutPort = mockk<DeregisterSubjectOutPort>()
    private val deleteRoleOutPort = mockk<DeleteRoleOutPort>(relaxed = true)
    private val deregisterSubjectService = DeregisterSubjectService(
        deregisterSubjectOutPort,
        deleteRoleOutPort
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `deregisterSubject should work properly`() = runTest {
        val subjectId = SubjectTestUtil.subjectId
        coEvery {
            deregisterSubjectOutPort.deregisterSubject(SubjectId.from(subjectId))
        } returns Unit

        assertDoesNotThrow {
            deregisterSubjectService.deregisterSubject(SubjectId.from(subjectId))
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `deregisterSubject should throw IllegalArgumentException if userId is null`() = runTest {
        val subjectId = null
        assertThrows<IllegalArgumentException> {
            deregisterSubjectService.deregisterSubject(SubjectId.from(subjectId))
        }
    }
}
