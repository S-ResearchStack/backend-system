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
import com.samsung.healthcare.platform.application.exception.NotFoundException
import com.samsung.healthcare.platform.application.port.output.project.InLabVisitOutputPort
import com.samsung.healthcare.platform.domain.Project
import com.samsung.healthcare.platform.domain.project.InLabVisit
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.reactor.mono
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@kotlinx.coroutines.ExperimentalCoroutinesApi
internal class GetInLabVisitServiceTest {
    private val inLabVisitOutputPort = mockk<InLabVisitOutputPort>()

    private val getInLabVisitService = GetInLabVisitService(inLabVisitOutputPort)

    private val projectId = Project.ProjectId.from(1)
    val account = Account(
        "account-id",
        Email("cubist@test.com"),
        listOf(Role.ProjectRole.ResearchAssistant(projectId.value.toString()))
    )

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getInLabVisitById should throw forbidden when account do not have project authority`() = runTest {
        mockkObject(ContextHolder)
        val wrongProjectId = Project.ProjectId.from(2)
        every { ContextHolder.getAccount() } returns Mono.just(account)

        assertThrows<ForbiddenException>("should throw a forbidden exception") {
            getInLabVisitService.getInLabVisitById(wrongProjectId.toString(), 1)
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getInLabVisitById should throw not found when no data found for given id`() = runTest {
        val inLabVisitId = 1

        mockkObject(Authorizer)
        every { Authorizer.getAccount(AccessInLabVisitAuthority(projectId.toString())) } returns mono { account }
        coEvery {
            inLabVisitOutputPort.findById(inLabVisitId)
        } returns null

        assertThrows<NotFoundException>("should throw a not found exception") {
            getInLabVisitService.getInLabVisitById(projectId.value.toString(), inLabVisitId)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getInLabVisitById should work properly`() = runTest {
        val time = LocalDateTime.now()
        val inLabVisitId = 1
        val inLabVisit = InLabVisit(inLabVisitId, "u1", "c1", time, time, null, "")
        mockkObject(Authorizer)
        every { Authorizer.getAccount(AccessInLabVisitAuthority(projectId.toString())) } returns mono { account }
        coEvery {
            inLabVisitOutputPort.findById(inLabVisitId)
        } returns inLabVisit

        val response = getInLabVisitService.getInLabVisitById(
            projectId.value.toString(),
            inLabVisitId,
        )

        Assertions.assertThat(response).isEqualTo(inLabVisit)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getInLabVisits should throw forbidden when account do not have project authority`() = runTest {
        mockkObject(ContextHolder)
        val wrongProjectId = Project.ProjectId.from(2)
        every { ContextHolder.getAccount() } returns Mono.just(account)

        assertThrows<ForbiddenException>("should throw a forbidden exception") {
            getInLabVisitService.getInLabVisits(wrongProjectId.toString())
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getInLabVisits should work properly`() = runTest {

        mockkObject(Authorizer)
        every { Authorizer.getAccount(AccessInLabVisitAuthority(projectId.toString())) } returns mono { account }
        coEvery {
            inLabVisitOutputPort.findAll()
        } returns emptyFlow()

        val response = getInLabVisitService.getInLabVisits(
            projectId.value.toString(),
        )

        Assertions.assertThat(response).isEqualTo(emptyFlow<InLabVisit>())
    }
}
