package com.samsung.healthcare.account.application.service

import com.samsung.healthcare.account.application.port.input.ResetPasswordCommand
import com.samsung.healthcare.account.application.port.output.AuthServicePort
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.kotlin.test.verifyError
import reactor.test.StepVerifier

internal class ResetPasswordServiceTest {
    private val authServicePort = mockk<AuthServicePort>()

    private val resetPasswordService = ResetPasswordService(authServicePort)

    @Test
    fun `resetPasswordService should not emit event`() {
        every { authServicePort.resetPassword(any(), any()) } returns Mono.empty()
        StepVerifier.create(
            resetPasswordService.resetPassword(resetPasswordCommand())
        ).verifyComplete()
    }

    // TODO define error case and then handle them
    @Test
    fun `registerRoles should throw exception when something is wrong`() {
        every { authServicePort.resetPassword(any(), any()) } returns Mono.error(Exception())

        StepVerifier.create(
            resetPasswordService.resetPassword(resetPasswordCommand())
        ).verifyError<Exception>()
    }

    private fun resetPasswordCommand() =
        ResetPasswordCommand("reset-token", "new-pw")
}
