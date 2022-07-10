package com.samsung.healthcare.platform.adapter.web.project.task

import com.samsung.healthcare.platform.adapter.web.context.ContextHolder
import com.samsung.healthcare.platform.application.port.input.project.UpdateUserProfileLastSyncedTimeUseCase
import com.samsung.healthcare.platform.application.port.input.project.task.UploadTaskResultUseCase
import com.samsung.healthcare.platform.domain.project.UserProfile
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.buildAndAwait

@Component
class TaskResultHandler(
    private val uploadTaskResultUseCase: UploadTaskResultUseCase,
    private val updateUserProfileLastSyncedTimeUseCase: UpdateUserProfileLastSyncedTimeUseCase,
) {
    suspend fun uploadTaskResults(req: ServerRequest): ServerResponse {
        val userId = UserProfile.UserId.from(ContextHolder.getFirebaseToken().uid)
        updateUserProfileLastSyncedTimeUseCase.updateLastSyncedTime(userId)
        uploadTaskResultUseCase.uploadResults(
            req.awaitBody()
        )
        return ServerResponse.ok().buildAndAwait()
    }
}
