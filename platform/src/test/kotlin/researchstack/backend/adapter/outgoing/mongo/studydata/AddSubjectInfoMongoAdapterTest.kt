package researchstack.backend.adapter.outgoing.mongo.studydata

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import reactor.core.publisher.Mono
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.SubjectInfoRepository
import researchstack.backend.application.exception.AlreadyExistsException
import researchstack.backend.domain.subject.SubjectInfo
import researchstack.backend.enums.SubjectStatus
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class AddSubjectInfoMongoAdapterTest {
    private val subjectInfoRepository = mockk<SubjectInfoRepository>()

    private val addSubjectInfoMongoAdapter = AddSubjectInfoMongoAdapter(subjectInfoRepository)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `addSubjectInfo should throw AlreadyExistsException when SubjectInfo already exists`() = runTest {
        val subjectInfo = createSubjectInfo()
        every {
            subjectInfoRepository.existsByStudyIdAndSubjectNumber(any(), any())
        } returns Mono.just(true)

        every {
            subjectInfoRepository.save(any())
        } returns Mono.just(
            subjectInfo.toEntity()
        )

        assertThrows<AlreadyExistsException> {
            addSubjectInfoMongoAdapter.addSubjectInfo(
                subjectInfo
            )
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `addSubjectInfo should work properly`() = runTest {
        val subjectInfo = createSubjectInfo()
        every {
            subjectInfoRepository.existsByStudyIdAndSubjectNumber(any(), any())
        } returns Mono.just(false)

        every {
            subjectInfoRepository.save(any())
        } returns Mono.just(
            subjectInfo.toEntity()
        )

        val ret = addSubjectInfoMongoAdapter.addSubjectInfo(subjectInfo)
        assertEquals(subjectInfo, ret)
    }

    private fun createSubjectInfo() = SubjectInfo(
        "id1",
        "id2",
        SubjectStatus.PARTICIPATING,
        "test-subject-id"
    )
}
