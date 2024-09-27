package researchstack.backend.adapter.outgoing.mongo.investigator

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder
import reactor.kotlin.core.publisher.toMono
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.mongo.mapper.toDomain
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.InvestigatorStudyRelationRepository
import researchstack.backend.domain.common.Email
import researchstack.backend.domain.investigator.InvestigatorStudyRelation

@ExperimentalCoroutinesApi
internal class UpdateInvestigatorStudyRelationMongoAdapterTest {
    private val relationRepository = mockk<InvestigatorStudyRelationRepository>()

    private val updateInvestigatorStudyRelationMongoAdapter =
        UpdateInvestigatorStudyRelationMongoAdapter(relationRepository)

    @Tag(POSITIVE_TEST)
    @Test
    fun `updateInvestigatorRelation should return updated investigatorStudyRelation`() = runTest {
        val email = Email("test@test.com")
        val studyId = "test-studyId"
        val roleBefore = "test-role-before"
        val roleAfter = "test-role-after"
        val relationBefore = InvestigatorStudyRelation(
            email = email,
            studyId = studyId,
            role = roleBefore
        )

        val relationAfter = InvestigatorStudyRelation(
            email = email,
            studyId = studyId,
            role = roleAfter
        )

        val relationEntityBefore = relationBefore.toEntity()
        val relationEntityAfter = relationAfter.toEntity()

        every {
            relationRepository.findByEmailAndStudyId(email.value, studyId)
        } answers {
            relationEntityBefore.toMono()
        }

        every {
            relationRepository.save(relationEntityAfter)
        } answers {
            relationEntityAfter.toMono()
        }

        val result = updateInvestigatorStudyRelationMongoAdapter.updateRelation(email.value, studyId, roleAfter)
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(result, relationEntityAfter.toDomain()))
    }
}
