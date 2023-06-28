package com.samsung.healthcare.trinoruleupdateservice.domain.trino.rule

import com.samsung.healthcare.trinoruleupdateservice.application.config.ApplicationProperties
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
        private val systemCatalog = Catalog(catalog = "system", allow = "none")

        private val defaultSchema = Schema(user = ALL, schema = ALL, owner = false)

        fun newRule(users: List<User>, config: ApplicationProperties): Rule {
            val originalDbCatalog = Catalog(catalog = config.trino.catalogs.originalDb.name, allow = "read-only")
            val deIdentifiedDbCatalog =
                Catalog(catalog = config.trino.catalogs.deIdentifiedDb.name, allow = "read-only")

            val catalogs = mutableListOf(adminCatalog, originalDbCatalog, deIdentifiedDbCatalog, systemCatalog)
            val schemas = mutableListOf(defaultSchema)

            val tables = mutableListOf<Table>()

            users.forEach { user ->
                user.roles.forEach { roleStr ->
                    val role = Role.newRole(roleStr)
                    if (role.position in listOf(
                            "study-creator",
                            "principal-investigator",
                            "research-assistant",
                            "data-scientist"
                        )
                    ) {
                        tables.add(
                            Table(
                                user = user.id,
                                catalog =
                                if (role.position == "data-scientist") deIdentifiedDbCatalog.catalog
                                else originalDbCatalog.catalog,
                                schema =
                                "${config.databaseConfig.prefix}${role.projectId}${config.databaseConfig.postfix}",
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
