package researchstack.backend.application.service.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.factory.Mappers
import researchstack.backend.application.port.incoming.study.CreateStudyCommand
import researchstack.backend.application.port.incoming.study.ParticipateInStudyCommand.EligibilityTestResultCommand
import researchstack.backend.application.port.incoming.study.ParticipateInStudyCommand.SignedInformedConsentCommand
import researchstack.backend.application.port.incoming.study.UpdateStudyCommand
import researchstack.backend.domain.study.EligibilityTestResult
import researchstack.backend.domain.study.SignedInformedConsent
import researchstack.backend.domain.study.Study

@Mapper(componentModel = "spring")
abstract class StudyMapper {
    companion object {
        val INSTANCE: StudyMapper = Mappers.getMapper(StudyMapper::class.java)
    }

    @Mappings(
        Mapping(target = "studyInfo", source = "."),
        Mapping(target = "irbInfo", source = ".")
    )
    abstract fun toDomain(command: CreateStudyCommand): Study

    abstract fun mapStudyInfo(command: CreateStudyCommand): Study.StudyInfo

    @Mappings(
        Mapping(target = "decidedAt", source = "irbDecidedAt"),
        Mapping(target = "expiredAt", source = "irbExpiredAt")
    )
    abstract fun mapIrbInfo(command: CreateStudyCommand): Study.IrbInfo

    @Mappings(
        Mapping(target = "studyInfo", source = "command"),
        Mapping(target = "irbInfo", source = "command"),
        Mapping(target = "id", source = "studyId")
    )
    abstract fun toDomain(command: UpdateStudyCommand, studyId: String): Study

    abstract fun mapStudyInfo(command: UpdateStudyCommand): Study.StudyInfo

    @Mappings(
        Mapping(target = "decidedAt", source = "irbDecidedAt"),
        Mapping(target = "expiredAt", source = "irbExpiredAt")
    )
    abstract fun mapIrbInfo(command: UpdateStudyCommand): Study.IrbInfo

    abstract fun toDomain(command: EligibilityTestResultCommand): EligibilityTestResult

    abstract fun toDomain(command: SignedInformedConsentCommand): SignedInformedConsent
}
