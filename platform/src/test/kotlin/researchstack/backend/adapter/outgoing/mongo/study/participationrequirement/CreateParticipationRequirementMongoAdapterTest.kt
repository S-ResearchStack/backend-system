package researchstack.backend.adapter.outgoing.mongo.study.participationrequirement

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
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.ParticipationRequirementRepository
import researchstack.backend.application.exception.AlreadyExistsException
import researchstack.backend.domain.study.InformedConsent
import researchstack.backend.domain.study.ParticipationRequirement

@ExperimentalCoroutinesApi
internal class CreateParticipationRequirementMongoAdapterTest {
    private val repository = mockk<ParticipationRequirementRepository>()
    private val adapter = CreateParticipationRequirementMongoAdapter(repository)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `create should throw AlreadyExistsException when it fails to create because of a duplicate key`() = runTest {
        val studyId = "s1"
        val requirement = ParticipationRequirement(
            null,
            InformedConsent("http://example.com/image.png"),
            listOf(),
            null
        )

        every {
            repository.save(requirement.toEntity(studyId))
        } returns Mono.error(DuplicateKeyException("duplicate key"))

        assertThrows<AlreadyExistsException> {
            adapter.create(requirement, studyId)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `create should work properly`() = runTest {
        val studyId = "s1"
        val requirement = ParticipationRequirement(
            null,
            InformedConsent("http://example.com/image.png"),
            listOf(),
            null
        )
        val requirementEntity = requirement.toEntity(studyId)

        every {
            repository.save(requirementEntity)
        } returns requirementEntity.toMono()

        assertDoesNotThrow {
            adapter.create(requirement, studyId)
        }
    }
}
