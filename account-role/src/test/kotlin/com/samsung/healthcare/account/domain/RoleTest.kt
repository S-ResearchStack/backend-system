package com.samsung.healthcare.account.domain

import com.samsung.healthcare.account.NEGATIVE_TEST
import com.samsung.healthcare.account.POSITIVE_TEST
import com.samsung.healthcare.account.domain.Role.ProjectRole
import com.samsung.healthcare.account.domain.Role.ProjectRole.Companion.SEPARATOR
import com.samsung.healthcare.account.domain.Role.ProjectRole.CustomRole
import com.samsung.healthcare.account.domain.Role.ProjectRole.HeadResearcher
import com.samsung.healthcare.account.domain.Role.ProjectRole.ProjectOwner
import com.samsung.healthcare.account.domain.Role.ProjectRole.Researcher
import com.samsung.healthcare.account.domain.Role.TeamAdmin
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.stream.Stream
import kotlin.reflect.KClass

internal class RoleTest {
    @ParameterizedTest
    @ValueSource(strings = [":research", "  :project-owner"])
    @Tag(NEGATIVE_TEST)
    fun `createRole should throw IllegalArgumentException when project-id is empty`(value: String) {
        assertThrows<IllegalArgumentException> {
            RoleFactory.createRole(value)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["project-id: ", "project-x:"])
    @Tag(NEGATIVE_TEST)
    fun `createRole should throw IllegalArgumentException when role name is empty`(value: String) {
        assertThrows<IllegalArgumentException> {
            RoleFactory.createRole(value)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `createRole should return admin instance when string is admin`() {
        val role = RoleFactory.createRole(Role.TEAM_ADMIN)

        assertEquals(TeamAdmin, role)
    }

    @ParameterizedTest
    @MethodSource("providesRoleNameAndClass")
    @Tag(POSITIVE_TEST)
    fun `createRole should return mapped role`(roleName: String, expectedClass: KClass<ProjectRole>) {
        val projectId = "project_id"
        val role = RoleFactory.createRole("$projectId$SEPARATOR$roleName")

        assertEquals(expectedClass, role::class)
        assertTrue((role as ProjectRole).canAccessProject(projectId))
    }

    @ParameterizedTest
    @ValueSource(strings = ["custom-role", "test-role"])
    @Tag(POSITIVE_TEST)
    fun `createRole should return custom role with given role name`(roleName: String) {
        val projectId = "project_id"
        val role = RoleFactory.createRole("$projectId$SEPARATOR$roleName")
        assertEquals(CustomRole::class, role::class)
        assertTrue((role as ProjectRole).canAccessProject(projectId))
    }

    companion object {
        @JvmStatic
        private fun providesRoleNameAndClass() =
            Stream.of(
                Arguments.of(Role.HEAD_RESEARCHER, HeadResearcher::class),
                Arguments.of(Role.RESEARCHER, Researcher::class),
                Arguments.of(Role.PROJECT_OWNER, ProjectOwner::class),
            )
    }
}
