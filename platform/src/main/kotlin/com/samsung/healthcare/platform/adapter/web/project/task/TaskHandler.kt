package com.samsung.healthcare.platform.adapter.web.project.task

import com.samsung.healthcare.platform.adapter.web.common.getEndTime
import com.samsung.healthcare.platform.adapter.web.common.getLastSyncTime
import com.samsung.healthcare.platform.adapter.web.common.getProjectId
import com.samsung.healthcare.platform.adapter.web.common.getRevisionId
import com.samsung.healthcare.platform.adapter.web.common.getStartTime
import com.samsung.healthcare.platform.adapter.web.common.getTaskId
import com.samsung.healthcare.platform.adapter.web.context.ContextHolder
import com.samsung.healthcare.platform.application.exception.ForbiddenException
import com.samsung.healthcare.platform.application.port.input.project.ExistUserProfileUseCase
import com.samsung.healthcare.platform.application.port.input.project.task.CreateTaskUseCase
import com.samsung.healthcare.platform.application.port.input.project.task.GetTaskCommand
import com.samsung.healthcare.platform.application.port.input.project.task.GetTaskUseCase
import com.samsung.healthcare.platform.application.port.input.project.task.UpdateTaskUseCase
import com.samsung.healthcare.platform.domain.project.UserProfile.UserId
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
import java.net.URI

@Component
class TaskHandler(
    private val getTaskUseCase: GetTaskUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val existUserProfileUseCase: ExistUserProfileUseCase,
) {
    suspend fun findByPeriod(req: ServerRequest): ServerResponse {
        val idToken: String? = req.headers().firstHeader("id-token")
        val command = GetTaskCommand(
            startTime = req.getStartTime(),
            endTime = req.getEndTime(),
            lastSyncTime = req.getLastSyncTime(),
            status = req.queryParam("status").orElse(null),
        )
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(
            if (idToken.isNullOrEmpty()) getTaskUseCase.findByPeriodFromResearcher(req.getProjectId(), command)
            else {
                val userId = UserId.from(ContextHolder.getFirebaseToken().uid)
                if (!existUserProfileUseCase.existsByUserId(userId)) {
                    throw ForbiddenException("This user(${userId.value}) is not registered on this project")
                }
                getTaskUseCase.findByPeriodFromParticipant(command)
            }
                .asFlux().collectList().awaitSingle()
        )
    }

    suspend fun findById(req: ServerRequest): ServerResponse {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(
            getTaskUseCase.findById(
                req.getProjectId(),
                req.getTaskId(),
            ).asFlux().collectList().awaitSingle()
        )
    }

    suspend fun createTask(req: ServerRequest): ServerResponse {
        val task = createTaskUseCase.createTask(req.getProjectId())
        return ServerResponse.created(URI.create("/api/projects/${req.getProjectId()}/tasks/${task.id}"))
            .bodyValue(task)
            .awaitSingle()
    }

    suspend fun updateTask(req: ServerRequest): ServerResponse {
        updateTaskUseCase.updateTask(
            req.getProjectId(),
            req.getTaskId(),
            RevisionId.from(req.getRevisionId()),
            req.awaitBody()
        )
        return ServerResponse.noContent().buildAndAwait()
    }
}
