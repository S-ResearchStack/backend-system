package researchstack.backend.application.service.mapper

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.application.port.incoming.study.CreateParticipationRequirementCommand
import researchstack.backend.domain.study.DataSpec
import researchstack.backend.domain.study.EligibilityTest
import researchstack.backend.domain.study.InformedConsent
import researchstack.backend.domain.study.ParticipationRequirement
import researchstack.backend.domain.task.Question
import researchstack.backend.domain.task.Section
import researchstack.backend.domain.task.SurveyTask
import researchstack.backend.enums.HealthDataType
import researchstack.backend.enums.QuestionTag
import researchstack.backend.enums.QuestionType
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@ExperimentalSerializationApi
class ParticipationRequirementMapperTest {
    @Test
    @Tag(NEGATIVE_TEST)
    fun `mapQuestion throws IllegalArgumentException when it received unsupported survey type`() {
        assertThrows<IllegalArgumentException> {
            ParticipationRequirementMapper.INSTANCE.mapQuestion(
                CreateParticipationRequirementCommand.EligibilityTest.Section.Question(
                    "",
                    "",
                    "",
                    QuestionTag.TEXT,
                    true,
                    QuestionType.UNSPECIFIED,
                    mapOf()
                )
            )
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `mapAnswer throws IllegalArgumentException when it received unsupported survey type`() {
        assertThrows<IllegalArgumentException> {
            ParticipationRequirementMapper.INSTANCE.mapAnswer(
                CreateParticipationRequirementCommand.EligibilityTest.Answer("", QuestionType.UNSPECIFIED, mapOf())
            )
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `mapQuestion throws MissingFieldException when it failed to receive required fields from properties`() {
        assertThrows<MissingFieldException> {
            ParticipationRequirementMapper.INSTANCE.mapQuestion(
                CreateParticipationRequirementCommand.EligibilityTest.Section.Question(
                    "",
                    "",
                    "",
                    QuestionTag.SLIDER,
                    true,
                    QuestionType.SCALE,
                    mapOf()
                )
            )
        }
        assertThrows<MissingFieldException> {
            ParticipationRequirementMapper.INSTANCE.mapQuestion(
                CreateParticipationRequirementCommand.EligibilityTest.Section.Question(
                    "",
                    "",
                    "",
                    QuestionTag.RADIO,
                    true,
                    QuestionType.CHOICE,
                    mapOf()
                )
            )
        }
        assertThrows<MissingFieldException> {
            ParticipationRequirementMapper.INSTANCE.mapQuestion(
                CreateParticipationRequirementCommand.EligibilityTest.Section.Question(
                    "",
                    "",
                    "",
                    QuestionTag.CHECKBOX,
                    true,
                    QuestionType.CHOICE,
                    mapOf()
                )
            )
        }
        assertThrows<MissingFieldException> {
            ParticipationRequirementMapper.INSTANCE.mapQuestion(
                CreateParticipationRequirementCommand.EligibilityTest.Section.Question(
                    "",
                    "",
                    "",
                    QuestionTag.IMAGE,
                    true,
                    QuestionType.CHOICE,
                    mapOf()
                )
            )
        }
        assertThrows<MissingFieldException> {
            ParticipationRequirementMapper.INSTANCE.mapQuestion(
                CreateParticipationRequirementCommand.EligibilityTest.Section.Question(
                    "",
                    "",
                    "",
                    QuestionTag.DROPDOWN,
                    true,
                    QuestionType.CHOICE,
                    mapOf()
                )
            )
        }
        assertThrows<MissingFieldException> {
            ParticipationRequirementMapper.INSTANCE.mapQuestion(
                CreateParticipationRequirementCommand.EligibilityTest.Section.Question(
                    "",
                    "",
                    "",
                    QuestionTag.DATETIME,
                    true,
                    QuestionType.DATETIME,
                    mapOf()
                )
            )
        }
        assertThrows<MissingFieldException> {
            ParticipationRequirementMapper.INSTANCE.mapQuestion(
                CreateParticipationRequirementCommand.EligibilityTest.Section.Question(
                    "",
                    "",
                    "",
                    QuestionTag.RANKING,
                    true,
                    QuestionType.RANKING,
                    mapOf()
                )
            )
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `mapAnswer throws MissingFieldException when it failed to receive required fields from properties`() {
        assertThrows<MissingFieldException> {
            ParticipationRequirementMapper.INSTANCE.mapAnswer(
                CreateParticipationRequirementCommand.EligibilityTest.Answer("", QuestionType.SCALE, mapOf())
            )
        }
        assertThrows<MissingFieldException> {
            ParticipationRequirementMapper.INSTANCE.mapAnswer(
                CreateParticipationRequirementCommand.EligibilityTest.Answer("", QuestionType.CHOICE, mapOf())
            )
        }
        assertThrows<MissingFieldException> {
            ParticipationRequirementMapper.INSTANCE.mapAnswer(
                CreateParticipationRequirementCommand.EligibilityTest.Answer("", QuestionType.DATETIME, mapOf())
            )
        }
        assertThrows<MissingFieldException> {
            ParticipationRequirementMapper.INSTANCE.mapAnswer(
                CreateParticipationRequirementCommand.EligibilityTest.Answer("", QuestionType.TEXT, mapOf())
            )
        }
        assertThrows<MissingFieldException> {
            ParticipationRequirementMapper.INSTANCE.mapAnswer(
                CreateParticipationRequirementCommand.EligibilityTest.Answer("", QuestionType.RANKING, mapOf())
            )
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `mapQuestion ignores unknown fields from properties`() {
        assertDoesNotThrow {
            ParticipationRequirementMapper.INSTANCE.mapQuestion(
                CreateParticipationRequirementCommand.EligibilityTest.Section.Question(
                    "",
                    "",
                    "",
                    QuestionTag.TEXT,
                    true,
                    QuestionType.TEXT,
                    mapOf(
                        "a" to listOf("a1", "a2", "a3"),
                        "b" to 123,
                        "c" to 1.23,
                        "d" to true,
                        "e" to "str"
                    )
                )
            )
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should map CreateParticipationRequirementCommand to ParticipationRequirement domain`() = runTest {
        val command = CreateParticipationRequirementCommand(
            informedConsent = CreateParticipationRequirementCommand.InformedConsent(imagePath = "example.com/test"),
            healthDataTypeList = listOf(HealthDataType.HEART_RATE, HealthDataType.WEIGHT),
            eligibilityTest = CreateParticipationRequirementCommand.EligibilityTest(
                sections = listOf(
                    CreateParticipationRequirementCommand.EligibilityTest.Section(
                        listOf(
                            CreateParticipationRequirementCommand.EligibilityTest.Section.Question(
                                "q0",
                                "q0 title",
                                "q0 ex",
                                QuestionTag.SLIDER,
                                true,
                                QuestionType.SCALE,
                                mapOf(
                                    "low" to 0,
                                    "high" to 1100,
                                    "lowLabel" to "ll",
                                    "highLabel" to "hl"
                                )
                            ),
                            CreateParticipationRequirementCommand.EligibilityTest.Section.Question(
                                "q1",
                                "q1 title",
                                "q1 ex",
                                QuestionTag.RADIO,
                                true,
                                QuestionType.CHOICE,
                                mapOf(
                                    "options" to listOf(
                                        Question.Option("a", "al"),
                                        Question.Option("b", "bl")
                                    )
                                )
                            )
                        )
                    )
                ),
                answers = listOf(
                    CreateParticipationRequirementCommand.EligibilityTest.Answer(
                        questionId = "q0",
                        type = QuestionType.SCALE,
                        properties = mapOf(
                            "from" to 10,
                            "to" to 20
                        )
                    ),
                    CreateParticipationRequirementCommand.EligibilityTest.Answer(
                        questionId = "q1",
                        type = QuestionType.CHOICE,
                        properties = mapOf(
                            "options" to listOf(
                                Question.Option("b", "bl")
                            )
                        )
                    )
                )
            ),
            taskInfo = listOf(
                CreateParticipationRequirementCommand.DataSpec(
                    "WatchSpO2Raw",
                    "WatchSpO2Raw",
                    "WatchSpO2Raw",
                    "Spot Check",
                    10,
                    10
                )
            )
        )

        val expected = ParticipationRequirement(
            eligibilityTest = EligibilityTest(
                SurveyTask(
                    listOf(
                        Section(
                            listOf(
                                Question(
                                    "q0",
                                    "q0 title",
                                    "q0 ex",
                                    QuestionTag.SLIDER,
                                    Question.ScaleProperties(0, 1100, "ll", "hl"),
                                    true,
                                    QuestionType.SCALE
                                ),
                                Question(
                                    "q1",
                                    "q1 title",
                                    "q1 ex",
                                    QuestionTag.RADIO,
                                    Question.ChoiceProperties(
                                        listOf(
                                            Question.Option("a", "al"),
                                            Question.Option("b", "bl")
                                        )
                                    ),
                                    true,
                                    QuestionType.CHOICE
                                )
                            )
                        )
                    )
                ),
                listOf(
                    EligibilityTest.Answer("q0", QuestionType.SCALE, EligibilityTest.ScaleAnswer(10, 20)),
                    EligibilityTest.Answer(
                        "q1",
                        QuestionType.CHOICE,
                        EligibilityTest.ChoiceAnswer(
                            listOf(EligibilityTest.ChoiceAnswer.Option("b", "bl"))
                        )
                    )
                )
            ),
            informedConsent = InformedConsent(imagePath = "example.com/test"),
            healthDataTypeList = listOf(HealthDataType.HEART_RATE, HealthDataType.WEIGHT),
            taskInfo = listOf(DataSpec("WatchSpO2Raw", "WatchSpO2Raw", "WatchSpO2Raw", "Spot Check", 10, 10))
        )

        val ret = command.toDomain()
        assertContentEquals(expected.healthDataTypeList, ret.healthDataTypeList)
        assertEquals(expected.informedConsent, ret.informedConsent)
        assertContentEquals(expected.eligibilityTest?.surveyTask?.sections, ret.eligibilityTest?.surveyTask?.sections)
        assertEquals(expected.taskInfo, ret.taskInfo)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should map CreateParticipationRequirementCommand to ParticipationRequirement domain without EligibilityTest and TaskInfo`() {
        val command = CreateParticipationRequirementCommand(
            informedConsent = CreateParticipationRequirementCommand.InformedConsent(imagePath = "example.com/test"),
            healthDataTypeList = listOf(HealthDataType.HEART_RATE, HealthDataType.WEIGHT),
            eligibilityTest = null,
            taskInfo = null
        )

        val ret = command.toDomain()
        assertContentEquals(
            listOf(HealthDataType.HEART_RATE, HealthDataType.WEIGHT),
            ret.healthDataTypeList
        )
        assertEquals(InformedConsent(imagePath = "example.com/test"), ret.informedConsent)
        assertEquals(null, ret.eligibilityTest)
        assertEquals(null, ret.taskInfo)
    }
}
