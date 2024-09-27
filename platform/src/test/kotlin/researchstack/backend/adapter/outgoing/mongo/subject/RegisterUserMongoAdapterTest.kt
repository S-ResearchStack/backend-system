package researchstack.backend.adapter.outgoing.mongo.subject

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import reactor.kotlin.core.publisher.toMono
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.SubjectTestUtil
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.SubjectRepository

@ExperimentalCoroutinesApi
internal class RegisterUserMongoAdapterTest {
    private val subjectRepository = mockk<SubjectRepository>()
    private val registerUserMongoAdapter = RegisterSubjectMongoAdapter(
        subjectRepository
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `registerUser should work properly`() = runTest {
        val userDomain = SubjectTestUtil.createRegisterSubjectCommand(
            SubjectTestUtil.subjectId
        ).toDomain()
        val userEntity = userDomain.toEntity()

        coEvery {
            subjectRepository.save(userEntity)
        } returns userEntity.toMono()

        assertDoesNotThrow {
            registerUserMongoAdapter.registerSubject(userDomain)
        }
    }
}
