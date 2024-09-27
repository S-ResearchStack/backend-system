package researchstack.backend.adapter.outgoing.mongo.study.participationrequirement

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.mongo.mapper.toEntity
import researchstack.backend.adapter.outgoing.mongo.repository.ParticipationRequirementRepository
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
import java.time.LocalDate
import java.time.LocalTime

@ExperimentalCoroutinesApi
internal class GetParticipationRequirementMongoAdapterTest {
    private val repository = mockk<ParticipationRequirementRepository>()
    private val adapter = GetParticipationRequirementMongoAdapter(repository)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getParticipationRequirement should throw NoSuchElementException when there is no participationRequirement matched with the study id`() =
        runTest {
            val studyId = "s1"

            every {
                repository.findById(studyId)
            } returns Mono.empty()

            assertThrows<NoSuchElementException> {
                adapter.getParticipationRequirement(studyId)
            }
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getParticipationRequirement should work properly`() = runTest {
        val studyId = "s1"
        val requirementEntity = createParticipationRequirement().toEntity(studyId)

        every {
            repository.findById(studyId)
        } returns requirementEntity.toMono()

        assertDoesNotThrow {
            adapter.getParticipationRequirement(studyId)
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getEligibilityTest should throw NoSuchElementException when there is no participationRequirement matched with the study id`() =
        runTest {
            val studyId = "s1"

            every {
                repository.findById(studyId)
            } returns Mono.empty()

            assertThrows<NoSuchElementException> {
                adapter.getEligibilityTest(studyId)
            }
        }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getEligibilityTest should throw NoSuchElementException when the eligibilityTest is null`() = runTest {
        val studyId = "s1"
        val requirementEntity = ParticipationRequirement(
            null,
            InformedConsent("http://example.com/image.png"),
            listOf(),
            null
        ).toEntity(studyId)

        every {
            repository.findById(studyId)
        } returns requirementEntity.toMono()

        assertThrows<NoSuchElementException> {
            adapter.getEligibilityTest(studyId)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getEligibilityTest should work properly`() = runTest {
        val studyId = "s1"
        val requirementEntity = createParticipationRequirement().toEntity(studyId)

        every {
            repository.findById(studyId)
        } returns requirementEntity.toMono()

        assertDoesNotThrow {
            adapter.getEligibilityTest(studyId)
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getInformedConsent should throw NoSuchElementException when there is no participationRequirement matched with the study id`() =
        runTest {
            val studyId = "s1"

            every {
                repository.findById(studyId)
            } returns Mono.empty()

            assertThrows<NoSuchElementException> {
                adapter.getInformedConsent(studyId)
            }
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getInformedConsent should work properly`() = runTest {
        val studyId = "s1"
        val requirementEntity = createParticipationRequirement().toEntity(studyId)

        every {
            repository.findById(studyId)
        } returns requirementEntity.toMono()

        assertDoesNotThrow {
            adapter.getInformedConsent(studyId)
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getRequiredHealthDataTypeList should throw NoSuchElementException when there is no participationRequirement matched with the study id`() =
        runTest {
            val studyId = "s1"

            every {
                repository.findById(studyId)
            } returns Mono.empty()

            assertThrows<NoSuchElementException> {
                adapter.getRequiredHealthDataTypeList(studyId)
            }
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getRequiredHealthDataTypeList should work properly`() = runTest {
        val studyId = "s1"
        val requirementEntity = createParticipationRequirement().toEntity(studyId)

        every {
            repository.findById(studyId)
        } returns requirementEntity.toMono()

        assertDoesNotThrow {
            adapter.getRequiredHealthDataTypeList(studyId)
        }
    }

    private fun createParticipationRequirement(): ParticipationRequirement {
        return ParticipationRequirement(
            EligibilityTest(
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
                                ),
                                Question(
                                    "q2",
                                    "q2 title",
                                    "q2 ex",
                                    QuestionTag.DATETIME,
                                    Question.DateTimeProperties(
                                        isTime = true,
                                        isDate = false,
                                        isRange = true
                                    ),
                                    false,
                                    QuestionType.DATETIME
                                ),
                                Question(
                                    "q3",
                                    "q3 title",
                                    "q3 ex",
                                    QuestionTag.RANKING,
                                    Question.RankingProperties(
                                        listOf(
                                            Question.Option("a", "al"),
                                            Question.Option("b", "bl")
                                        )
                                    ),
                                    true,
                                    QuestionType.RANKING
                                ),
                                Question(
                                    "q4",
                                    "q4 title",
                                    "q4 ex",
                                    QuestionTag.TEXT,
                                    Question.TextProperties(),
                                    true,
                                    QuestionType.TEXT
                                )
                            )
                        )
                    )
                ),
                listOf(
                    EligibilityTest.Answer(
                        "q0",
                        QuestionType.SCALE,
                        EligibilityTest.ScaleAnswer(10, 20)
                    ),
                    EligibilityTest.Answer(
                        "q1",
                        QuestionType.CHOICE,
                        EligibilityTest.ChoiceAnswer(
                            listOf(EligibilityTest.ChoiceAnswer.Option("b", "bl"))
                        )
                    ),
                    EligibilityTest.Answer(
                        "q2",
                        QuestionType.DATETIME,
                        EligibilityTest.DateTimeAnswer(
                            LocalDate.now(),
                            LocalDate.now().plusDays(1),
                            LocalTime.now(),
                            LocalTime.now().plusHours(1)
                        )
                    ),
                    EligibilityTest.Answer(
                        "q3",
                        QuestionType.RANKING,
                        EligibilityTest.RankingAnswer(listOf("b"))
                    ),
                    EligibilityTest.Answer(
                        "q4",
                        QuestionType.TEXT,
                        EligibilityTest.TextAnswer(listOf("ans"))
                    )
                )
            ),
            InformedConsent("http://example.com/image.png"),
            listOf(HealthDataType.WEIGHT, HealthDataType.HEART_RATE),
            listOf(
                DataSpec(
                    "WatchSpO2Raw",
                    "WatchSpO2Raw",
                    "WatchSpO2Raw",
                    "Spot Check",
                    10,
                    10
                )
            )
        )
    }
}
