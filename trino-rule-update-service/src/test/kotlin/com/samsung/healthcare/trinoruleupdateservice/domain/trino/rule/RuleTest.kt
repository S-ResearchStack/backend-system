package com.samsung.healthcare.trinoruleupdateservice.domain.trino.rule

import com.samsung.healthcare.trinoruleupdateservice.NEGATIVE_TEST
import com.samsung.healthcare.trinoruleupdateservice.POSITIVE_TEST
import com.samsung.healthcare.trinoruleupdateservice.application.config.ApplicationProperties
import com.samsung.healthcare.trinoruleupdateservice.application.config.ApplicationProperties.AccessControlConfig
import com.samsung.healthcare.trinoruleupdateservice.application.config.ApplicationProperties.AccountServiceConfig
import com.samsung.healthcare.trinoruleupdateservice.application.config.ApplicationProperties.DatabaseConfig
import com.samsung.healthcare.trinoruleupdateservice.application.config.ApplicationProperties.TrinoConfig
import com.samsung.healthcare.trinoruleupdateservice.application.config.ApplicationProperties.TrinoConfig.Catalogs
import com.samsung.healthcare.trinoruleupdateservice.domain.accountservice.User
import com.samsung.healthcare.trinoruleupdateservice.domain.trino.rule.Rule.Companion.ALL
import com.samsung.healthcare.trinoruleupdateservice.domain.trino.rule.Rule.Companion.SELECT_PRIVILEGE
import com.samsung.healthcare.trinoruleupdateservice.domain.trino.rule.Rule.Companion.newRule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class RuleTest {
    private val config = ApplicationProperties(
        DatabaseConfig("prefix", "postfix"),
        AccountServiceConfig("localhost"),
        TrinoConfig(
            Catalogs(Catalogs.Db("db1"), Catalogs.Db("db2")),
            AccessControlConfig("/"),
        )
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `newRule should return rule with given users`() {
        val projectId = "1"
        val studyCreator = User(
            "study-creator-id",
            "study-creator@research-hub.test.com",
            listOf("$projectId:study-creator")
        )
        val principalInvestigator = User(
            "principal-id",
            "principal@research-hub.test.com",
            listOf("$projectId:principal-investigator")
        )
        val researchAssistant = User(
            "research-assistant-id",
            "research-assistant@research-hub.test.com",
            listOf("$projectId:research-assistant")
        )

        val users = listOf(studyCreator, principalInvestigator, researchAssistant)

        val rule = newRule(users, config)

        assertNotNull(rule.tables)
        assertNotNull(rule.schemas)
        assertNull(rule.columns)

        rule.tables!!.filter { table ->
            table.user in listOf(studyCreator.id, principalInvestigator.id, researchAssistant.id)
        }.forEach { table ->
            assertTrue(table.schema?.contains(projectId) ?: false)
            assertEquals(listOf(SELECT_PRIVILEGE), table.privileges)
            assertNull(table.group)
            assertNull(table.role)
            assertEquals(ALL, table.table)
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `newRule should throw IllegalArgumentException when role of user is invalid`() {
        val user = User("study-creator", "study-creator@research-hub.test.com", listOf("study-creator"))

        assertThrows<IllegalArgumentException> {
            newRule(listOf(user), config)
        }
    }
}
