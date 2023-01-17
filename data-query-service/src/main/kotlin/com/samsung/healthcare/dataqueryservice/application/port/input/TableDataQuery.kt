package com.samsung.healthcare.dataqueryservice.application.port.input

data class Table(val name: String)

data class Column(val name: String, val type: String)

interface TableDataQuery {
    fun listAllTables(projectId: String, accountId: String): List<Table>

    fun columnsOfTable(projectId: String, tableName: String, accountId: String): List<Column>

    fun countOfTable(projectId: String, tableName: String, accountId: String): Long
}
