package researchstack.backend.adapter.outgoing.mongo.investigator

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import reactor.kotlin.core.publisher.toMono
import researchstack.backend.InvestigatorTestUtil
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.InvestigatorRepository

@ExperimentalCoroutinesApi
internal class UpdateInvestigatorMongoAdapterTest {
    private val investigatorRepository = mockk<InvestigatorRepository>()
    private val updateInvestigatorMongoAdapter = UpdateInvestigatorMongoAdapter(investigatorRepository)

    @Test
    @Tag(POSITIVE_TEST)
    fun `updateInvestigator should work properly`() = runTest {
        val investigator = InvestigatorTestUtil.createInvestigator()
        val investigatorEntity = investigator.toEntity()

        every {
            investigatorRepository.findById(investigator.id)
        } returns investigatorEntity.toMono()
        every {
            investigatorRepository.save(investigatorEntity)
        } returns investigatorEntity.toMono()

        assertDoesNotThrow {
            updateInvestigatorMongoAdapter.updateInvestigator(investigator)
        }
    }
}
