package com.samsung.healthcare.account.application.service

import com.samsung.healthcare.account.application.exception.AlreadyExistedEmailException
import com.samsung.healthcare.account.application.port.output.AuthServicePort
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.account.domain.Role.TeamAdmin
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.kotlin.test.verifyError
import reactor.test.StepVerifier
import java.util.UUID

internal class AccountServiceTest {

    private val authServicePort = mockk<AuthServicePort>()

    private val mailService = mockk<MailService>()

    private val accountService = AccountService(authServicePort, mailService)

    private val accountId = "account-id"

    private val email = Email("test@account-service-test.com")

    @Test
    fun `assignRoles should not emit event`() {
        every { authServicePort.assignRoles(accountId, any()) } returns Mono.empty()
        StepVerifier.create(
            accountService.assignRoles(accountId, listOf(TeamAdmin))
        ).verifyComplete()
    }

    @Test
    fun `assignRoles should throw illegal argument exception when roles is empty`() {
        StepVerifier.create(
            accountService.assignRoles(accountId, emptyList())
        ).verifyError<IllegalArgumentException>()
    }

    @Test
    fun `inviteUser should send invitation email`() {
        val account = Account(UUID.randomUUID().toString(), email, listOf())
        every { authServicePort.registerNewUser(any(), any()) } returns Mono.just(account)
        every { authServicePort.generateResetToken(any()) } returns Mono.just(UUID.randomUUID().toString())
        every { authServicePort.assignRoles(account.id, any()) } returns Mono.empty()
        every { mailService.sendMail(email, any()) } returns Mono.empty()

        StepVerifier.create(
            accountService.inviteUser(email, listOf(TeamAdmin))
        ).verifyComplete()

        verify { mailService.sendMail(email, any()) }
    }

    @Test
    fun `inviteUser should assign roles to when email is already registered`() {
        every { authServicePort.registerNewUser(any(), any()) } returns Mono.error(
            AlreadyExistedEmailException()
        )

        every { authServicePort.assignRoles(email, listOf(TeamAdmin)) } returns Mono.empty()

        StepVerifier.create(
            accountService.inviteUser(email, listOf(TeamAdmin))
        ).verifyComplete()

        verify { authServicePort.assignRoles(email, listOf(TeamAdmin)) }
    }

    @Test
    fun `removeRolesFromAccount should throw illegal argument exception when roles is empty`() {
        StepVerifier.create(
            accountService.removeRolesFromAccount(accountId, emptyList())
        ).verifyError<IllegalArgumentException>()
    }

    @Test
    fun `removeRolesFromAccount should not emit event`() {
        every { authServicePort.removeRolesFromAccount(any(), any()) } returns Mono.empty()
        StepVerifier.create(
            accountService.removeRolesFromAccount(accountId, listOf(TeamAdmin))
        ).verifyComplete()
    }
}
