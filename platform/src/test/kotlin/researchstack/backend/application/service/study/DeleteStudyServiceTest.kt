package researchstack.backend.application.service.study

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.port.outgoing.casbin.DeleteStudyPolicyOutPort
import researchstack.backend.application.port.outgoing.investigator.DeleteInvestigatorStudyRelationOutPort
import researchstack.backend.application.port.outgoing.study.DeleteStudyOutPort

@ExperimentalCoroutinesApi
internal class DeleteStudyServiceTest {
    private val deleteStudyOutPort = mockk<DeleteStudyOutPort>()
    private val deleteStudyPolicyOutPort = mockk<DeleteStudyPolicyOutPort>(relaxed = true)
    private val deleteInvestigatorStudyRelationOutPort = mockk<DeleteInvestigatorStudyRelationOutPort>(relaxed = true)
    private val deleteStudyService = DeleteStudyService(
        deleteStudyOutPort,
        deleteStudyPolicyOutPort,
        deleteInvestigatorStudyRelationOutPort
    )
    private val studyId = "test-study-id"

    @Test
    @Tag(POSITIVE_TEST)
    fun `deleteStudy should work properly`() = runTest {
        coEvery {
            deleteStudyOutPort.deleteStudy(studyId)
        } returns Unit

        assertDoesNotThrow {
            deleteStudyService.deleteStudy(studyId)
        }
    }
}
