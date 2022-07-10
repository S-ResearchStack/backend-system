package com.samsung.healthcare.account.domain

import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class EmailTest {
    @ParameterizedTest
    @ValueSource(strings = [".123@com", "without-at", "@com", "a.b.com"])
    fun `Email should throw IllegalArgumentException when email is invalid`(value: String) {
        assertThrows<IllegalArgumentException> { Email(value) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["123@abc.com", "12-ab@abc.com"])
    fun `Email should return instance when email is valid`(value: String) {
        assertDoesNotThrow { Email(value) }
    }
}
