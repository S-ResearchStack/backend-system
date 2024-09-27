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
internal class UpdateSubjectMongoAdapterTest {
    private val subjectRepository = mockk<SubjectRepository>()
    private val updateUserProfileMongoAdapter = UpdateSubjectProfileMongoAdapter(
        subjectRepository
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `updateUserProfile should work properly`() = runTest {
        val userDomain = SubjectTestUtil.createUpdateSubjectProfileCommand().toDomain(SubjectTestUtil.subjectId)
        val userEntity = userDomain.toEntity()

        coEvery {
            subjectRepository.save(userEntity)
        } returns userEntity.toMono()

        assertDoesNotThrow {
            updateUserProfileMongoAdapter.updateSubjectProfile(userDomain.subjectId.value, userDomain)
        }
    }
}
