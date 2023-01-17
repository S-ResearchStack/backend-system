package com.samsung.healthcare.dataqueryservice.application.service

import com.samsung.healthcare.dataqueryservice.NEGATIVE_TEST
import com.samsung.healthcare.dataqueryservice.POSITIVE_TEST
import com.samsung.healthcare.dataqueryservice.application.exception.TableNotFoundException
import com.samsung.healthcare.dataqueryservice.application.port.input.Column
import com.samsung.healthcare.dataqueryservice.application.port.output.QueryDataPort
import com.samsung.healthcare.dataqueryservice.application.port.output.QueryDataResult
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.sql.SQLDataException

internal class TableDataQueryServiceTest {
    private val queryDataPort = mockk<QueryDataPort>()

    private val tableDataQueryService = TableDataQueryService(queryDataPort)

    private val testProjectId = "test-project-id"
    private val testAccountId = "test-account-id"

    @Test
    @Tag(POSITIVE_TEST)
    fun `listAllTables should return name of all tables`() {
        val names = setOf("table1", "table2", "table3")

        every {
            queryDataPort.executeQuery(testProjectId, testAccountId, SHOW_TABLE_QUERY)
        } returns QueryDataResult(emptyList(), names.map { mapOf("name" to it) })

        val tableNames = tableDataQueryService.listAllTables(testProjectId, testAccountId)
            .map { it.name }
            .toSet()

        assertEquals(names, tableNames)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `columnsOfTable should return all columns of given table`() {
        val tableName = "table_name"

        val columns = setOf(Column("user_id", "varchar(320)"), Column("profile", "json"))
        every {
            queryDataPort.executeQuery(testProjectId, testAccountId, any())
        } returns QueryDataResult(emptyList(), columns.map { mapOf("Column" to it.name, "Type" to it.type) })

        val actualColumns = tableDataQueryService.columnsOfTable(testProjectId, tableName, testAccountId)
            .toSet()

        assertEquals(columns, actualColumns)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["1table", "table-name", "table@name", "table`name"])
    fun `columnsOfTable should throw IllegalArgumentException when table name is invalid`(tableName: String) {
        assertThrows<IllegalArgumentException> {
            tableDataQueryService.columnsOfTable(testProjectId, tableName, testAccountId)
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `columnsOfTable should throw TableNotFound when not existed tableName`() {
        val tableName = "not_existed"
        every {
            queryDataPort.executeQuery(testProjectId, testAccountId, any())
        } throws SQLDataException()

        assertThrows<TableNotFoundException> {
            tableDataQueryService.columnsOfTable(testProjectId, tableName, testAccountId)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `countOfTable should return the number of table data`() {
        val tableName = "test_table"
        val rowCount = 777L
        every {
            queryDataPort.executeQuery(testProjectId, testAccountId, any())
        } returns QueryDataResult(emptyList(), listOf(mapOf("total" to rowCount)))

        val actualCount = tableDataQueryService.countOfTable(testProjectId, tableName, testAccountId)
        assertEquals(rowCount, actualCount)
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["1table", "table-name", "table@name", "table`name"])
    fun `countOfTable should throw IllegalArgumentException when table name is invalid`(tableName: String) {
        assertThrows<IllegalArgumentException> {
            tableDataQueryService.countOfTable(testProjectId, tableName, testAccountId)
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `countOfTable should throw TableNotFound when not existed tableName`() {
        val tableName = "not_existed"
        every {
            queryDataPort.executeQuery(testProjectId, testAccountId, any())
        } throws SQLDataException()

        assertThrows<TableNotFoundException> {
            tableDataQueryService.countOfTable(testProjectId, tableName, testAccountId)
        }
    }
}
