package researchstack.backend.application.service.subject

import org.springframework.stereotype.Service
import researchstack.backend.adapter.exception.ExceptionMessage
import researchstack.backend.application.port.incoming.subject.RegisterSubjectCommand
import researchstack.backend.application.port.incoming.subject.RegisterSubjectUseCase
import researchstack.backend.application.port.outgoing.casbin.AddRoleOutPort
import researchstack.backend.application.port.outgoing.subject.RegisterSubjectOutPort
import researchstack.backend.util.validateContext

@Service
class RegisterSubjectService(
    private val registerSubjectOutPort: RegisterSubjectOutPort,
    private val addRoleOutPort: AddRoleOutPort
) : RegisterSubjectUseCase {
    override suspend fun registerSubject(command: RegisterSubjectCommand) {
        validateContext(command.subjectId, ExceptionMessage.EMPTY_SUBJECT_ID)
        val subjectProfile = command.toDomain()
        registerSubjectOutPort.registerSubject(subjectProfile)
        addRoleOutPort.addRolesForMyself(subjectProfile.subjectId.value)
    }
}
