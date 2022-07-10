package com.samsung.healthcare.account.application.service

import com.samsung.healthcare.account.application.port.input.UpdateAccountProfileCommand
import com.samsung.healthcare.account.application.port.input.UpdateAccountProfileUseCase
import com.samsung.healthcare.account.application.port.output.AuthServicePort
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UpdateAccountProfileService(
    private val authServicePort: AuthServicePort
) : UpdateAccountProfileUseCase {
    // TODO do we need to authorize?
    override fun updateProfile(updateAccountProfileCommand: UpdateAccountProfileCommand): Mono<Map<String, Any>> =
        authServicePort.updateAccountProfile(
            updateAccountProfileCommand.accountId,
            updateAccountProfileCommand.profile
        )
}
