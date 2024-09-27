package researchstack.backend.adapter.outgoing.mongo.study

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.StudyTestUtil
import researchstack.backend.adapter.outgoing.mongo.entity.study.SubjectStudyRelationEntity
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.StudyRepository
import researchstack.backend.adapter.outgoing.mongo.repository.SubjectStudyRelationRepository
import researchstack.backend.enums.StudyScope
import researchstack.backend.enums.StudyStage
import kotlin.test.assertContentEquals

@ExperimentalCoroutinesApi
internal class GetStudyMongoAdapterTest {
    private val studyRepository = mockk<StudyRepository>()
    private val subjectStudyRelationRepository = mockk<SubjectStudyRelationRepository>()
    private val adapter = GetStudyMongoAdapter(studyRepository, subjectStudyRelationRepository)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getStudy should throw NoSuchElementException when there is no study matched with the study id`() = runTest {
        val studyId = "tid"

        every {
            studyRepository.findById(studyId)
        } returns Mono.empty()

        assertThrows<NoSuchElementException> {
            adapter.getStudy(studyId)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getStudy should work properly`() = runTest {
        val studyId = "tid"
        val study = StudyTestUtil.createDummyStudy(studyId)

        every {
            studyRepository.findById(studyId)
        } returns study.toEntity().toMono()

        assertDoesNotThrow {
            adapter.getStudy(studyId)
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getStudyByParticipationCode should throw NoSuchElementException when there is no study matched with the given code`() =
        runTest {
            val code = "testCode"

            every {
                studyRepository.findByParticipationCodeAndStudyInfoStage(code, StudyStage.STARTED_OPEN)
            } returns Mono.empty()

            assertThrows<NoSuchElementException> {
                adapter.getStudyByParticipationCode(code)
            }
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getStudyByParticipationCode should work properly`() = runTest {
        val code = "testCode"
        val study = StudyTestUtil.createDummyStudy("id")

        every {
            studyRepository.findByParticipationCodeAndStudyInfoStage(code, StudyStage.STARTED_OPEN)
        } returns study.toEntity().toMono()

        assertDoesNotThrow {
            adapter.getStudyByParticipationCode(code)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getStudyList should work properly`() = runTest {
        val study = StudyTestUtil.createDummyStudy("id")

        every {
            studyRepository.findByStudyInfoStage(StudyStage.STARTED_OPEN)
        } returns listOf(study.toEntity()).toFlux()

        assertDoesNotThrow {
            adapter.getStudyList()
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getPublicStudyList should work properly`() = runTest {
        val study = StudyTestUtil.createDummyStudy("id")

        every {
            studyRepository.findByStudyInfoScopeAndStudyInfoStage(StudyScope.PUBLIC, StudyStage.STARTED_OPEN)
        } returns listOf(study.toEntity()).toFlux()

        assertDoesNotThrow {
            adapter.getPublicStudyList()
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getParticipatedStudyList should work properly`() = runTest {
        val userId = "u1"
        val expected = listOf("s1", "s3", "s5")
        every {
            studyRepository.findByStudyInfoStage(StudyStage.STARTED_OPEN)
        } returns listOf(
            StudyTestUtil.createDummyStudy("s1").toEntity(),
            StudyTestUtil.createDummyStudy("s2").toEntity(),
            StudyTestUtil.createDummyStudy("s3").toEntity(),
            StudyTestUtil.createDummyStudy("s4").toEntity(),
            StudyTestUtil.createDummyStudy("s5").toEntity()
        ).toFlux()

        every {
            subjectStudyRelationRepository.findBySubjectId(userId)
        } returns listOf(
            SubjectStudyRelationEntity("1", userId, "sub1", "s1", "ex.png", null),
            SubjectStudyRelationEntity("2", userId, "sub2", "s3", "ex.png", null),
            SubjectStudyRelationEntity("3", userId, "sub3", "s5", "ex.png", null)
        ).toFlux()

        assertContentEquals(
            adapter.getParticipatedStudyList(userId)
                .map { it.id }.sorted(),
            expected.sorted()
        )
    }
}
