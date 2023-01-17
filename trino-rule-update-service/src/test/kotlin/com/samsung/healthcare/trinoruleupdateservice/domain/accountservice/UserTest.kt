package com.samsung.healthcare.trinoruleupdateservice.domain.accountservice

import com.samsung.healthcare.trinoruleupdateservice.POSITIVE_TEST
import com.samsung.healthcare.trinoruleupdateservice.domain.accountservice.User.Companion.newUser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

internal class UserTest {

    @Test
    @Tag(POSITIVE_TEST)
    fun `newUser should work properly`() {
        val id = "some-id"
        val email = "test@research-hub.test.com"
        val roles = listOf("role1", "role2")

        val user = newUser(id, email, roles)

        assertEquals(id, user.id)
        assertEquals(email, user.email)
        assertEquals(roles, user.roles)
    }
}
