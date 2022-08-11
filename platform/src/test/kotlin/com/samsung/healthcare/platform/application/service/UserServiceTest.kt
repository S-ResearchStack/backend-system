package com.samsung.healthcare.platform.application.service

import com.samsung.healthcare.platform.application.port.input.RegisterUserCommand
import com.samsung.healthcare.platform.application.port.output.UserOutputPort
import com.samsung.healthcare.platform.domain.User
import com.samsung.healthcare.platform.domain.User.UserId
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class UserServiceTest {
    private val userOutputPort = mockk<UserOutputPort>()
    private val userService = UserService(userOutputPort)
    private val userId = UserId.from("test@abc.com")

    @Test
    fun `existsEmail should return true if already existed email`(): Unit = runBlocking {
        coEvery { userOutputPort.existsById(userId) } returns true
        assertTrue(userService.existsById(userId))
    }

    @Test
    fun `existsEmail should return false if not existed email`(): Unit = runBlocking {
        coEvery { userOutputPort.existsById(userId) } returns false
        assertFalse(userService.existsById(userId))
    }

    @Test
    fun `registerUser should return new userId`(): Unit = runBlocking {
        val newUserId = UserId.from("1")
        val user = User.newUser(newUserId, "sub", "provider")
        coEvery { userOutputPort.create(user) } returns newUserId
        assertEquals(
            newUserId,
            userService.registerUser(RegisterUserCommand(newUserId, user.sub, user.provider))
        )
    }
}
