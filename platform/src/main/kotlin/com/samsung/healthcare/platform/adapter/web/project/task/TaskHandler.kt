package com.samsung.healthcare.platform.adapter.web.project.task

import com.samsung.healthcare.platform.adapter.web.common.getEndTime
import com.samsung.healthcare.platform.adapter.web.common.getLastSyncTime
import com.samsung.healthcare.platform.adapter.web.common.getRevisionId
import com.samsung.healthcare.platform.adapter.web.common.getStartTime
import com.samsung.healthcare.platform.adapter.web.common.getTaskId
import com.samsung.healthcare.platform.application.port.input.project.task.CreateTaskUseCase
import com.samsung.healthcare.platform.application.port.input.project.task.GetTaskCommand
import com.samsung.healthcare.platform.application.port.input.project.task.GetTaskUseCase
import com.samsung.healthcare.platform.application.port.input.project.task.UpdateTaskUseCase
import com.samsung.healthcare.platform.domain.project.task.RevisionId
import kotlinx.coroutines.reactor.asFlux
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait

@Component
class TaskHandler(
    private val getTaskUseCase: GetTaskUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
) {
    suspend fun findByPeriod(req: ServerRequest): ServerResponse {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(
            getTaskUseCase.findByPeriod(
                GetTaskCommand(
                    startTime = req.getStartTime(),
                    endTime = req.getEndTime(),
                    lastSyncTime = req.getLastSyncTime(),
                    status = req.queryParam("status").orElse(null),
                )
            ).asFlux().collectList().awaitSingle()
        )
    }

    suspend fun findById(req: ServerRequest): ServerResponse {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(
            getTaskUseCase.findById(
                req.getTaskId(),
            ).asFlux().collectList().awaitSingle()
        )
    }

    suspend fun createTask(req: ServerRequest): ServerResponse =
        ServerResponse
            .ok()
            .bodyValue(createTaskUseCase.createTask())
            .awaitSingle()

    suspend fun updateTask(req: ServerRequest): ServerResponse {
        updateTaskUseCase.updateTask(
            req.getTaskId(),
            RevisionId.from(req.getRevisionId()),
            req.awaitBody()
        )
        return ServerResponse.noContent().buildAndAwait()
    }
}
