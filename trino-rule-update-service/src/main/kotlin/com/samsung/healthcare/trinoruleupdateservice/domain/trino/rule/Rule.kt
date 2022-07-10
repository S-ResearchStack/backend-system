package com.samsung.healthcare.trinoruleupdateservice.domain.trino.rule

import com.samsung.healthcare.trinoruleupdateservice.domain.accountservice.Role
import com.samsung.healthcare.trinoruleupdateservice.domain.accountservice.User

data class Rule(
    val catalogs: List<Catalog>? = null,
    val schemas: String? = null,
    val tables: List<Table>? = null,
    val columns: String? = null,
    // TODO: add 'systemSessionProperties' field
    // TODO: add 'catalogSessionnProperties' field
) {
    companion object {
        fun newRule(users: List<User>, dbPrefix: String = "project_", dbPostfix: String = "_research"): Rule {
            val catalogs = mutableListOf<Catalog>()
            catalogs.add(Catalog(user = "admin", catalog = ".*", allow = "all"))
            catalogs.add(Catalog(catalog = "postgresql", allow = "all"))
            catalogs.add(Catalog(catalog = "system", allow = "none"))
            val tables = mutableListOf<Table>()
            users.forEach { user ->
                user.roles.forEach { roleStr ->
                    val role = Role.newRole(roleStr)
                    when (role.position) {
                        "project-owner", "head-researcher", "researcher" ->
                            tables.add(
                                Table(
                                    user = user.id,
                                    catalog = "postgresql",
                                    schema = "$dbPrefix${role.projectId}$dbPostfix",
                                    table = ".*",
                                    privileges = listOf("SELECT"),
                                )
                            )
                    }
                }
            }
            tables.add(Table(user = ".*", privileges = listOf()))

            return Rule(catalogs = catalogs, tables = tables)
        }
    }
}
