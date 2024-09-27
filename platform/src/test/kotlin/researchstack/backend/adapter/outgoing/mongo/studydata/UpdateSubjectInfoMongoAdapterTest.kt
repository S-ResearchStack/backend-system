package researchstack.backend.adapter.outgoing.mongo.studydata

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.mongo.entity.studydata.SubjectInfoEntity
import researchstack.backend.adapter.outgoing.mongo.repository.SubjectInfoRepository
import researchstack.backend.enums.SubjectStatus

@ExperimentalCoroutinesApi
internal class UpdateSubjectInfoMongoAdapterTest {
    private val subjectInfoRepository = mockk<SubjectInfoRepository>()
    private val updateSubjectInfoMongoAdapter = UpdateSubjectInfoMongoAdapter(
        subjectInfoRepository
    )
    private val studyId = "test-study-id"
    private val subjectNumber = "test-subject-number"
    private val userId = "test-subject-id"
    private val subjectInfoEntity = SubjectInfoEntity(
        "test-id",
        studyId,
        subjectNumber,
        SubjectStatus.PARTICIPATING,
        userId
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `updateSubjectStatus should work properly`() = runTest {
        coEvery {
            subjectInfoRepository.findByStudyIdAndSubjectNumber(
                studyId,
                subjectNumber
            )
        } returns subjectInfoEntity.toMono()
        coEvery {
            subjectInfoRepository.save(subjectInfoEntity)
        } returns Mono.just(subjectInfoEntity)

        assertDoesNotThrow {
            updateSubjectInfoMongoAdapter.updateSubjectStatus(
                studyId,
                subjectNumber,
                SubjectStatus.PARTICIPATING,
                userId
            )
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `updateSubjectStatus should throw NoSuchElementException if there is no matched data`() = runTest {
        coEvery {
            subjectInfoRepository.findByStudyIdAndSubjectNumber(
                studyId,
                subjectNumber
            )
        } returns Mono.empty()

        assertThrows<NoSuchElementException> {
            updateSubjectInfoMongoAdapter.updateSubjectStatus(
                studyId,
                subjectNumber,
                SubjectStatus.PARTICIPATING,
                userId
            )
        }
    }
}
