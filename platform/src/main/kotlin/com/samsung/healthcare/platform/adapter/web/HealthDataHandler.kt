package com.samsung.healthcare.platform.adapter.web

import com.samsung.healthcare.platform.adapter.web.context.ContextHolder.getFirebaseToken
import com.samsung.healthcare.platform.application.port.input.GetHealthDataCommand
import com.samsung.healthcare.platform.application.port.input.GetHealthDataQuery
import com.samsung.healthcare.platform.application.port.input.SaveHealthDataUseCase
import com.samsung.healthcare.platform.domain.User.UserId
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait

@Component
class HealthDataHandler(
    private val createHealthDataUseCase: SaveHealthDataUseCase,
    private val getHealthDataQuery: GetHealthDataQuery,
) {
    suspend fun createHealthData(req: ServerRequest): ServerResponse {
        // TODO run async : fire and forget
        createHealthDataUseCase.saveHealthData(
            UserId.from(getFirebaseToken().uid),
            req.awaitBody()
        )

        return ServerResponse.accepted().buildAndAwait()
    }

    suspend fun findByPeriod(req: ServerRequest): ServerResponse {

        val healthData = getHealthDataQuery.findByPeriod(
            GetHealthDataCommand(
                req.getTypes(),
                req.getUsers(),
                req.getStartDate(),
                req.getEndDate()
            )
        )

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyAndAwait(healthData)
    }
}
