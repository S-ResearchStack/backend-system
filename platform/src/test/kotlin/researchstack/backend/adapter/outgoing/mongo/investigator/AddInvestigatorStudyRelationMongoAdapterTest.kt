package researchstack.backend.adapter.outgoing.mongo.investigator

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder
import reactor.kotlin.core.publisher.toMono
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.mongo.mapper.toDomain
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.InvestigatorStudyRelationRepository
import researchstack.backend.application.exception.AlreadyExistsException
import researchstack.backend.domain.common.Email
import researchstack.backend.domain.investigator.InvestigatorStudyRelation

@ExperimentalCoroutinesApi
internal class AddInvestigatorStudyRelationMongoAdapterTest {
    private val relationRepository = mockk<InvestigatorStudyRelationRepository>()

    private val addInvestigatorStudyRelationAdapter = AddInvestigatorStudyRelationMongoAdapter(relationRepository)

    @Tag(POSITIVE_TEST)
    @Test
    fun `addInvestigatorStudyRelation should return added investigatorStudyRelation`() = runTest {
        val email = Email("test@test.com")
        val studyId = "test-studyId"
        val role = "test-role"
        val relation = InvestigatorStudyRelation(
            email = email,
            studyId = studyId,
            role = role
        )
        val relationEntity = relation.toEntity()

        every {
            relationRepository.existsByEmailAndStudyId(email.value, studyId)
        } answers {
            false.toMono()
        }

        every {
            relationRepository.insert(relationEntity)
        } answers {
            relationEntity.toMono()
        }

        val result = addInvestigatorStudyRelationAdapter.addRelation(relation)
        assertTrue(EqualsBuilder.reflectionEquals(result, relationEntity.toDomain()))
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `addInvestigatorStudyRelation should throw AlreadyExistsException`() = runTest {
        val email = Email("test@test.com")
        val studyId = "test-studyId"
        val role = "test-role"
        val relation = InvestigatorStudyRelation(
            email = email,
            studyId = studyId,
            role = role
        )
        every {
            relationRepository.existsByEmailAndStudyId(email.value, studyId)
        } answers {
            true.toMono()
        }

        assertThrows<AlreadyExistsException> {
            addInvestigatorStudyRelationAdapter.addRelation(relation)
        }
    }
}
