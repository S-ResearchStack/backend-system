package com.samsung.healthcare.account.application.service

import com.samsung.healthcare.account.NEGATIVE_TEST
import com.samsung.healthcare.account.POSITIVE_TEST
import com.samsung.healthcare.account.application.port.input.CreateProjectRoleRequest
import com.samsung.healthcare.account.application.port.output.AuthServicePort
import com.samsung.healthcare.account.domain.Role.TeamAdmin
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.kotlin.test.verifyError
import reactor.test.StepVerifier

internal class RegisterRolesServiceTest {

    private val authServicePort = mockk<AuthServicePort>()

    private val registerRolesService = RegisterRolesService(authServicePort)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `registerRoles should throw IllegalArgumentException when roles is empty`() {
        StepVerifier.create(
            registerRolesService.registerRoles(emptyList())
        ).verifyError<IllegalArgumentException>()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `registerRoles should not emit event`() {
        every { authServicePort.createRoles(any()) } returns Mono.empty()

        StepVerifier.create(
            registerRolesService.registerRoles(listOf(TeamAdmin))
        ).verifyComplete()
    }

    // TODO define error case and then handle them
    @Test
    @Tag(NEGATIVE_TEST)
    fun `registerRoles should throw exception when something is wrong`() {
        every { authServicePort.createRoles(any()) } returns Mono.error(Exception())

        StepVerifier.create(
            registerRolesService.registerRoles(emptyList())
        ).verifyError<Exception>()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `createProjectRoles should not emit event`() {
        every { authServicePort.createRoles(any()) } returns Mono.empty()
        every { authServicePort.assignRoles(any<String>(), any()) } returns Mono.empty()

        StepVerifier.create(
            registerRolesService.createProjectRoles(CreateProjectRoleRequest("account-id", "project-id"))
        ).verifyComplete()
    }
}
