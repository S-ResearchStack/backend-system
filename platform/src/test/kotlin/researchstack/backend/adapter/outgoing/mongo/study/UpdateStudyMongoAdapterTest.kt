package researchstack.backend.adapter.outgoing.mongo.study

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.dao.DuplicateKeyException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.StudyTestUtil
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.StudyRepository
import researchstack.backend.application.exception.AlreadyExistsException

@ExperimentalCoroutinesApi
internal class UpdateStudyMongoAdapterTest {
    private val studyRepository = mockk<StudyRepository>()
    private val adapter = UpdateStudyMongoAdapter(studyRepository)

    @Test
    @Tag(POSITIVE_TEST)
    fun `updateStudy should work properly`() = runTest {
        val study = StudyTestUtil.createDummyStudy("id")
        val studyEntity = study.toEntity()

        every {
            studyRepository.findById(study.id)
        } returns studyEntity.toMono()
        every {
            studyRepository.save(studyEntity)
        } returns studyEntity.toMono()

        assertDoesNotThrow {
            adapter.updateStudy(study)
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `updateStudy should throw NoSuchElementException when there is no study matched with the given study id`() =
        runTest {
            val study = StudyTestUtil.createDummyStudy("id")

            every {
                studyRepository.findById(study.id)
            } returns Mono.empty()

            assertThrows<NoSuchElementException> {
                adapter.updateStudy(study)
            }
        }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `updateStudy should throw AlreadyExistsException when it fails to update because of a duplicate key`() =
        runTest {
            val study = StudyTestUtil.createDummyStudy("id")

            every {
                studyRepository.findById(study.id)
            } returns Mono.error(DuplicateKeyException("duplicate key"))

            assertThrows<AlreadyExistsException> {
                adapter.updateStudy(study)
            }
        }
}
