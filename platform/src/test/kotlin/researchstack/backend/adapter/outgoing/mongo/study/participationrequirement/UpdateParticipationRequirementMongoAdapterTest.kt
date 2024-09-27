package researchstack.backend.adapter.outgoing.mongo.study.participationrequirement

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.ParticipationRequirementRepository
import researchstack.backend.domain.study.InformedConsent
import researchstack.backend.domain.study.ParticipationRequirement

@ExperimentalCoroutinesApi
internal class UpdateParticipationRequirementMongoAdapterTest {
    private val repository = mockk<ParticipationRequirementRepository>()
    private val adapter = UpdateParticipationRequirementMongoAdapter(repository)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `update should throw NoSuchElementException when there is no study matched with the given study id`() =
        runTest {
            val studyId = "s1"
            val requirement = ParticipationRequirement(
                null,
                InformedConsent("http://example.com/image.png"),
                listOf(),
                null
            )

            every {
                repository.findById(studyId)
            } returns Mono.empty()

            assertThrows<NoSuchElementException> {
                adapter.update(studyId, requirement)
            }
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `update should work properly`() = runTest {
        val studyId = "s1"
        val requirement = ParticipationRequirement(
            null,
            InformedConsent("http://example.com/image.png"),
            listOf(),
            null
        )
        val requirementEntity = requirement.toEntity(studyId)

        every {
            repository.findById(studyId)
        } returns requirementEntity.toMono()
        every {
            repository.save(requirementEntity)
        } returns requirementEntity.toMono()

        assertDoesNotThrow {
            adapter.update(studyId, requirement)
        }
    }
}
