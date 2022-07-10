package com.samsung.healthcare.platform.adapter.web.project.healthdata

import com.samsung.healthcare.platform.adapter.web.context.ContextHolder.getFirebaseToken
import com.samsung.healthcare.platform.application.port.input.project.UpdateUserProfileLastSyncedTimeUseCase
import com.samsung.healthcare.platform.application.port.input.project.healthdata.SaveHealthDataUseCase
import com.samsung.healthcare.platform.domain.project.UserProfile.UserId
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.buildAndAwait

@Component
class HealthDataHandler(
    private val createHealthDataUseCase: SaveHealthDataUseCase,
    private val updateUserProfileLastSyncedTimeUseCase: UpdateUserProfileLastSyncedTimeUseCase,
) {
    /**
     * Handles requests to register [HealthData][com.samsung.healthcare.platform.domain.project.healthdata.HealthData].
     *
     * Updates the requesting user profile's [last synced time][com.samsung.healthcare.platform.domain.project.UserProfile.lastSyncedAt].
     *
     * @param req ServerRequest providing ths list of data to be registered as HealthData.
     * @return ServerResponse indicating that the request was accepted.
     */
    suspend fun createHealthData(req: ServerRequest): ServerResponse {
        val userId = UserId.from(getFirebaseToken().uid)
        updateUserProfileLastSyncedTimeUseCase.updateLastSyncedTime(userId)
        // TODO run async : fire and forget
        createHealthDataUseCase.saveHealthData(
            userId,
            req.awaitBody()
        )

        return ServerResponse.accepted().buildAndAwait()
    }
}
