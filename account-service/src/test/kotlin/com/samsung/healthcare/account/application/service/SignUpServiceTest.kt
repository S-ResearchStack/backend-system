package com.samsung.healthcare.account.application.service

import com.samsung.healthcare.account.POSITIVE_TEST
import com.samsung.healthcare.account.application.port.input.SignUpCommand
import com.samsung.healthcare.account.application.port.output.AuthServicePort
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.account.domain.Role
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyOrder
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

internal class SignUpServiceTest {
    private val authServicePort = mockk<AuthServicePort>()
    private val mailService = mockk<MailService>()
    private val registerRolesService = mockk<RegisterRolesService>()

    private val signUpService = SignUpService(authServicePort, mailService, registerRolesService)

    @Test
    @Tag(POSITIVE_TEST)
    fun `signUp should return properly when countUsers return 1`() {
        val email = Email("cubist@reserach-hub.test.com")
        val password = "pw"
        val profile: Map<String, Any> = emptyMap()
        val account = Account("id", email, listOf(Role.TeamAdmin))
        every { authServicePort.registerNewUser(email, password) } returns Mono.just(account)
        every { authServicePort.updateAccountProfile(account.id, profile) } returns Mono.just(emptyMap())
        every { authServicePort.countUsers() } returns Mono.just(1)
        every { authServicePort.assignRoles(account.id, any()) } returns Mono.empty()
        every { authServicePort.generateEmailVerificationToken(account.id, email) } returns Mono.just("test-token")
        every { mailService.sendVerificationMail(email, "test-token") } returns Mono.empty()
        every { registerRolesService.registerRoles(any()) } returns Mono.empty()

        StepVerifier.create(
            signUpService.signUp(SignUpCommand(email, password, profile))
        ).verifyComplete()

        verifyOrder {
            registerRolesService.registerRoles(any())
            authServicePort.assignRoles(account.id, any())
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `signUp should return properly when countUsers return more than 1`() {
        val email = Email("cubist@reserach-hub.test.com")
        val password = "pw"
        val profile: Map<String, Any> = emptyMap()
        val account = Account("id", email, listOf(Role.TeamAdmin))
        every { authServicePort.registerNewUser(email, password) } returns Mono.just(account)
        every { authServicePort.updateAccountProfile(account.id, profile) } returns Mono.just(emptyMap())
        every { authServicePort.countUsers() } returns Mono.just(2)
        every { authServicePort.generateEmailVerificationToken(account.id, email) } returns Mono.just("test-token")
        every { mailService.sendVerificationMail(email, "test-token") } returns Mono.empty()

        StepVerifier.create(
            signUpService.signUp(SignUpCommand(email, password, profile))
        ).verifyComplete()
    }
}
