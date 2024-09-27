package researchstack.backend.domain.education

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import researchstack.backend.EducationTestUtil
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.enums.EducationalContentStatus
import researchstack.backend.enums.EducationalContentType
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
internal class EducationalContentTest {
    private val educationalContent = EducationTestUtil.getEducationalContent()

    @Test
    @Tag(POSITIVE_TEST)
    fun `new should return a new EducationalContent instance with default values`() = runTest {
        val newEducationalContent = educationalContent.new()

        assertEquals(newEducationalContent.id, educationalContent.id)
        assertEquals(newEducationalContent.title, educationalContent.title)
        assertEquals(newEducationalContent.type, educationalContent.type)
        assertEquals(newEducationalContent.status, educationalContent.status)
        assertEquals(newEducationalContent.category, educationalContent.category)
        assertEquals(newEducationalContent.creatorId, educationalContent.creatorId)
        assertEquals(newEducationalContent.publisherId, educationalContent.publisherId)
        assertEquals(newEducationalContent.modifierId, educationalContent.modifierId)
        assertEquals(newEducationalContent.publishedAt, educationalContent.publishedAt)
        assertEquals(newEducationalContent.modifiedAt, educationalContent.modifiedAt)
        assertEquals(newEducationalContent.content, educationalContent.content)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `new should update specific fields with provided values`() = runTest {
        val testTime = LocalDateTime.now()
        val updatedPublishedAt = testTime.plusDays(1)
        val updatedModifiedAt = testTime.plusDays(2)
        val newEducationalContent = educationalContent.new(
            id = "newId",
            title = "newTitle",
            type = EducationalContentType.VIDEO,
            status = EducationalContentStatus.DRAFT,
            category = "newCategory",
            creatorId = "newCreatorId",
            publisherId = "newPublisherId",
            modifierId = "newModifierId",
            publishedAt = updatedPublishedAt,
            modifiedAt = updatedModifiedAt,
            content = EducationalContent.VideoContent(url = "newUrl", description = "newDescription")
        )

        assertEquals(newEducationalContent.id, "newId")
        assertEquals(newEducationalContent.title, "newTitle")
        assertEquals(newEducationalContent.type, EducationalContentType.VIDEO)
        assertEquals(newEducationalContent.status, EducationalContentStatus.DRAFT)
        assertEquals(newEducationalContent.category, "newCategory")
        assertEquals(newEducationalContent.creatorId, "newCreatorId")
        assertEquals(newEducationalContent.publisherId, "newPublisherId")
        assertEquals(newEducationalContent.modifierId, "newModifierId")
        assertEquals(newEducationalContent.publishedAt, updatedPublishedAt)
        assertEquals(newEducationalContent.modifiedAt, updatedModifiedAt)
        assertEquals(
            newEducationalContent.content,
            EducationalContent.VideoContent(url = "newUrl", description = "newDescription")
        )
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["UNKNOWN"])
    fun `new should throw IllegalArgumentException if type is UNKNOWN`(type: String) = runTest {
        assertThrows<IllegalArgumentException> {
            educationalContent.new(type = EducationalContentType.valueOf(type))
        }
    }

    @ParameterizedTest
    @Tag(NEGATIVE_TEST)
    @ValueSource(strings = ["UNKNOWN"])
    fun `new should throw IllegalArgumentException if status is UNKNOWN`(status: String) = runTest {
        assertThrows<IllegalArgumentException> {
            educationalContent.new(status = EducationalContentStatus.valueOf(status))
        }
    }
}
