package researchstack.backend.adapter.outgoing.mongo.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import researchstack.backend.adapter.outgoing.mongo.entity.study.SubjectStudyRelationEntity
import researchstack.backend.domain.study.EligibilityTestResult

@Mapper
abstract class EligibilityTestResultMapper {
    abstract fun toEntity(eligibilityTestResult: EligibilityTestResult): SubjectStudyRelationEntity.EligibilityTestResult
}

private val converter = Mappers.getMapper(EligibilityTestResultMapper::class.java)

fun EligibilityTestResult.toEntity(): SubjectStudyRelationEntity.EligibilityTestResult = converter.toEntity(this)
