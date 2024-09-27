package researchstack.backend.adapter.outgoing.mongo.studydata

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.mongo.entity.studydata.SubjectInfoEntity
import researchstack.backend.adapter.outgoing.mongo.mapper.toDomain
import researchstack.backend.adapter.outgoing.mongo.repository.SubjectInfoRepository
import researchstack.backend.enums.SubjectStatus
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class GetSubjectInfoMongoAdapterTest {
    private val subjectInfoRepository = mockk<SubjectInfoRepository>()
    private val reactiveMongoTemplate = mockk<ReactiveMongoTemplate>()
    private val getSubjectInfoMongoAdapter = GetSubjectInfoMongoAdapter(
        subjectInfoRepository,
        reactiveMongoTemplate
    )
    private val studyId = "test-study-id"
    private val subjectNumber = "test-subject-number"
    private val userId = "test-subject-id"
    private val subjectInfoEntity = SubjectInfoEntity(
        "test-id",
        studyId,
        subjectNumber,
        SubjectStatus.PARTICIPATING,
        userId
    )
    private val subjectInfoEntities = listOf(
        subjectInfoEntity
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `getSubjectInfoList should work properly`() = runTest {
        coEvery {
            reactiveMongoTemplate.aggregate(any<Aggregation>(), any<String>(), SubjectInfoEntity::class.java)
        } answers {
            subjectInfoEntities.toFlux()
        }

        val actual = getSubjectInfoMongoAdapter.getSubjectInfoList(studyId)
        assertEquals(subjectInfoEntities.size, actual.size)
        for (i in actual.indices) {
            assertEquals(subjectInfoEntities[i].toDomain(), actual[i])
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getSubjectInfoListCount should work properly`() = runTest {
        coEvery {
            reactiveMongoTemplate.aggregate(any<Aggregation>(), any<String>(), SubjectInfoEntity::class.java)
        } answers {
            subjectInfoEntities.toFlux()
        }

        val actual = getSubjectInfoMongoAdapter.getSubjectInfoListCount(studyId)
        assertEquals(subjectInfoEntities.size.toLong(), actual)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getSubjectInfo should work properly`() = runTest {
        coEvery {
            subjectInfoRepository.findByStudyIdAndSubjectNumber(studyId, subjectNumber)
        } returns subjectInfoEntity.toMono()

        val actual = getSubjectInfoMongoAdapter.getSubjectInfo(studyId, subjectNumber)
        assertEquals(subjectInfoEntity.toDomain(), actual)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getSubjectInfoByUserId should work properly`() = runTest {
        coEvery {
            subjectInfoRepository.findByStudyIdAndSubjectId(studyId, userId)
        } returns subjectInfoEntity.toMono()

        val actual = getSubjectInfoMongoAdapter.getSubjectInfoBySubjectId(studyId, userId)
        assertEquals(subjectInfoEntity.toDomain(), actual)
    }
}
