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
import com.samsung.healthcare.platform.application.port.input.project.UpdateInLabVisitCommand
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
internal class UpdateInLabVisitServiceTest {
    private val inLabVisitOutputPort = mockk<InLabVisitOutputPort>()

    private val updateInLabVisitService = UpdateInLabVisitService(inLabVisitOutputPort)

    private val projectId = Project.ProjectId.from(1)
    val account = Account(
        "account-id",
        Email("cubist@test.com"),
        listOf(Role.ProjectRole.ResearchAssistant(projectId.value.toString()))
    )

    @Test
    @Tag(NEGATIVE_TEST)
    fun `updateInLabVisit should throw forbidden when account do not have project authority`() = runTest {
        val command = UpdateInLabVisitCommand("u1", "c1", LocalDateTime.now(), LocalDateTime.now(), null)

        mockkObject(ContextHolder)
        val wrongProjectId = Project.ProjectId.from(2)
        every { ContextHolder.getAccount() } returns Mono.just(account)

        assertThrows<ForbiddenException>("should throw a forbidden exception") {
            updateInLabVisitService.updateInLabVisit(wrongProjectId.toString(), 1, command)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `updateInLabVisit should work properly`() = runTest {
        val time = LocalDateTime.now()
        val inLabVisitId = 1
        val data = InLabVisit(inLabVisitId, "u1", "c1", time, time, null, null)

        mockkObject(Authorizer)
        every { Authorizer.getAccount(AccessInLabVisitAuthority(projectId.toString())) } returns mono { account }
        coEvery {
            inLabVisitOutputPort.update(data)
        } returns data

        val response = updateInLabVisitService.updateInLabVisit(
            projectId.value.toString(),
            inLabVisitId,
            UpdateInLabVisitCommand(data.userId, data.checkedInBy, data.startTime, data.endTime, data.notes)
        )

        Assertions.assertThat(response).isEqualTo(data)
    }
}
