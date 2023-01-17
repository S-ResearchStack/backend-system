package com.samsung.healthcare.platform.domain

import com.samsung.healthcare.platform.NEGATIVE_TEST
import com.samsung.healthcare.platform.POSITIVE_TEST
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ProjectTest {

    @Test
    @Tag(NEGATIVE_TEST)
    fun `project id cannot be null`() {
        assertThrows<IllegalArgumentException> { Project.ProjectId.from(null) }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `project id cannot be negative number`() {
        assertThrows<IllegalArgumentException> { Project.ProjectId.from(-1) }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `projectId constructor should work properly`() {
        val id = 5

        val projectId = Project.ProjectId.from(id)

        assertEquals(id.toString(), projectId.toString())
    }
}
