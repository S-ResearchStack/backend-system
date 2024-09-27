package researchstack.backend.application.service.study

import org.springframework.stereotype.Service
import researchstack.backend.application.exception.AlreadyExistsException
import researchstack.backend.application.exception.NotFoundException
import researchstack.backend.application.port.incoming.study.CreateStudyCommand
import researchstack.backend.application.port.incoming.study.CreateStudyResponse
import researchstack.backend.application.port.incoming.study.CreateStudyUseCase
import researchstack.backend.application.port.incoming.study.GetStudyUseCase
import researchstack.backend.application.port.outgoing.casbin.CreateStudyPolicyOutPort
import researchstack.backend.application.port.outgoing.casbin.GetRoleOutPort
import researchstack.backend.application.port.outgoing.investigator.AddInvestigatorStudyRelationOutPort
import researchstack.backend.application.port.outgoing.investigator.GetInvestigatorOutPort
import researchstack.backend.application.port.outgoing.study.CreateStudyOutPort
import researchstack.backend.application.port.outgoing.study.CreateSubjectNumberGeneratorOutPort
import researchstack.backend.application.port.outgoing.studydata.AddStudyDataInfoOutPort
import researchstack.backend.config.STUDY_DATA_ROOT_ID
import researchstack.backend.domain.investigator.InvestigatorStudyRelation
import researchstack.backend.domain.studydata.StudyDataFolder

@Service
class CreateStudyService(
    private val createSubjectNumberGeneratorOutPort: CreateSubjectNumberGeneratorOutPort,
    private val createStudyOutPort: CreateStudyOutPort,
    private val createStudyPolicyOutPort: CreateStudyPolicyOutPort,
    private val getStudyUseCase: GetStudyUseCase,
    private val addStudyDataInfoOutPort: AddStudyDataInfoOutPort,
    private val getInvestigatorOutPort: GetInvestigatorOutPort,
    private val getRoleOutPort: GetRoleOutPort,
    private val addInvestigatorStudyRelationOutPort: AddInvestigatorStudyRelationOutPort
) : CreateStudyUseCase {
    override suspend fun createStudy(
        investigatorId: String,
        createStudyCommand: CreateStudyCommand
    ): CreateStudyResponse {
        val study = createStudyCommand.toDomain()
        if (!study.participationCode.isNullOrBlank()) validateParticipationCode(study.participationCode)

        createSubjectNumberGeneratorOutPort.createSubjectNumberGenerator(createStudyCommand.id)
        val studyId = createStudyOutPort.createStudy(study)
        createStudyPolicyOutPort.createStudyPolicies(investigatorId, createStudyCommand.id)
        addStudyDataInfoOutPort.addStudyDataInfo(
            StudyDataFolder(
                name = createStudyCommand.id,
                studyId = createStudyCommand.id,
                parentId = STUDY_DATA_ROOT_ID
            )
        )
        val investigator = getInvestigatorOutPort.getInvestigator(investigatorId)
        val roles = getRoleOutPort.getRolesInStudy(investigatorId, studyId)
        if (roles.isNotEmpty()) {
            addInvestigatorStudyRelationOutPort.addRelation(
                InvestigatorStudyRelation(
                    email = investigator.email,
                    studyId = studyId,
                    role = roles.first()
                )
            )
        }
        return CreateStudyResponse(studyId)
    }

    suspend fun validateParticipationCode(participationCode: String) {
        try {
            getStudyUseCase.getStudyByParticipationCode(participationCode)
        } catch (e: NotFoundException) {
            return
        } catch (e: NoSuchElementException) {
            return
        }
        throw AlreadyExistsException("duplicate participation code($participationCode) exists")
    }
}
