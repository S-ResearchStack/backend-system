package com.samsung.healthcare.dataqueryservice.application.service

import com.samsung.healthcare.dataqueryservice.NEGATIVE_TEST
import com.samsung.healthcare.dataqueryservice.POSITIVE_TEST
import com.samsung.healthcare.dataqueryservice.application.exception.ForbiddenSqlStatementTypeException
import com.samsung.healthcare.dataqueryservice.application.port.input.QueryDataCommand
import com.samsung.healthcare.dataqueryservice.application.port.input.QueryDataResultSet
import com.samsung.healthcare.dataqueryservice.application.port.input.QueryDataResultSet.MetaData
import com.samsung.healthcare.dataqueryservice.application.port.output.QueryDataPort
import com.samsung.healthcare.dataqueryservice.application.port.output.QueryDataResult
import io.mockk.every
import io.mockk.mockk
import io.trino.sql.parser.ParsingException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class QueryDataServiceTest {
    private val queryDataPort = mockk<QueryDataPort>()

    private val queryDataService = QueryDataService(queryDataPort)

    @Test
    @Tag(POSITIVE_TEST)
    fun `execute should not emit event`() {
        every { queryDataPort.executeQuery(any(), any(), any()) } returns QueryDataResult(emptyList(), emptyList())

        val expectedResult = QueryDataResultSet(MetaData(emptyList(), 0), emptyList())
        val actualResult = queryDataService.execute(
            "project-id",
            "account-id",
            QueryDataCommand("SELECT * from tables")
        )

        assertEquals(expectedResult, actualResult)
    }

    @ParameterizedTest
    @ValueSource(strings = ["SHOW COLUMNS FROM t1", "DESC t1", "SHOW catalogs", "SHOW schemas", "SHOW session"])
    @Tag(NEGATIVE_TEST)
    fun `executeQuery should throw ForbiddenSqlStatementTypeException for forbidden statement types`(sql: String) {
        assertThrows<ForbiddenSqlStatementTypeException> {
            queryDataService.execute("1", "accountId", QueryDataCommand(sql))
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["invalid", " ", "SELECT * FROM t1;", "SELECT * FROM (SHOW COLUMNS FROM t1)"])
    @Tag(NEGATIVE_TEST)
    fun `executeQuery should throw ParsingException when sql statement was invalid`(sql: String) {
        assertThrows<ParsingException> {
            queryDataService.execute("1", "accountId", QueryDataCommand(sql))
        }
    }
}
