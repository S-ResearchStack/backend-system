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
import researchstack.backend.application.port.outgoing.subject.UpdateSubjectProfileOutPort
import researchstack.backend.domain.subject.Subject.SubjectId

@ExperimentalCoroutinesApi
internal class UpdateSubjectServiceTest {
    private val updateSubjectProfileOutPort = mockk<UpdateSubjectProfileOutPort>()
    private val updateUserProfileService = UpdateSubjectProfileService(
        updateSubjectProfileOutPort
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `updateUserProfile should work properly`() = runTest {
        val userId = SubjectTestUtil.subjectId
        val updateUserProfileCommand = SubjectTestUtil.createUpdateSubjectProfileCommand()
        val userProfile = updateUserProfileCommand.toDomain(userId)

        coEvery {
            updateSubjectProfileOutPort.updateSubjectProfile(userId, userProfile)
        } returns Unit

        assertDoesNotThrow {
            updateUserProfileService.updateSubjectProfile(SubjectId.from(userId), updateUserProfileCommand)
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `updateUserProfile should throw IllegalArgumentException if userId is null`() = runTest {
        val userId = null
        val updateUserProfileCommand = SubjectTestUtil.createUpdateSubjectProfileCommand()
        assertThrows<IllegalArgumentException> {
            updateUserProfileService.updateSubjectProfile(SubjectId.from(userId), updateUserProfileCommand)
        }
    }
}
