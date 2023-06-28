package com.samsung.healthcare.platform.application.service.project

import com.samsung.healthcare.account.domain.AccessInLabVisitAuthority
import com.samsung.healthcare.platform.application.authorize.Authorizer
import com.samsung.healthcare.platform.application.port.input.project.UpdateInLabVisitCommand
import com.samsung.healthcare.platform.application.port.input.project.UpdateInLabVisitUseCase
import com.samsung.healthcare.platform.application.port.output.project.InLabVisitOutputPort
import com.samsung.healthcare.platform.domain.project.InLabVisit
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.mono
import org.springframework.stereotype.Service

@Service
class UpdateInLabVisitService(
    private val inLabVisitOutputPort: InLabVisitOutputPort,
) : UpdateInLabVisitUseCase {
    override suspend fun updateInLabVisit(
        projectId: String,
        inLabVisitId: Int,
        command: UpdateInLabVisitCommand
    ): InLabVisit {
        return Authorizer.getAccount(AccessInLabVisitAuthority(projectId))
            .flatMap {
                mono {
                    inLabVisitOutputPort.update(
                        InLabVisit(
                            inLabVisitId,
                            command.userId,
                            command.checkedInBy,
                            command.startTime,
                            command.endTime,
                            command.notes,
                            null,
                        )
                    )
                }
            }.awaitSingle()
    }
}
