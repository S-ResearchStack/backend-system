package researchstack.backend.application.service.study

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.StudyTestUtil
import researchstack.backend.application.port.outgoing.study.UpdateStudyOutPort

@ExperimentalCoroutinesApi
internal class UpdateStudyServiceTest {
    private val updateStudyOutPort = mockk<UpdateStudyOutPort>()
    private val updateStudyService = UpdateStudyService(
        updateStudyOutPort
    )
    private val studyId = "test-study-id"

    @Test
    @Tag(POSITIVE_TEST)
    fun `updateStudy should work properly`() = runTest {
        val command = StudyTestUtil.createUpdateStudyCommand()
        coEvery {
            updateStudyOutPort.updateStudy(command.toDomain(studyId))
        } returns Unit

        assertDoesNotThrow {
            updateStudyService.updateStudy(studyId, command)
        }
    }
}
