package com.samsung.healthcare.trinoruleupdateservice.domain.trino.rule

import com.samsung.healthcare.trinoruleupdateservice.POSITIVE_TEST
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

internal class SchemaTest {
    @Test
    @Tag(POSITIVE_TEST)
    fun `methods of Schema should work properly`() {
        val userString = "some-user"
        val roleString = "some-role"
        val groupString = "some-group"
        val catalogString = "some-catalog"
        val schemaString = "some-schema"
        val isOwner = false
        val schema = Schema(userString, roleString, groupString, catalogString, schemaString, isOwner)

        Assertions.assertEquals(userString, schema.user)
        Assertions.assertEquals(roleString, schema.role)
        Assertions.assertEquals(groupString, schema.group)
        Assertions.assertEquals(catalogString, schema.catalog)
        Assertions.assertEquals(schemaString, schema.schema)
        Assertions.assertEquals(isOwner, schema.owner)
    }
}
