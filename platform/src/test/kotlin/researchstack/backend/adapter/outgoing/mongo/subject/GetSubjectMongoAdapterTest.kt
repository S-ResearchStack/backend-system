package researchstack.backend.adapter.outgoing.mongo.subject

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import reactor.kotlin.core.publisher.toMono
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.SubjectTestUtil
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.SubjectInfoRepository
import researchstack.backend.adapter.outgoing.mongo.repository.SubjectRepository
import researchstack.backend.domain.subject.Subject

@ExperimentalCoroutinesApi
internal class GetSubjectMongoAdapterTest {
    private val subjectRepository = mockk<SubjectRepository>()
    private val subjectInfoRepository = mockk<SubjectInfoRepository>()
    private val getUserProfileMongoAdapter = GetSubjectProfileMongoAdapter(
        subjectRepository,
        subjectInfoRepository
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `getUserProfile should work properly`() = runTest {
        val userDomain = SubjectTestUtil.createRegisterSubjectCommand(
            SubjectTestUtil.subjectId
        ).toDomain()
        val userEntity = userDomain.toEntity()

        coEvery {
            subjectRepository.findById(userDomain.subjectId.value)
        } returns userEntity.toMono()

        val response = getUserProfileMongoAdapter.getSubjectProfile(Subject.SubjectId.from(userDomain.subjectId.value))
        SubjectTestUtil.compareSubjectProfile(userDomain, response)
    }
}
