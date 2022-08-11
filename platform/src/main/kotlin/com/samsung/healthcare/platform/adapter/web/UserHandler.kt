package com.samsung.healthcare.platform.adapter.web

import com.samsung.healthcare.platform.application.port.input.UserInputPort
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyAndAwait

@Component
@Suppress("UnusedPrivateMember")
class UserHandler(
    private val inputPort: UserInputPort,
) {
    suspend fun getUsers(req: ServerRequest): ServerResponse =
        ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyAndAwait(inputPort.getUsers())
}
