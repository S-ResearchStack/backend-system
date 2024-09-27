package researchstack.backend.application.service.study

import org.casbin.jcasbin.main.Enforcer
import org.springframework.stereotype.Service
import researchstack.backend.adapter.incoming.mapper.study.toResponse
import researchstack.backend.adapter.outgoing.casbin.RoleConstant
import researchstack.backend.application.port.incoming.study.CreateStudyCommandEnum
import researchstack.backend.application.port.incoming.study.GetStudyUseCase
import researchstack.backend.application.port.incoming.study.StudyResponse
import researchstack.backend.application.port.outgoing.study.GetStudyOutPort

@Service
class GetStudyService(
    private val enforcer: Enforcer,
    private val getStudyOutPort: GetStudyOutPort
) : GetStudyUseCase {
    override suspend fun getStudy(studyId: String): StudyResponse {
        return getStudyOutPort.getStudy(studyId).toResponse()
    }

    override suspend fun getStudyByParticipationCode(participationCode: String): StudyResponse {
        return getStudyOutPort.getStudyByParticipationCode(participationCode).toResponse()
    }

    override suspend fun getStudyList(): List<StudyResponse> {
        return getStudyOutPort.getStudyList().map { it.toResponse() }
    }

    override suspend fun getStudyListByUser(investigatorId: String): List<StudyResponse> {
        return getStudyOutPort.getStudyList()
            .filter { enforcer.enforce(investigatorId, it.id, RoleConstant.RESOURCE_STUDY, RoleConstant.ACTION_READ) }
            .map { it.toResponse() }
    }

    override suspend fun getPublicStudyList(): List<StudyResponse> {
        return getStudyOutPort.getPublicStudyList().map { it.toResponse() }
    }

    override suspend fun getParticipatedStudyList(subjectId: String): List<StudyResponse> {
        return getStudyOutPort.getParticipatedStudyList(subjectId).map { it.toResponse() }
    }

    override suspend fun getCreateStudyCommand(): CreateStudyCommandEnum {
        return CreateStudyCommandEnum()
    }
}
