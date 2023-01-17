package com.samsung.healthcare.platform.application.service.project

import com.samsung.healthcare.platform.NEGATIVE_TEST
import com.samsung.healthcare.platform.POSITIVE_TEST
import com.samsung.healthcare.platform.adapter.web.context.ContextHolder
import com.samsung.healthcare.platform.adapter.web.context.ContextHolder.getFirebaseToken
import com.samsung.healthcare.platform.application.exception.ForbiddenException
import com.samsung.healthcare.platform.application.exception.UserAlreadyExistsException
import com.samsung.healthcare.platform.application.port.input.CreateUserCommand
import com.samsung.healthcare.platform.application.port.output.project.UserProfileOutputPort
import com.samsung.healthcare.platform.domain.project.UserProfile
import com.samsung.healthcare.platform.domain.project.UserProfile.UserId
import io.mockk.MockKMatcherScope
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

internal class UserProfileServiceTest {
    private val userProfileOutputPort = mockk<UserProfileOutputPort>()
    private val userProfileService = UserProfileService(userProfileOutputPort)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should throw forbidden if token does not match`() = runTest {
        mockkObject(ContextHolder)
        coEvery { getFirebaseToken().uid } returns "tokenUID"
        val createUserCommand = CreateUserCommand("wrongUID", emptyMap())

        assertThrows<ForbiddenException> { userProfileService.registerUser(createUserCommand) }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should throw UserAlreadyExistsException if uid already exists`() = runTest {
        mockkObject(ContextHolder)
        val uid = "legalUID"
        coEvery { getFirebaseToken().uid } returns uid
        coEvery {
            userProfileOutputPort.create(match { it.userId.value == uid })
        } throws UserAlreadyExistsException()

        assertThrows<UserAlreadyExistsException> { userProfileService.registerUser(CreateUserCommand(uid, emptyMap())) }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should register corresponding user`() = runTest {
        mockkObject(ContextHolder)
        val uid = "legalUID"
        coEvery { getFirebaseToken().uid } returns uid
        val createUserCommand = CreateUserCommand(uid, emptyMap())

        mockkStatic(LocalDateTime::class)
        val testLocalDateTime = LocalDateTime.parse("2022-10-21T17:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        every { LocalDateTime.now() } returns testLocalDateTime

        coEvery { userProfileOutputPort.existsByUserId(UserId.from(uid)) } returns false
        coJustRun { userProfileOutputPort.create(userMatchesCommand(createUserCommand, testLocalDateTime)) }

        userProfileService.registerUser(createUserCommand)

        coVerify { userProfileOutputPort.create(userMatchesCommand(createUserCommand, testLocalDateTime)) }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should request to update lastSyncedAt`() = runTest {
        val userId = UserId.from("userId")

        coJustRun { userProfileOutputPort.updateLastSyncedAt(userId) }

        userProfileService.updateLastSyncedTime(userId)

        coVerify { userProfileOutputPort.updateLastSyncedAt(userId) }
    }

    private fun MockKMatcherScope.userMatchesCommand(
        createUserCommand: CreateUserCommand,
        time: LocalDateTime
    ): UserProfile =
        match {
            it.userId.value == createUserCommand.userId &&
                it.profile == createUserCommand.profile &&
                it.lastSyncedAt == time
        }
}
