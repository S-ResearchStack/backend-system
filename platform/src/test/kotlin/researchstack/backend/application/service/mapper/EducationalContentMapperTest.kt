package researchstack.backend.application.service.mapper

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
    fun `should map CreateEducationalContentCommand to domain EducationalContent`() = runTest {
        val creatorId = EducationTestUtil.userId

        val command = EducationTestUtil.getCreateEducationalContentCommand()
        val result = command.toDomain(EducationTestUtil.userId)

        assertEquals(creatorId, result.creatorId)
        assertEquals(command.title, result.title)
        assertEquals(command.type, result.type)
        assertEquals(command.status, result.status)
        assertEquals(command.category, result.category)
        assertEquals(JsonHandler.toJson(command.content), JsonHandler.toJson(result.content))
    }
}
