package researchstack.backend.adapter.outgoing.mongo.mapper

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.backend.EducationTestUtil
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.incoming.rest.common.JsonHandler
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class EducationalContentMapperTest {
    @Test
    @Tag(POSITIVE_TEST)
    fun `should map EducationalContent to EducationalContentEntity`() = runTest {
        val educationalContent = EducationTestUtil.getEducationalContent()
        val result = educationalContent.toEntity()

        assertEquals(educationalContent.title, result.title)
        assertEquals(educationalContent.type, result.type)
        assertEquals(educationalContent.status, result.status)
        assertEquals(educationalContent.category, result.category)
        assertEquals(educationalContent.creatorId, result.creatorId)
        assertEquals(JsonHandler.toJson(educationalContent.content), JsonHandler.toJson(result.content))
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should map EducationalContentEntity to EducationalContent`() = runTest {
        val entity = EducationTestUtil.getEducationalContentEntity()
        val result = entity.toDomain()

        assertEquals(entity.title, result.title)
        assertEquals(entity.type, result.type)
        assertEquals(entity.status, result.status)
        assertEquals(entity.category, result.category)
        assertEquals(entity.creatorId, result.creatorId)
        assertEquals(JsonHandler.toJson(entity.content), JsonHandler.toJson(result.content))
    }
}
