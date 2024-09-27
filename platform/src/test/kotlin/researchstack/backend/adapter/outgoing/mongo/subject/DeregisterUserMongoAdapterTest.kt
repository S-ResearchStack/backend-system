package researchstack.backend.adapter.outgoing.mongo.subject

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import reactor.core.publisher.Mono
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.SubjectTestUtil
import researchstack.backend.adapter.outgoing.mongo.repository.SubjectRepository
import researchstack.backend.domain.subject.Subject

@ExperimentalCoroutinesApi
internal class DeregisterUserMongoAdapterTest {
    private val subjectRepository = mockk<SubjectRepository>()
    private val deregisterUserMongoAdapter = DeregisterSubjectMongoAdapter(
        subjectRepository
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `deregisterUser should work properly`() = runTest {
        coEvery {
            subjectRepository.deleteById(SubjectTestUtil.subjectId)
        } returns Mono.empty()

        assertDoesNotThrow {
            deregisterUserMongoAdapter.deregisterSubject(Subject.SubjectId.from(SubjectTestUtil.subjectId))
        }
    }
}
