package com.samsung.healthcare.dataqueryservice.adapter.web.graphql

import com.netflix.graphql.dgs.DgsQueryExecutor
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration
import com.netflix.graphql.types.errors.ErrorType.NOT_FOUND
import com.netflix.graphql.types.errors.TypedGraphQLError
import com.ninjasquad.springmockk.MockkBean
import com.samsung.healthcare.dataqueryservice.NEGATIVE_TEST
import com.samsung.healthcare.dataqueryservice.POSITIVE_TEST
import com.samsung.healthcare.dataqueryservice.adapter.web.PROJECT_ID
import com.samsung.healthcare.dataqueryservice.adapter.web.exception.DataFetchingExceptionHandler
import com.samsung.healthcare.dataqueryservice.application.context.AuthContext
import com.samsung.healthcare.dataqueryservice.application.exception.TableNotFoundException
import com.samsung.healthcare.dataqueryservice.application.port.input.Column
import com.samsung.healthcare.dataqueryservice.application.port.input.Table
import com.samsung.healthcare.dataqueryservice.application.port.input.TableDataQuery
import graphql.ExecutionResult
import io.mockk.every
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders

@SpringBootTest(classes = [DgsAutoConfiguration::class, TableDataFetcher::class, DataFetchingExceptionHandler::class])
internal class TableDataFetcherTest {

    @MockkBean
    lateinit var tableQuery: TableDataQuery

    @Autowired
    lateinit var dgsQueryExecutor: DgsQueryExecutor

    @Test
    @Tag(POSITIVE_TEST)
    fun `tables should return table with columns`() {
        val tables = listOf(Table("table_1"), Table("table_2"))
        val columns = listOf(Column("column_1", "varchar(20)"), Column("column_2", "json"))
        every { tableQuery.listAllTables(any(), any()) } returns tables
        every { tableQuery.columnsOfTable(any(), any(), any()) } returns columns

        val result = executeGraphQLQuery(
            """
                {
                    tables {
                        name
                        columns {
                            name
                            type
                        }
                    }
                }
            """.trimIndent()
        )

        assertTrue(result.errors.isEmpty())
        val data = result.getData<Map<String, List<Map<String, Any>>>>()["tables"] ?: fail("")
        assertEquals(tables, data.map { Table(it["name"] as String) })
        @Suppress("UNCHECKED_CAST")
        val columnResult = data.first()["columns"] as? List<Map<String, String>>
        assertNotNull(columnResult)

        val actualColumns = columnResult?.mapNotNull {
            val name = it["name"]
            val type = it["type"]
            if (name == null || type == null) null
            else Column(name, type)
        }
        assertEquals(columns, actualColumns)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `count should return the number of table data`() {
        val count = 777
        every { tableQuery.countOfTable(any(), any(), any()) } returns count.toLong()

        val result = executeTableCountQuery("table_1")

        assertTrue(result.errors.isEmpty())
        val data = result.getData<Map<String, Int>>()

        assertEquals(count, data["count"])
    }

    private fun executeTableCountQuery(tableName: String) = executeGraphQLQuery(
        """
                {
                    count(tableName: "$tableName")
                }
        """.trimIndent()
    )

    @Test
    @Tag(NEGATIVE_TEST)
    fun `count should error when not existed table-name`() {
        every { tableQuery.countOfTable(any(), any(), any()) } throws TableNotFoundException()

        val result = executeTableCountQuery("not_existed")

        assertTrue(result.errors.isNotEmpty())

        val errorTypes = result.errors.filterIsInstance<TypedGraphQLError>()
            .map { it.extensions["errorType"] as String }

        assertTrue(errorTypes.contains(NOT_FOUND.name))
    }

    private fun executeGraphQLQuery(query: String): ExecutionResult = dgsQueryExecutor.execute(
        query, emptyMap(), emptyMap(),
        HttpHeaders().apply {
            this[PROJECT_ID] = "test-project-id"
            this[AuthContext.ACCOUNT_ID_KEY_NAME] = "test-account-id"
        }
    )
}
