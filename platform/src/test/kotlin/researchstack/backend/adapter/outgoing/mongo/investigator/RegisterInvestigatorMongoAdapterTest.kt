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
import researchstack.backend.adapter.outgoing.mongo.repository.InvestigatorRepository
import researchstack.backend.domain.common.Email
import researchstack.backend.domain.investigator.Investigator

@ExperimentalCoroutinesApi
internal class RegisterInvestigatorMongoAdapterTest {
    private val investigatorRepository = mockk<InvestigatorRepository>()

    private val registerInvestigatorMongoAdapter = RegisterInvestigatorMongoAdapter(investigatorRepository)

    @Tag(POSITIVE_TEST)
    @Test
    fun `registerInvestigator should return registered investigator`() = runTest {
        val id = "test-investigator"
        val firstName = "test-firstName"
        val lastName = "test-lastName"
        val company = "test-company"
        val team = "test-team"
        val email = Email("test@test.com")
        val officePhoneNumber = "123-0000-0000"
        val mobilePhoneNumber = "123-0000-0000"
        val investigator = Investigator(
            id = id,
            firstName = firstName,
            lastName = lastName,
            company = company,
            team = team,
            email = email,
            officePhoneNumber = officePhoneNumber,
            mobilePhoneNumber = mobilePhoneNumber
        )

        val investigatorEntity = investigator.toEntity()

        every {
            investigatorRepository.insert(investigatorEntity)
        } answers {
            investigatorEntity.toMono()
        }

        val result = registerInvestigatorMongoAdapter.registerInvestigator(investigator)
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(result, investigatorEntity.toDomain()))
    }
}
