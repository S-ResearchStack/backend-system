package com.samsung.healthcare.platform.application.service.project

import com.samsung.healthcare.platform.adapter.web.context.ContextHolder.getFirebaseToken
import com.samsung.healthcare.platform.application.exception.ForbiddenException
import com.samsung.healthcare.platform.application.port.input.CreateUserCommand
import com.samsung.healthcare.platform.application.port.input.UpdateUserCommand
import com.samsung.healthcare.platform.application.port.input.project.ExistUserProfileUseCase
import com.samsung.healthcare.platform.application.port.input.project.UpdateUserProfileLastSyncedTimeUseCase
import com.samsung.healthcare.platform.application.port.input.project.UserProfileInputPort
import com.samsung.healthcare.platform.application.port.output.project.UserProfileOutputPort
import com.samsung.healthcare.platform.domain.project.UserProfile
import org.springframework.stereotype.Service

@Service
class UserProfileService(
    private val userProfileOutputPort: UserProfileOutputPort,
) : UserProfileInputPort, UpdateUserProfileLastSyncedTimeUseCase, ExistUserProfileUseCase {

    /**
     * Creates a new user profile and registers said user.
     *
     * Prior to user profile creation, a validation check is completed using a Firebase token.
     *
     * @param command [CreateUserCommand] with request parameters.
     * @throws [ForbiddenException] if the uid associated with the Firebase token does not match the userId.
     */
    override suspend fun registerUser(command: CreateUserCommand) {
        if (command.userId != getFirebaseToken().uid)
            throw ForbiddenException("This operation can only be done by owner of token.")

        userProfileOutputPort.create(UserProfile.newUserProfile(command.userId, command.profile))
    }

    /**
     * Updates a profile of specific user.
     *
     * Prior to user profile creation, a validation check is completed using a Firebase token.
     *
     * @param requesstUserId [String] from API Path variable.
     * @param command [UpdateUserCommand] with request parameters.
     * @throws [ForbiddenException] if the uid associated with the Firebase token does not match the userId.
     */
    override suspend fun updateUser(requestedUserId: String, command: UpdateUserCommand) {
        if (requestedUserId != getFirebaseToken().uid)
            throw ForbiddenException("This operation can only be done by owner of token.")

        userProfileOutputPort.update(UserProfile.newUserProfile(requestedUserId, command.profile))
    }

    /**
     * Updates the last synced time of the user.
     *
     * Used to track updates to user-specific data, such as [HealthData][com.samsung.healthcare.platform.domain.project.healthdata.HealthData] or [TaskResult][com.samsung.healthcare.platform.domain.project.task.TaskResult]
     * instances associated with a UserProfile.
     *
     * @param userId UserId of the [UserProfile] to be updated.
     */
    override suspend fun updateLastSyncedTime(userId: UserProfile.UserId) {
        userProfileOutputPort.updateLastSyncedAt(userId)
    }

    /**
     * Checks whether the user is registered or not.
     *
     * @param userId UserId of the [UserProfile].
     * @return A [Boolean] value for the result.
     */
    override suspend fun existsByUserId(userId: UserProfile.UserId): Boolean =
        userProfileOutputPort.existsByUserId(userId)
}
