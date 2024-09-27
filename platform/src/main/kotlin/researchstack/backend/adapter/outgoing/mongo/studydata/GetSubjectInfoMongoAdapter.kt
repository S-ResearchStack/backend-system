package researchstack.backend.adapter.outgoing.mongo.studydata

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.MatchOperation
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation
import org.springframework.data.mongodb.core.aggregation.SortOperation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Component
import researchstack.backend.adapter.outgoing.mongo.entity.studydata.SubjectInfoEntity
import researchstack.backend.adapter.outgoing.mongo.mapper.toDomain
import researchstack.backend.adapter.outgoing.mongo.repository.SubjectInfoRepository
import researchstack.backend.application.port.outgoing.studydata.GetSubjectInfoOutPort
import researchstack.backend.domain.subject.SubjectInfo
import researchstack.backend.enums.SubjectStatus
import kotlin.reflect.full.declaredMemberProperties

@Component
class GetSubjectInfoMongoAdapter(
    private val subjectInfoRepository: SubjectInfoRepository,
    private val reactiveMongoTemplate: ReactiveMongoTemplate
) : GetSubjectInfoOutPort {
    override suspend fun getSubjectInfoList(studyId: String, page: Long?, size: Long?): List<SubjectInfo> {
        val matchStage = getMatchStage(studyId)
        val projectStage = getProjectStage()
        val sortStage = getSortStage()

        val aggregation = if (size != null && size > 0) {
            Aggregation.newAggregation(
                matchStage,
                projectStage,
                sortStage,
                Aggregation.skip((page ?: 0) * size),
                Aggregation.limit(size)
            )
        } else {
            Aggregation.newAggregation(
                matchStage,
                projectStage,
                sortStage
            )
        }

        return reactiveMongoTemplate
            .aggregate(aggregation, "subjectInfo", SubjectInfoEntity::class.java)
            .collectList()
            .awaitSingle()
            .map { it.toDomain() }
    }

    override suspend fun getSubjectInfoListCount(studyId: String): Long {
        val matchStage = getMatchStage(studyId)
        val projectStage = getProjectStage()
        val sortStage = getSortStage()
        val aggregation = Aggregation.newAggregation(
            matchStage,
            projectStage,
            sortStage
        )

        return reactiveMongoTemplate
            .aggregate(aggregation, "subjectInfo", SubjectInfoEntity::class.java)
            .count()
            .awaitSingle()
    }

    override suspend fun getSubjectInfo(studyId: String, subjectNumber: String): SubjectInfo {
        return subjectInfoRepository.findByStudyIdAndSubjectNumber(
            studyId = studyId,
            subjectNumber = subjectNumber
        )
            .awaitSingle()
            .toDomain()
    }

    override suspend fun getSubjectInfoBySubjectId(studyId: String, subjectId: String): SubjectInfo {
        return subjectInfoRepository.findByStudyIdAndSubjectId(
            studyId = studyId,
            subjectId = subjectId
        )
            .awaitSingle()
            .toDomain()
    }

    private suspend fun getMatchStage(studyId: String): MatchOperation =
        Aggregation.match(
            Criteria
                .where("studyId")
                .`is`(studyId)
                .and("status")
                .ne(SubjectStatus.HIDDEN.name)
        )

    private suspend fun getProjectStage(): ProjectionOperation {
        val sortBy = "subjectNumber"

        var aggregation = Aggregation.project()
        SubjectInfoEntity::class.declaredMemberProperties.forEach {
            if (it.name == "id") return@forEach
            aggregation = aggregation.and(it.name).`as`(it.name)
        }
        return aggregation
            .andExpression("{\$toInt: '\$$sortBy'}")
            .`as`("numericField")
    }

    private suspend fun getSortStage(direction: Direction = Direction.ASC): SortOperation =
        Aggregation.sort(Sort.by(direction, "numericField"))
}
