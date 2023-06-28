package com.samsung.healthcare.cloudstorageservice.application.service

import com.samsung.healthcare.account.application.context.ContextHolder
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.account.domain.Role
import com.samsung.healthcare.cloudstorageservice.NEGATIVE_TEST
import com.samsung.healthcare.cloudstorageservice.POSITIVE_TEST
import com.samsung.healthcare.cloudstorageservice.application.port.output.ListObjectsPort
import com.samsung.healthcare.cloudstorageservice.domain.ObjectInfo
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

internal class ListObjectsServiceTest {
    private val listObjectsPort = mockk<ListObjectsPort>()

    private val listObjectsService = ListObjectsService(listObjectsPort)

    private val path = "test-path"
    private val projectId = "test-id"
    val account = Account(
        "account-id",
        Email("cubist@test.com"),
        listOf(Role.ProjectRole.ResearchAssistant(projectId)),
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `listObjects should work properly`() {
        val objects = listOf(
            ObjectInfo("$path/test-name-1", 1),
            ObjectInfo("$path/test-name-2", 10),
        )

        mockkObject(ContextHolder)
        every { ContextHolder.getAccount() } returns Mono.just(account)
        every { listObjectsPort.list("$projectId/$path/") } returns
            objects.map {
                ObjectInfo("$projectId/${it.name}", it.size)
            }

        StepVerifier.create(
            listObjectsService.listObjects(projectId, path)
        )
            .expectNextMatches {
                it == objects
            }
            .verifyComplete()
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `listObjects should throw IllegalArgumentException when it receives empty projectId`(
        projectId: String
    ) {
        assertThrows<IllegalArgumentException> {
            listObjectsService.listObjects(projectId, path)
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["", " "])
    fun `listObjects should throw IllegalArgumentException when it receives empty path`(
        path: String
    ) {
        assertThrows<IllegalArgumentException> {
            listObjectsService.listObjects(projectId, path)
        }
    }
}
