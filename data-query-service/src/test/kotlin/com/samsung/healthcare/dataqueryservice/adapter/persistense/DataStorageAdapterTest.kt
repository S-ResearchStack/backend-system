package com.samsung.healthcare.dataqueryservice.adapter.persistense

import com.samsung.healthcare.dataqueryservice.NEGATIVE_TEST
import com.samsung.healthcare.dataqueryservice.POSITIVE_TEST
import com.samsung.healthcare.dataqueryservice.adapter.persistence.DataStorageAdapter
import com.samsung.healthcare.dataqueryservice.application.config.ApplicationProperties
import com.samsung.healthcare.dataqueryservice.application.config.ApplicationProperties.Db
import com.samsung.healthcare.dataqueryservice.application.config.ApplicationProperties.JwksConfig
import com.samsung.healthcare.dataqueryservice.application.config.ApplicationProperties.Trino
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.sql.Connection
import java.sql.Date
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Time
import java.sql.Timestamp
import java.sql.Types.BIGINT
import java.sql.Types.DATE
import java.sql.Types.TIME
import java.sql.Types.TIMESTAMP
import java.sql.Types.VARCHAR

class DataStorageAdapterTest {

    private val appProperties = ApplicationProperties(
        db = Db("pre", "post"),
        trino = Trino("jdbc://127.0.0.1", "postgres", "trino-user"),
        jwks = JwksConfig("https://127.0.0.1:3567/jwks")
    )

    private val dataStorageAdapter = DataStorageAdapter(appProperties)

    @Test
    @Tag(POSITIVE_TEST)
    fun `executeQuery should return query result set`() {
        mockkStatic(DriverManager::class)
        val connection = mockk<Connection>()
        every { DriverManager.getConnection(any(), any()) } returns connection

        val columns = listOf(
            "id" to BIGINT,
            "name" to VARCHAR,
            "time" to TIME,
            "timestamp" to TIMESTAMP,
            "date" to DATE
        )

        data class Row(val id: Long, val name: String, val date: Date, val time: Time, val timestamp: Timestamp)

        val resultSet = mockk<ResultSet>()
        every { resultSet.metaData.columnCount } returns columns.size
        every { resultSet.metaData.getColumnName(any()) } answers {
            val index = this.firstArg<Int>()
            columns[index - 1].first
        }

        every { resultSet.metaData.getColumnType(any()) } answers {
            val index = this.firstArg<Int>()
            columns[index - 1].second
        }

        val rowData = listOf(
            Row(1L, "alice", Date(1000L), Time(1000L), Timestamp(1000L))
        )
        val rowDataIterator = rowData.iterator()

        var row: Row? = null
        every { resultSet.next() } answers {
            if (!rowDataIterator.hasNext()) false
            else {
                row = rowDataIterator.next()
                true
            }
        }

        val sql = "SELECT * FROM table1"

        every { resultSet.getString(any<Int>()) } answers { row?.name }
        every { resultSet.getTime(any<Int>()) } answers { row?.time }
        every { resultSet.getLong(any<Int>()) } answers { row?.id!! }
        every { resultSet.getDate(any<Int>()) } answers { row?.date }
        every { resultSet.getTimestamp(any<Int>()) } answers { row?.timestamp }

        every { connection.prepareStatement(sql).executeQuery() } returns resultSet
        justRun { connection.close() }

        val queryResultSet =
            dataStorageAdapter.executeQuery("projectId", "accountId", sql)

        assertEquals(columns.map { it.first }, queryResultSet.columns)
        assertEquals(rowData.size, queryResultSet.data.size)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `executeQuery should throw when project id is empty`() {
        assertThrows<IllegalArgumentException> {
            dataStorageAdapter.executeQuery(" ", "accountId", "SELECT * FROM table1")
        }
    }
}
