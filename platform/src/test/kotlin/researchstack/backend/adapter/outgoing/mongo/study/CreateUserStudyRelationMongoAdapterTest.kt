package researchstack.backend.adapter.outgoing.mongo.study

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import reactor.kotlin.core.publisher.toMono
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.mongo.entity.study.SubjectStudyRelationEntity
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.SubjectStudyRelationRepository
import researchstack.backend.adapter.outgoing.mongo.study.studyrelation.CreateUserStudyRelationMongoAdapter
import researchstack.backend.domain.study.EligibilityTestResult
import researchstack.backend.domain.study.SignedInformedConsent
import researchstack.backend.domain.task.TaskResult
import researchstack.backend.enums.SubjectStatus

@ExperimentalCoroutinesApi
internal class CreateUserStudyRelationMongoAdapterTest {
    private val subjectStudyRelationRepository = mockk<SubjectStudyRelationRepository>()
    private val adapter = CreateUserStudyRelationMongoAdapter(subjectStudyRelationRepository)

    @Test
    @Tag(POSITIVE_TEST)
    fun `participateInStudy should work properly`() = runTest {
        val userId = "u1"
        val studyId = "s1"
        val subjectNumber = "1"
        val sessionId = "test-session-id"
        val imagePath = "http://www.example.com/image"
        val eligibilityTestResult = EligibilityTestResult(TaskResult.SurveyResult(listOf()))
        val subjectStudyRelationEntity = SubjectStudyRelationEntity(
            null,
            userId,
            subjectNumber,
            studyId,
            imagePath,
            eligibilityTestResult.toEntity()
        )

        every {
            subjectStudyRelationRepository.save(subjectStudyRelationEntity)
        } returns SubjectStudyRelationEntity(
            "1",
            userId,
            subjectNumber,
            studyId,
            imagePath,
            eligibilityTestResult.toEntity()
        ).toMono()

        assertDoesNotThrow {
            adapter.participateInStudy(
                userId,
                studyId,
                subjectNumber,
                SubjectStatus.PARTICIPATING,
                sessionId,
                eligibilityTestResult,
                SignedInformedConsent(imagePath)
            )
        }
    }
}
