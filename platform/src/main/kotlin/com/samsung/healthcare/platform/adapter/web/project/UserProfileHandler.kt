package com.samsung.healthcare.platform.adapter.web.project

import com.samsung.healthcare.platform.adapter.web.common.getProjectId
import com.samsung.healthcare.platform.adapter.web.common.getUserId
import com.samsung.healthcare.platform.adapter.web.context.ContextHolder
import com.samsung.healthcare.platform.application.exception.ForbiddenException
import com.samsung.healthcare.platform.application.port.input.CreateUserCommand
import com.samsung.healthcare.platform.application.port.input.UpdateUserCommand
import com.samsung.healthcare.platform.application.port.input.project.ExistUserProfileUseCase
import com.samsung.healthcare.platform.application.port.input.project.UserProfileInputPort
import com.samsung.healthcare.platform.domain.project.UserProfile
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.buildAndAwait
import java.net.URI

@Component
class UserProfileHandler(
    private val inputPort: UserProfileInputPort,
    private val existUserProfileUseCase: ExistUserProfileUseCase,
) {

    /**
     * Handles requests to register a new [UserProfile][com.samsung.healthcare.platform.domain.project.UserProfile].
     *
     * @param req ServerRequest providing [CreateUserCommand]
     * @return ServerResponse indicating that the UserProfile was successfully created.
     */
    suspend fun registerUser(req: ServerRequest): ServerResponse {
        val createUserCommand = req.awaitBody<CreateUserCommand>()
        inputPort.registerUser(createUserCommand)

        return ServerResponse.created(URI.create("/api/projects/${req.getProjectId()}/users")).buildAndAwait()
    }

    /**
     * Handles requests to update a [UserProfile][com.samsung.healthcare.platform.domain.project.UserProfile].
     *
     * @param req ServerRequest providing [UpdateUserCommand]
     * @return ServerResponse indicating that the UserProfile was successfully updated.
     */
    suspend fun updateUser(req: ServerRequest): ServerResponse {
        val command = req.awaitBody<UpdateUserCommand>()
        inputPort.updateUser(req.getUserId(), command)

        return ServerResponse.ok().buildAndAwait()
    }

    suspend fun exists(req: ServerRequest): ServerResponse {
        val userId = UserProfile.UserId.from(ContextHolder.getFirebaseToken().uid)

        if (!existUserProfileUseCase.existsByUserId(userId)) {
            throw ForbiddenException("Not registered user on this project")
        }

        return ServerResponse.ok().buildAndAwait()
    }
}
