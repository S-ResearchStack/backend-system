package com.samsung.healthcare.account.domain

import com.samsung.healthcare.account.NEGATIVE_TEST
import com.samsung.healthcare.account.POSITIVE_TEST
import com.samsung.healthcare.account.domain.Role.ProjectRole
import com.samsung.healthcare.account.domain.Role.ProjectRole.Companion.SEPARATOR
import com.samsung.healthcare.account.domain.Role.ProjectRole.PrincipalInvestigator
import com.samsung.healthcare.account.domain.Role.ProjectRole.StudyCreator
import com.samsung.healthcare.account.domain.Role.ProjectRole.ResearchAssistant
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
    @ValueSource(strings = [":research", "  :study-creator"])
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

    companion object {
        @JvmStatic
        private fun providesRoleNameAndClass() =
            Stream.of(
                Arguments.of(Role.PRINCIPAL_INVESTIGATOR, PrincipalInvestigator::class),
                Arguments.of(Role.RESEARCH_ASSISTANT, ResearchAssistant::class),
                Arguments.of(Role.STUDY_CREATOR, StudyCreator::class),
            )
    }
}
