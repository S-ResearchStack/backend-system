package com.samsung.healthcare.trinoruleupdateservice.domain.trino.rule

import com.samsung.healthcare.trinoruleupdateservice.NEGATIVE_TEST
import com.samsung.healthcare.trinoruleupdateservice.POSITIVE_TEST
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

    @Test
    @Tag(POSITIVE_TEST)
    fun `newRule should return rule with given users`() {
        val projectId = "1"
        val owner = User("owner-id", "owner@research-hub.test.com", listOf("$projectId:project-owner"))
        val headResearcher = User("head-id", "head@research-hub.test.com", listOf("$projectId:head-researcher"))
        val researcher = User("researcher-id", "researcher@research-hub.test.com", listOf("$projectId:researcher"))
        val prefix = "prefix"
        val postfix = "postfix"

        val users = listOf(owner, headResearcher, researcher)

        val rule = newRule(users, prefix, postfix)

        assertNotNull(rule.tables)
        assertNotNull(rule.schemas)
        assertNull(rule.columns)

        rule.tables!!.filter { table ->
            table.user in listOf(owner.id, headResearcher.id, researcher.id)
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
        val user = User("owner-id", "owner@research-hub.test.com", listOf("project-owner"))

        assertThrows<IllegalArgumentException> {
            newRule(listOf(user))
        }
    }
}
