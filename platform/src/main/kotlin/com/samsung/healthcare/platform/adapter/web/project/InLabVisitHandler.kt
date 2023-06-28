package com.samsung.healthcare.platform.adapter.web.project

import com.samsung.healthcare.platform.adapter.web.common.getInLabVisitId
import com.samsung.healthcare.platform.adapter.web.common.getProjectId
import com.samsung.healthcare.platform.application.port.input.project.CreateInLabVisitUseCase
import com.samsung.healthcare.platform.application.port.input.project.GetInLabVisitUseCase
import com.samsung.healthcare.platform.application.port.input.project.UpdateInLabVisitUseCase
import kotlinx.coroutines.reactor.asFlux
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import java.net.URI

@Component
class InLabVisitHandler(
    private val createInLabVisitUseCase: CreateInLabVisitUseCase,
    private val updateInLabVisitUseCase: UpdateInLabVisitUseCase,
    private val getInLabVisitUseCase: GetInLabVisitUseCase,
) {
    suspend fun createInLabVisit(req: ServerRequest): ServerResponse =
        createInLabVisitUseCase.createInLabVisit(req.getProjectId(), req.awaitBody()).let {
            ServerResponse.created(URI.create("/api/projects/${req.getProjectId()}/in-lab-visits/${it.id}"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(it)
                .awaitSingle()
        }

    suspend fun updateInLabVisit(req: ServerRequest): ServerResponse =
        ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(
            updateInLabVisitUseCase.updateInLabVisit(req.getProjectId(), req.getInLabVisitId(), req.awaitBody())
        )

    suspend fun getInLabVisit(req: ServerRequest): ServerResponse =
        ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(
            getInLabVisitUseCase.getInLabVisitById(req.getProjectId(), req.getInLabVisitId())
        )

    suspend fun listInLabVisits(req: ServerRequest): ServerResponse =
        ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(
            getInLabVisitUseCase.getInLabVisits(req.getProjectId())
                .asFlux().collectList().awaitSingle()
        )
}
