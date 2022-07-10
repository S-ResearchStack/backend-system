package com.samsung.healthcare.account.application.accesscontrol

import com.samsung.healthcare.account.application.context.ContextHolder
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.AssignRoleAuthority
import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.account.domain.Role
import com.samsung.healthcare.account.domain.Role.ProjectRole.ProjectOwner
import com.samsung.healthcare.account.domain.Role.ProjectRole.Researcher
import org.junit.jupiter.api.Test
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory
import reactor.core.publisher.Mono
import reactor.kotlin.test.verifyError
import reactor.test.StepVerifier

interface Target {
    fun test(roles: List<Role>): Mono<Void>
}

open class TestTarget : Target {
    @Requires([AssignRoleAuthority::class])
    override fun test(roles: List<Role>) = Mono.empty<Void>()
}

internal class AccessControlAspectTest {

    private val testTarget = TestTarget()

    val target = AspectJProxyFactory(testTarget).apply {
        addAspect(AccessControlAspect())
    }.getProxy<Target>()

    @Test
    fun `should throw IllegalAccessException when account does not have a owner roles for project`() {

        val projectId = "project-id"
        StepVerifier.create(
            withAccountContext(
                target.test(
                    listOf(Researcher(projectId))
                ),
                testAccount(Researcher(projectId))
            )
        ).verifyError<IllegalAccessException>()
    }

    @Test
    fun `should throw IllegalAccessException when account does have project owner roles and assign researcher`() {
        val projectId = "project-id"
        val roles = listOf(Researcher(projectId))

        StepVerifier.create(
            withAccountContext(
                target.test(roles),
                testAccount(ProjectOwner(projectId))
            )
        ).verifyComplete()
    }

    @Test
    fun `should throw IllegalAccessException when context has no account`() {
        StepVerifier.create(
            target.test(emptyList())
        ).verifyError<IllegalAccessException>()
    }

    private fun withAccountContext(mono: Mono<*>, account: Account) =
        ContextHolder.setAccount(mono, account)

    private fun testAccount(vararg roles: Role) =
        Account("account", Email("test@test.com"), roles.asList())
}
