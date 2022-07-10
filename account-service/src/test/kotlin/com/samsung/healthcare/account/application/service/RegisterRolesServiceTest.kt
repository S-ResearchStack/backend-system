package com.samsung.healthcare.account.application.service

import com.samsung.healthcare.account.application.port.output.AuthServicePort
import com.samsung.healthcare.account.domain.Role.TeamAdmin
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.kotlin.test.verifyError
import reactor.test.StepVerifier

internal class RegisterRolesServiceTest {

    private val authServicePort = mockk<AuthServicePort>()

    private val registerRolesService = RegisterRolesService(authServicePort)

    @Test
    fun `registerRoles should throw IllegalArgumentException when roles is empty`() {
        StepVerifier.create(
            registerRolesService.registerRoles(emptyList())
        ).verifyError<IllegalArgumentException>()
    }

    @Test
    fun `registerRoles should not emit event`() {
        every { authServicePort.createRoles(any()) } returns Mono.empty()

        StepVerifier.create(
            registerRolesService.registerRoles(listOf(TeamAdmin))
        ).verifyComplete()
    }

    // TODO define error case and then handle them
    @Test
    fun `registerRoles should throw exception when something is wrong`() {
        every { authServicePort.createRoles(any()) } returns Mono.error(Exception())

        StepVerifier.create(
            registerRolesService.registerRoles(emptyList())
        ).verifyError<Exception>()
    }
}
