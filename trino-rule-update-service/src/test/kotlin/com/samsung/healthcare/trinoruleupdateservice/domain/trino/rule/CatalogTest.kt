package com.samsung.healthcare.trinoruleupdateservice.domain.trino.rule

import com.samsung.healthcare.trinoruleupdateservice.POSITIVE_TEST
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

internal class CatalogTest {
    @Test
    @Tag(POSITIVE_TEST)
    fun `methods of Catalog should work properly`() {
        val userString = "some-user"
        val roleString = "some-role"
        val groupString = "some-group"
        val catalogString = "some-catalog"
        val allowString = "some-allow"
        val catalog = Catalog(userString, roleString, groupString, catalogString, allowString)

        assertEquals(userString, catalog.user)
        assertEquals(roleString, catalog.role)
        assertEquals(groupString, catalog.group)
        assertEquals(catalogString, catalog.catalog)
        assertEquals(allowString, catalog.allow)
    }
}
