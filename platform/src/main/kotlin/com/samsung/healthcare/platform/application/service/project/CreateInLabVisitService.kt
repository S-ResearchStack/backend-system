package com.samsung.healthcare.platform.application.service.project

import com.samsung.healthcare.account.domain.AccessInLabVisitAuthority
import com.samsung.healthcare.platform.application.authorize.Authorizer
import com.samsung.healthcare.platform.application.port.input.project.CreateInLabVisitCommand
import com.samsung.healthcare.platform.application.port.input.project.CreateInLabVisitUseCase
import com.samsung.healthcare.platform.application.port.output.project.InLabVisitOutputPort
import com.samsung.healthcare.platform.domain.project.InLabVisit
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.mono
import org.springframework.stereotype.Service

@Service
class CreateInLabVisitService(
    private val inLabVisitOutputPort: InLabVisitOutputPort,
) : CreateInLabVisitUseCase {
    override suspend fun createInLabVisit(projectId: String, command: CreateInLabVisitCommand): InLabVisit {
        return Authorizer.getAccount(AccessInLabVisitAuthority(projectId))
            .flatMap {
                mono {
                    inLabVisitOutputPort.create(
                        InLabVisit(
                            null,
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
