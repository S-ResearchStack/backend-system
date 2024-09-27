package researchstack.backend.adapter.outgoing.mongo.studydata

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import reactor.core.publisher.Mono
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.mongo.repository.SubjectInfoRepository
import researchstack.backend.domain.subject.SubjectInfo
import researchstack.backend.enums.SubjectStatus

@ExperimentalCoroutinesApi
class DeleteSubjectInfoMongoAdapterTest {
    private val subjectInfoRepository = mockk<SubjectInfoRepository>()

    private val deleteSubjectInfoMongoAdapter = DeleteSubjectInfoMongoAdapter(subjectInfoRepository)

    private val userId = "userId"
    private val studyId = "studyId"
    private val subjectNumber = "subjectNumber"

    @Test
    @Tag(POSITIVE_TEST)
    fun `deleteSubjectInfo should work properly`() = runTest {
        every {
            subjectInfoRepository.deleteByStudyIdAndSubjectId(studyId, userId)
        } returns Mono.empty()

        assertDoesNotThrow {
            deleteSubjectInfoMongoAdapter.deleteSubjectInfo(
                studyId = studyId,
                subjectId = userId
            )
        }
    }

    private fun createSubjectInfo() = SubjectInfo(
        studyId,
        subjectNumber,
        SubjectStatus.PARTICIPATING,
        userId
    )
}
