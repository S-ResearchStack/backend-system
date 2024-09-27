package researchstack.backend.adapter.outgoing.mongo.study.participationrequirement

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import reactor.core.publisher.Mono
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.mongo.repository.ParticipationRequirementRepository

@ExperimentalCoroutinesApi
internal class DeleteParticipationRequirementMongoAdapterTest {
    private val repository = mockk<ParticipationRequirementRepository>()
    private val adapter = DeleteParticipationRequirementMongoAdapter(repository)

    @Test
    @Tag(POSITIVE_TEST)
    fun `delete should work properly`() = runTest {
        val studyId = "s1"

        every {
            repository.deleteById(studyId)
        } returns Mono.empty()

        assertDoesNotThrow {
            adapter.delete(studyId)
        }
    }
}
