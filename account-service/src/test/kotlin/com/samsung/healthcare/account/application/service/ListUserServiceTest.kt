package com.samsung.healthcare.account.application.service

import com.samsung.healthcare.account.POSITIVE_TEST
import com.samsung.healthcare.account.application.port.output.AuthServicePort
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

internal class ListUserServiceTest {
    private val authServicePort = mockk<AuthServicePort>()

    private val listUserService = ListUserService(authServicePort)

    @Test
    @Tag(POSITIVE_TEST)
    fun `listAllUsers should not emit event`() {
        every { authServicePort.listUsers() } returns Mono.empty()
        StepVerifier.create(
            listUserService.listAllUsers()
        ).verifyComplete()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `usersOfProject should not emit event`() {
        val projectId = "project-x"
        every { authServicePort.retrieveUsersAssociatedWithRoles(any()) } returns Mono.empty()
        StepVerifier.create(
            listUserService.usersOfProject(projectId)
        ).verifyComplete()
    }
}
