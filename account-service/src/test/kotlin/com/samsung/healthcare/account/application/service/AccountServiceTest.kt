package com.samsung.healthcare.account.application.service

import com.samsung.healthcare.account.NEGATIVE_TEST
import com.samsung.healthcare.account.POSITIVE_TEST
import com.samsung.healthcare.account.application.exception.AlreadyExistedEmailException
import com.samsung.healthcare.account.application.exception.UnknownRoleException
import com.samsung.healthcare.account.application.port.output.AuthServicePort
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.account.domain.Role.ProjectRole.ResearchAssistant
import com.samsung.healthcare.account.domain.Role.TeamAdmin
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Tag
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
    @Tag(POSITIVE_TEST)
    fun `assignRoles should not emit event`() {
        every { authServicePort.assignRoles(accountId, any()) } returns Mono.empty()
        StepVerifier.create(
            accountService.assignRoles(accountId, listOf(TeamAdmin))
        ).verifyComplete()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `assignRoles should throw illegal argument exception when roles is empty`() {
        StepVerifier.create(
            accountService.assignRoles(accountId, emptyList())
        ).verifyError<IllegalArgumentException>()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `inviteUser should send invitation email`() {
        val account = Account(UUID.randomUUID().toString(), email, listOf())
        val verificationToken = "verification-token"
        val resetToken = UUID.randomUUID().toString()
        every { authServicePort.registerNewUser(email, any()) } returns Mono.just(account)
        every {
            authServicePort.generateEmailVerificationToken(account.id, account.email)
        } returns Mono.just(verificationToken)
        every { authServicePort.verifyEmail(verificationToken) } returns Mono.empty()
        every { authServicePort.generateResetToken(account.id) } returns Mono.just(resetToken)
        every { authServicePort.assignRoles(account.id, any()) } returns Mono.empty()
        every { mailService.sendInvitationMail(email, resetToken) } returns Mono.empty()

        StepVerifier.create(
            accountService.inviteUser(email, listOf(TeamAdmin))
        ).verifyComplete()

        verify { mailService.sendInvitationMail(email, any()) }
    }

    @Test
    @Tag(POSITIVE_TEST)
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
    @Tag(NEGATIVE_TEST)
    fun `removeRolesFromAccount should throw illegal argument exception when roles is empty`() {
        StepVerifier.create(
            accountService.removeRolesFromAccount(accountId, emptyList())
        ).verifyError<IllegalArgumentException>()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `removeRolesFromAccount should not emit event`() {
        every { authServicePort.removeRolesFromAccount(any(), any()) } returns Mono.empty()
        StepVerifier.create(
            accountService.removeRolesFromAccount(accountId, listOf(TeamAdmin))
        ).verifyComplete()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `assignRoles should throw Unknown exception when roles does not exist`() {
        val accountId = UUID.randomUUID().toString()
        every { authServicePort.assignRoles(accountId, any()) } returns Mono.error(UnknownRoleException())

        StepVerifier.create(
            accountService.assignRoles(accountId, listOf(ResearchAssistant("1")))
        ).verifyError<UnknownRoleException>()
    }
}
