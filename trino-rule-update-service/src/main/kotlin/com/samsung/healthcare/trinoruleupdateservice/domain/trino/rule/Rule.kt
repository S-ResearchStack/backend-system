package com.samsung.healthcare.trinoruleupdateservice.domain.trino.rule

import com.samsung.healthcare.trinoruleupdateservice.domain.accountservice.Role
import com.samsung.healthcare.trinoruleupdateservice.domain.accountservice.User

data class Rule(
    val catalogs: List<Catalog>? = null,
    val schemas: List<Schema>? = null,
    val tables: List<Table>? = null,
    val columns: String? = null,
    // TODO: add 'systemSessionProperties' field
    // TODO: add 'catalogSessionnProperties' field
) {
    companion object {
        internal const val ALL = ".*"
        internal const val SELECT_PRIVILEGE = "SELECT"
        private val adminCatalog = Catalog(user = "admin", catalog = ALL, allow = "all")
        private val postgresCatalog = Catalog(catalog = "postgresql", allow = "read-only")
        private val systemCatalog = Catalog(catalog = "system", allow = "none")

        private val defaultSchema = Schema(user = ALL, schema = ALL, owner = false)

        fun newRule(users: List<User>, dbPrefix: String = "project_", dbPostfix: String = "_research"): Rule {
            val catalogs = mutableListOf(adminCatalog, postgresCatalog, systemCatalog)
            val schemas = mutableListOf(defaultSchema)

            val tables = mutableListOf<Table>()

            users.forEach { user ->
                user.roles.forEach { roleStr ->
                    val role = Role.newRole(roleStr)
                    if (role.position in listOf("project-owner", "head-researcher", "researcher")) {
                        tables.add(
                            Table(
                                user = user.id,
                                catalog = postgresCatalog.catalog,
                                schema = "$dbPrefix${role.projectId}$dbPostfix",
                                table = ALL,
                                privileges = listOf(SELECT_PRIVILEGE),
                            )
                        )
                    }
                }
            }
            tables.add(Table(user = ALL, privileges = emptyList()))

            return Rule(catalogs = catalogs, schemas = schemas, tables = tables)
        }
    }
}
