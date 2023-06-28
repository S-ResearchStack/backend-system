package com.samsung.healthcare.dataqueryservice.adapter.persistense

import com.samsung.healthcare.dataqueryservice.NEGATIVE_TEST
import com.samsung.healthcare.dataqueryservice.adapter.persistence.DataStorageAdapter
import com.samsung.healthcare.dataqueryservice.application.config.ApplicationProperties
import com.samsung.healthcare.dataqueryservice.application.config.ApplicationProperties.Db
import com.samsung.healthcare.dataqueryservice.application.config.ApplicationProperties.JwksConfig
import com.samsung.healthcare.dataqueryservice.application.config.ApplicationProperties.Trino
import com.samsung.healthcare.dataqueryservice.application.context.AuthContext
import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DataStorageAdapterTest {

    private val appProperties = ApplicationProperties(
        db = Db("pre", "post"),
        trino = Trino("jdbc://127.0.0.1", "postgres", "di-postgres", "trino-user"),
        jwks = JwksConfig("https://127.0.0.1:3567/jwks")
    )

    private val dataStorageAdapter = DataStorageAdapter(appProperties)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `executeQuery should throw when project id is empty`() {
        assertThrows<IllegalArgumentException> {
            dataStorageAdapter.executeQuery(" ", "accountId", "SELECT * FROM table1")
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `dbConnection should throw when user does not have any role on the project`() {
        mockkObject(AuthContext)
        every { AuthContext.getValue(any()) } returns null

        assertThrows<IllegalArgumentException> {
            dataStorageAdapter.executeQuery("1", "accountId", "SELECT * FROM table1")
        }
    }
}
