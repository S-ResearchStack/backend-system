package researchstack.backend.adapter.serializer

import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.domain.task.Question
import researchstack.backend.enums.QuestionTag
import researchstack.backend.enums.QuestionType
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class QuestionPolymorphicSerializerTest {
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `QuestionPolymorphicSerializer throws IllegalArgumentException when it received malformed json string`() {
        val question = ""
        assertThrows<IllegalArgumentException> {
            json.decodeFromString<Question>(question)
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `QuestionPolymorphicSerializer throws IllegalArgumentException when the type was unset`() {
        val question =
            """
                {
                    "id": "i",
                    "title": "t",
                    "explanation": "ex",
                    "tag": "RADIO",
                    "required": true,
                    "itemProperties": {"options": [{"value": "v1", "label": "l1"}]}
                }
            """.trimIndent()
        assertThrows<IllegalArgumentException>("failed to read type of question") {
            json.decodeFromString<Question>(question)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `QuestionPolymorphicSerializer should work properly`() {
        var question: Question = json.decodeFromString(
            createQuestionJsonString(
                QuestionTag.TEXT,
                QuestionType.TEXT,
                "{}"
            )
        )
        assertTrue(question.itemProperties is Question.TextProperties)

        question = json.decodeFromString(
            createQuestionJsonString(
                QuestionTag.RADIO,
                QuestionType.CHOICE,
                """{"options": [{"value": "v1", "label": "l1"}]}"""
            )
        )
        assertTrue(question.itemProperties is Question.ChoiceProperties)
        assertContentEquals(
            listOf(Question.Option("v1", "l1")),
            (question.itemProperties as Question.ChoiceProperties).options
        )

        question = json.decodeFromString(
            createQuestionJsonString(
                QuestionTag.SLIDER,
                QuestionType.SCALE,
                """{"low": 0, "high": 1, "lowLabel": "ll", "highLabel": "hl"}"""
            )
        )
        assertTrue(question.itemProperties is Question.ScaleProperties)
        assertEquals(question.itemProperties, Question.ScaleProperties(0, 1, "ll", "hl"))

        question = json.decodeFromString(
            createQuestionJsonString(
                QuestionTag.RANKING,
                QuestionType.RANKING,
                """{"options": [{"value": "v1", "label": "l1"}]}"""
            )
        )
        assertTrue(question.itemProperties is Question.RankingProperties)
        assertContentEquals(
            listOf(Question.Option("v1", "l1")),
            (question.itemProperties as Question.RankingProperties).options
        )

        question = json.decodeFromString(
            createQuestionJsonString(
                QuestionTag.DATETIME,
                QuestionType.DATETIME,
                """{"isTime": true, "isDate":  false, "isRange": true}"""
            )
        )
        assertTrue(question.itemProperties is Question.DateTimeProperties)
        assertEquals(
            question.itemProperties,
            Question.DateTimeProperties(isTime = true, isDate = false, isRange = true)
        )
    }

    fun createQuestionJsonString(tag: QuestionTag, type: QuestionType, itemProperties: String): String {
        return """
            {
                "id": "i",
                "title": "t",
                "explanation": "ex",
                "tag": "$tag",
                "required": true,
                "itemProperties": $itemProperties,
                "type": "$type"
            }
        """.trimIndent()
    }
}
