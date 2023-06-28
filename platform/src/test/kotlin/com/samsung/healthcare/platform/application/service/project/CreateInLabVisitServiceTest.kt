package com.samsung.healthcare.platform.application.service.project

import com.samsung.healthcare.account.application.context.ContextHolder
import com.samsung.healthcare.account.domain.AccessInLabVisitAuthority
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.account.domain.Role
import com.samsung.healthcare.platform.NEGATIVE_TEST
import com.samsung.healthcare.platform.POSITIVE_TEST
import com.samsung.healthcare.platform.application.authorize.Authorizer
import com.samsung.healthcare.platform.application.exception.ForbiddenException
import com.samsung.healthcare.platform.application.port.input.project.CreateInLabVisitCommand
import com.samsung.healthcare.platform.application.port.output.project.InLabVisitOutputPort
import com.samsung.healthcare.platform.domain.Project
import com.samsung.healthcare.platform.domain.project.InLabVisit
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.reactor.mono
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@kotlinx.coroutines.ExperimentalCoroutinesApi
internal class CreateInLabVisitServiceTest {
    private val inLabVisitOutputPort = mockk<InLabVisitOutputPort>()

    private val createInLabVisitService = CreateInLabVisitService(inLabVisitOutputPort)

    private val projectId = Project.ProjectId.from(1)
    val account = Account(
        "account-id",
        Email("cubist@test.com"),
        listOf(Role.ProjectRole.ResearchAssistant(projectId.value.toString()))
    )
    val command = CreateInLabVisitCommand("u1", "c1", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `createInLabVisit should throw forbidden when account do not have project authority`() = runTest {
        mockkObject(ContextHolder)
        val wrongProjectId = Project.ProjectId.from(2)
        every { ContextHolder.getAccount() } returns Mono.just(account)

        assertThrows<ForbiddenException>("should throw an forbidden exception") {
            createInLabVisitService.createInLabVisit(wrongProjectId.toString(), command)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `createInLabVisit should work properly`() = runTest {
        val data = InLabVisit(1, "u1", "c1", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, "")

        mockkObject(Authorizer)
        every { Authorizer.getAccount(AccessInLabVisitAuthority(projectId.toString())) } returns mono { account }
        coEvery {
            inLabVisitOutputPort.create(any())
        } returns data

        val response = createInLabVisitService.createInLabVisit(
            projectId.value.toString(),
            CreateInLabVisitCommand(data.userId, data.checkedInBy, data.startTime, data.endTime, data.notes),
        )

        Assertions.assertThat(response).isEqualTo(data)
    }
}
