package researchstack.backend.adapter.outgoing.mongo.investigator

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.mongo.entity.studydata.InvestigatorEntity
import researchstack.backend.adapter.outgoing.mongo.entity.studydata.InvestigatorStudyRelationEntity
import researchstack.backend.adapter.outgoing.mongo.mapper.toDomain
import researchstack.backend.adapter.outgoing.mongo.repository.InvestigatorRepository
import researchstack.backend.adapter.outgoing.mongo.repository.InvestigatorStudyRelationRepository
import researchstack.backend.domain.common.Email

@ExperimentalCoroutinesApi
internal class GetInvestigatorMongoAdapterTest {
    private val investigatorRepository = mockk<InvestigatorRepository>()
    private val relationRepository = mockk<InvestigatorStudyRelationRepository>()

    private val getInvestigatorMongoAdapter = GetInvestigatorMongoAdapter(investigatorRepository, relationRepository)

    @Tag(POSITIVE_TEST)
    @Test
    fun `getInvestigator should return investigator`() = runTest {
        val investigatorId = "test-investigator"
        val firstName = "test-firstName"
        val lastName = "test-lastName"
        val company = "test-company"
        val team = "test-team"
        val email = Email("test@test.com")
        val officePhoneNumber = "123-0000-0000"
        val mobilePhoneNumber = "123-0000-0000"
        val investigatorEntity = InvestigatorEntity(
            id = investigatorId,
            email = email.value,
            firstName = firstName,
            lastName = lastName,
            company = company,
            team = team,
            officePhoneNumber = officePhoneNumber,
            mobilePhoneNumber = mobilePhoneNumber
        )

        every {
            investigatorRepository.findById(investigatorId)
        } answers {
            investigatorEntity.toMono()
        }

        val result = getInvestigatorMongoAdapter.getInvestigator(investigatorId)
        assertEquals(result, investigatorEntity.toDomain())
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `getInvestigatorByEmail should return investigator`() = runTest {
        val id = "test-investigator"
        val firstName = "test-firstName"
        val lastName = "test-lastName"
        val company = "test-company"
        val team = "test-team"
        val email = Email("test@test.com")
        val officePhoneNumber = "123-0000-0000"
        val mobilePhoneNumber = "123-0000-0000"
        val investigatorEntity = InvestigatorEntity(
            id = id,
            email = email.value,
            firstName = firstName,
            lastName = lastName,
            company = company,
            team = team,
            officePhoneNumber = officePhoneNumber,
            mobilePhoneNumber = mobilePhoneNumber
        )

        every {
            investigatorRepository.findByEmail(email.value)
        } answers {
            investigatorEntity.toMono()
        }

        val result = getInvestigatorMongoAdapter.getInvestigatorByEmail(email.value)
        assertEquals(result, investigatorEntity.toDomain())
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `getInvestigatorsByStudyId should return investigators`() = runTest {
        val email = Email("test@test.com")
        val id = "test-id"
        val studyId = "test-studyId"
        val role = "test-role"
        val relationEntities = listOf(
            InvestigatorStudyRelationEntity(
                id = id,
                email = email.value,
                studyId = studyId,
                role = role
            )
        )

        val investigatorId = "test-investigator"
        val firstName = "test-firstName"
        val lastName = "test-lastName"
        val company = "test-company"
        val team = "test-team"
        val officePhoneNumber = "123-0000-0000"
        val mobilePhoneNumber = "123-0000-0000"
        val investigatorEntities = listOf(
            InvestigatorEntity(
                id = investigatorId,
                email = email.value,
                firstName = firstName,
                lastName = lastName,
                company = company,
                team = team,
                officePhoneNumber = officePhoneNumber,
                mobilePhoneNumber = mobilePhoneNumber
            )
        )

        every {
            relationRepository.findAllByStudyId(studyId)
        } answers {
            relationEntities.toFlux()
        }

        every {
            investigatorRepository.findAllByEmailIn(listOf(email.value))
        } answers {
            investigatorEntities.toFlux()
        }

        val result = getInvestigatorMongoAdapter.getInvestigatorsByStudyId(studyId)
        assertArrayEquals(
            result.map { it.hashCode() }.toTypedArray(),
            investigatorEntities.map { it.toDomain().hashCode() }.toTypedArray()
        )
    }
}
