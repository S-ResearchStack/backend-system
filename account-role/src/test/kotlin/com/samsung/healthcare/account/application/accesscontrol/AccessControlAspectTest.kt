package com.samsung.healthcare.account.application.accesscontrol

import com.samsung.healthcare.account.NEGATIVE_TEST
import com.samsung.healthcare.account.POSITIVE_TEST
import com.samsung.healthcare.account.application.context.ContextHolder
import com.samsung.healthcare.account.domain.AccessProjectAuthority
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.AssignRoleAuthority
import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.account.domain.Role
import com.samsung.healthcare.account.domain.Role.ProjectRole.ProjectOwner
import com.samsung.healthcare.account.domain.Role.ProjectRole.Researcher
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory
import reactor.core.publisher.Mono
import reactor.kotlin.test.verifyError
import reactor.test.StepVerifier

interface Target {
    fun testAssignRole(roles: List<Role>): Mono<Void>
    fun testAccessProject(projectId: String): Mono<Void>
}

open class TestTarget : Target {
    @Requires([AssignRoleAuthority::class])
    override fun testAssignRole(roles: List<Role>) = Mono.empty<Void>()

    @Requires([AccessProjectAuthority::class])
    override fun testAccessProject(projectId: String) = Mono.empty<Void>()
}

internal class AccessControlAspectTest {

    private val testTarget = TestTarget()

    val target = AspectJProxyFactory(testTarget).apply {
        addAspect(AccessControlAspect())
    }.getProxy<Target>()

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should throw IllegalAccessException when account does not have owner roles for project`() {
        val projectId = "project-id"
        StepVerifier.create(
            withAccountContext(
                target.testAssignRole(
                    listOf(Researcher(projectId))
                ),
                testAccount(Researcher(projectId))
            )
        ).verifyError<IllegalAccessException>()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should return ok when account does have project owner roles and assign researcher`() {
        val projectId = "project-id"
        val roles = listOf(Researcher(projectId))

        StepVerifier.create(
            withAccountContext(
                target.testAssignRole(roles),
                testAccount(ProjectOwner(projectId))
            )
        ).verifyComplete()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should throw IllegalAccessException when context has no account`() {
        StepVerifier.create(
            target.testAssignRole(emptyList())
        ).verifyError<IllegalAccessException>()

        StepVerifier.create(
            target.testAccessProject("project-id")
        ).verifyError<IllegalAccessException>()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should throw IllegalAccessException when account does not have access roles for project`() {
        val projectId = "project-id"
        StepVerifier.create(
            withAccountContext(
                target.testAccessProject(
                    projectId
                ),
                testAccount(Researcher("another-project-id"))
            )
        ).verifyError<IllegalAccessException>()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should return ok when account does have a access role`() {
        val projectId = "project-id"

        StepVerifier.create(
            withAccountContext(
                target.testAccessProject(projectId),
                testAccount(Researcher(projectId))
            )
        ).verifyComplete()

        StepVerifier.create(
            withAccountContext(
                target.testAccessProject(projectId),
                testAccount(ProjectOwner(projectId))
            )
        ).verifyComplete()
    }

    private fun withAccountContext(mono: Mono<*>, account: Account) =
        ContextHolder.setAccount(mono, account)

    private fun testAccount(vararg roles: Role) =
        Account("account", Email("test@test.com"), roles.asList())
}
