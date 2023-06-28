package com.samsung.healthcare.trinoruleupdateservice.adapter.output.web

import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.github.tomakehurst.wiremock.junit5.WireMockExtension
import com.marcinziolo.kotlin.wiremock.equalTo
import com.marcinziolo.kotlin.wiremock.get
import com.marcinziolo.kotlin.wiremock.returnsJson
import com.samsung.healthcare.trinoruleupdateservice.NEGATIVE_TEST
import com.samsung.healthcare.trinoruleupdateservice.POSITIVE_TEST
import com.samsung.healthcare.trinoruleupdateservice.application.config.ApplicationProperties
import com.samsung.healthcare.trinoruleupdateservice.application.config.ApplicationProperties.AccessControlConfig
import com.samsung.healthcare.trinoruleupdateservice.application.config.ApplicationProperties.AccountServiceConfig
import com.samsung.healthcare.trinoruleupdateservice.application.config.ApplicationProperties.DatabaseConfig
import com.samsung.healthcare.trinoruleupdateservice.application.config.ApplicationProperties.TrinoConfig
import com.samsung.healthcare.trinoruleupdateservice.application.config.ApplicationProperties.TrinoConfig.Catalogs
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

internal class AccountServiceAdapterTest {

    @JvmField
    @RegisterExtension
    val wm = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort())
        .build()

    private val accountServiceAdapter by lazy {
        AccountServiceAdapter(
            ApplicationProperties(
                databaseConfig = DatabaseConfig("prefix", "postfix"),
                accountService = AccountServiceConfig("http://localhost:${wm.port}"),
                trino = TrinoConfig(
                    Catalogs(Catalogs.Db("db1"), Catalogs.Db("db2")),
                    AccessControlConfig("/etc/trino/access-control/rules.json"),
                )
            )
        )
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getUsers should not emit event`() {

        val uuid = "random-uuid"
        val email = "test@research-hub.test.com"

        wm.get {
            url equalTo "/internal/account-service/users"
        } returnsJson {
            body = """[
{
  "email": "$email",
  "id": "$uuid",
  "roles": ["1:research-assistant"],
  "profile": {}
}
]"""
        }

        val users = accountServiceAdapter.getUsers()

        assertEquals(1, users.size)
        assertEquals(uuid, users[0].id)
        assertEquals(email, users[0].email)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getUsers should throw error if role does not match ROLE_REGEX`() {
        val uuid = "random-uuid"
        val email = "test@research-hub.test.com"

        wm.get {
            url equalTo "/internal/account-service/users"
        } returnsJson {
            body = """[
{
  "email": "$email",
  "id": "$uuid",
  "roles": ["team-amdin"],
  "profile": {}
}
]"""
        }

        assertThrows<IllegalArgumentException> {
            accountServiceAdapter.getUsers()
        }
    }
}
