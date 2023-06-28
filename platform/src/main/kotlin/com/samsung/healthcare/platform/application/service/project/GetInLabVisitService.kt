package com.samsung.healthcare.platform.application.service.project

import com.samsung.healthcare.account.domain.AccessInLabVisitAuthority
import com.samsung.healthcare.platform.application.authorize.Authorizer
import com.samsung.healthcare.platform.application.exception.NotFoundException
import com.samsung.healthcare.platform.application.port.input.project.GetInLabVisitUseCase
import com.samsung.healthcare.platform.application.port.output.project.InLabVisitOutputPort
import com.samsung.healthcare.platform.domain.project.InLabVisit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.mono
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class GetInLabVisitService(
    private val inLabVisitOutputPort: InLabVisitOutputPort,
) : GetInLabVisitUseCase {
    override suspend fun getInLabVisitById(projectId: String, inLabVisitId: Int): InLabVisit {
        return Authorizer.getAccount(AccessInLabVisitAuthority(projectId))
            .flatMap {
                mono {
                    inLabVisitOutputPort.findById(inLabVisitId)
                }
            }
            .switchIfEmpty(Mono.error(NotFoundException("no data found for given id")))
            .awaitSingle()
    }

    override suspend fun getInLabVisits(projectId: String): Flow<InLabVisit> {
        return Authorizer.getAccount(AccessInLabVisitAuthority(projectId))
            .flatMap {
                mono {
                    inLabVisitOutputPort.findAll()
                }
            }.awaitSingle()
    }
}
