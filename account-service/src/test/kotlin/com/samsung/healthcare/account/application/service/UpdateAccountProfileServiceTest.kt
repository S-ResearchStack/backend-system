package com.samsung.healthcare.account.application.service

import com.samsung.healthcare.account.POSITIVE_TEST
import com.samsung.healthcare.account.application.port.input.UpdateAccountProfileCommand
import com.samsung.healthcare.account.application.port.output.AuthServicePort
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

internal class UpdateAccountProfileServiceTest {

    private val authServicePort = mockk<AuthServicePort>()

    private val updateAccountProfileService = UpdateAccountProfileService(authServicePort)

    @Test
    @Tag(POSITIVE_TEST)
    fun `should not emit event`() {
        every { authServicePort.updateAccountProfile(any(), any()) } returns Mono.empty()

        StepVerifier.create(
            updateAccountProfileService.updateProfile(updateAccountProfileCommand())
        ).verifyComplete()
    }

    private fun updateAccountProfileCommand() =
        UpdateAccountProfileCommand("account-id", emptyMap())
}
