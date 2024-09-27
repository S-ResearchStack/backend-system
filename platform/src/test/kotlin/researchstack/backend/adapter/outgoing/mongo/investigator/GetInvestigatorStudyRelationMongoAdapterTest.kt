package researchstack.backend.adapter.outgoing.mongo.investigator

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder
import reactor.kotlin.core.publisher.toFlux
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.mongo.mapper.toDomain
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.InvestigatorStudyRelationRepository
import researchstack.backend.domain.common.Email
import researchstack.backend.domain.investigator.InvestigatorStudyRelation
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class GetInvestigatorStudyRelationMongoAdapterTest {
    private val relationRepository = mockk<InvestigatorStudyRelationRepository>()

    private val getInvestigatorStudyRelationMongoAdapter = GetInvestigatorStudyRelationMongoAdapter(relationRepository)

    @Tag(POSITIVE_TEST)
    @Test
    fun `getInvestigatorRelation should return investigatorStudyRelation`() = runTest {
        val email = Email("test@test.com")
        val studyId = "test-studyId"
        val role = "test-role"
        val relations = listOf(
            InvestigatorStudyRelation(
                email = email,
                studyId = studyId,
                role = role
            )
        )

        val relationEntities = relations.map { it.toEntity() }

        every {
            relationRepository.findAllByEmail(email.value)
        } answers {
            relationEntities.toFlux()
        }

        val result = getInvestigatorStudyRelationMongoAdapter.getRelationsByEmail(email.value)

        assertEquals(result.size, relationEntities.size)

        for (i: Int in result.indices) {
            assertTrue(EqualsBuilder.reflectionEquals(result[i], relationEntities[i].toDomain()))
        }
    }
}
