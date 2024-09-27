package researchstack.backend.adapter.outgoing.mongo.task

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
import researchstack.backend.adapter.outgoing.mongo.repository.SubjectInfoRepository
import researchstack.backend.adapter.outgoing.mongo.repository.TaskResultRepository
import researchstack.backend.domain.subject.SubjectInfo
import researchstack.backend.domain.task.TaskResult
import researchstack.backend.enums.ActivityType
import researchstack.backend.enums.SubjectStatus
import researchstack.backend.enums.TaskType
import java.time.Instant

@ExperimentalCoroutinesApi
internal class CreateTaskResultMongoAdapterTest {
    private val subjectInfoRepository = mockk<SubjectInfoRepository>()
    private val taskResultRepository = mockk<TaskResultRepository>()
    private val adapter = CreateTaskResultMongoAdapter(
        subjectInfoRepository,
        taskResultRepository
    )

    @Test
    @Tag(NEGATIVE_TEST)
    fun `createTaskResult should throw NoSuchElementException when there is no subjectInfo matched with the given studyId and userId`() =
        runTest {
            val userId = "u1"
            val studyId = "s1"
            val taskResult = TaskResult(
                studyId,
                "t1",
                Instant.now().toEpochMilli(),
                Instant.now().plusMillis(3600000).toEpochMilli(),
                TaskType.SURVEY,
                TaskResult.SurveyResult(
                    listOf(
                        TaskResult.SurveyResult.QuestionResult("q", "r")
                    )
                ),
                32400000
            )

            every {
                subjectInfoRepository.findByStudyIdAndSubjectId(studyId, userId)
            } returns Mono.empty()

            assertThrows<NoSuchElementException> {
                adapter.createTaskResult(userId, taskResult)
            }
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `createTaskResult should work properly`() = runTest {
        val userId = "u1"
        val studyId = "s1"
        val taskId = "t1"
        val results = listOf(
            TaskResult.SurveyResult(
                listOf(
                    TaskResult.SurveyResult.QuestionResult("q", "r")
                )
            ),
            TaskResult.ActivityResult(
                ActivityType.TAPPING_SPEED,
                TaskResult.ActivityResult.TappingSpeed("result")
            ),
            TaskResult.ActivityResult(
                ActivityType.REACTION_TIME,
                TaskResult.ActivityResult.ReactionTime("result")
            ),
            TaskResult.ActivityResult(
                ActivityType.GUIDED_BREATHING,
                TaskResult.ActivityResult.GuidedBreathing("result")
            ),
            TaskResult.ActivityResult(
                ActivityType.RANGE_OF_MOTION,
                TaskResult.ActivityResult.RangeOfMotion("result")
            ),
            TaskResult.ActivityResult(
                ActivityType.GAIT_AND_BALANCE,
                TaskResult.ActivityResult.GaitAndBalance("result")
            ),
            TaskResult.ActivityResult(
                ActivityType.STROOP_TEST,
                TaskResult.ActivityResult.StroopTest("result")
            ),
            TaskResult.ActivityResult(
                ActivityType.SPEECH_RECOGNITION,
                TaskResult.ActivityResult.SpeechRecognition("result")
            ),
            TaskResult.ActivityResult(
                ActivityType.MOBILE_SPIROMETRY,
                TaskResult.ActivityResult.MobileSpirometry("filePath")
            ),
            TaskResult.ActivityResult(
                ActivityType.SUSTAINED_PHONATION,
                TaskResult.ActivityResult.SustainedPhonation("result")
            ),
            TaskResult.ActivityResult(
                ActivityType.FIVE_METER_WALK_TEST,
                TaskResult.ActivityResult.FiveMeterWalkTest("2024-04-01T16:36:44+09:00", "2024-04-26T04:18:00+09:00")
            ),
            TaskResult.ActivityResult(
                ActivityType.STATE_BALANCE_TEST,
                TaskResult.ActivityResult.StateBalanceTest("2024-04-01T16:36:44+09:00", "2024-04-26T04:18:00+09:00")
            ),
            TaskResult.ActivityResult(
                ActivityType.ROMBERG_TEST,
                TaskResult.ActivityResult.RombergTest("2024-04-01T16:36:44+09:00", "2024-04-26T04:18:00+09:00")
            ),
            TaskResult.ActivityResult(
                ActivityType.SIT_TO_STAND,
                TaskResult.ActivityResult.SitToStand("2024-04-01T16:36:44+09:00", "2024-04-26T04:18:00+09:00")
            ),
            TaskResult.ActivityResult(
                ActivityType.ORTHOSTATIC_BP,
                TaskResult.ActivityResult.OrthostaticBp("2024-04-01T16:36:44+09:00", "2024-04-26T04:18:00+09:00")
            ),
            TaskResult.ActivityResult(
                ActivityType.BIA_MEASUREMENT,
                TaskResult.ActivityResult.BiaMeasurement("2024-04-01T16:36:44+09:00", "2024-04-26T04:18:00+09:00")
            ),
            TaskResult.ActivityResult(
                ActivityType.BP_MEASUREMENT,
                TaskResult.ActivityResult.BpMeasurement("2024-04-01T16:36:44+09:00", "2024-04-26T04:18:00+09:00")
            ),
            TaskResult.ActivityResult(
                ActivityType.ECG_MEASUREMENT,
                TaskResult.ActivityResult.EcgMeasurement("2024-04-01T16:36:44+09:00", "2024-04-26T04:18:00+09:00")
            ),
            TaskResult.ActivityResult(
                ActivityType.PPG_MEASUREMENT,
                TaskResult.ActivityResult.PpgMeasurement("2024-04-01T16:36:44+09:00", "2024-04-26T04:18:00+09:00")
            ),
            TaskResult.ActivityResult(
                ActivityType.SPO2_MEASUREMENT,
                TaskResult.ActivityResult.Spo2Measurement("2024-04-01T16:36:44+09:00", "2024-04-26T04:18:00+09:00")
            ),
            TaskResult.ActivityResult(
                ActivityType.BP_AND_BIA_MEASUREMENT,
                TaskResult.ActivityResult.BpAndBiaMeasurement("2024-04-01T16:36:44+09:00", "2024-04-26T04:18:00+09:00")
            ),
            TaskResult.ActivityResult(
                ActivityType.STABLE_MEASUREMENT,
                TaskResult.ActivityResult.StableMeasurement("2024-04-01T16:36:44+09:00", "2024-04-26T04:18:00+09:00")
            ),
            TaskResult.ActivityResult(
                ActivityType.SHAPE_PAINTING,
                TaskResult.ActivityResult.ShapePainting("result")
            ),
            TaskResult.ActivityResult(
                ActivityType.CATCH_LADYBUG,
                TaskResult.ActivityResult.CatchLadybug("result")
            ),
            TaskResult.ActivityResult(
                ActivityType.MEMORIZE,
                TaskResult.ActivityResult.Memorize("result")
            ),
            TaskResult.ActivityResult(
                ActivityType.MEMORIZE_WORDS_START,
                TaskResult.ActivityResult.MemorizeWordsStart("result")
            ),
            TaskResult.ActivityResult(
                ActivityType.MEMORIZE_WORDS_END,
                TaskResult.ActivityResult.MemorizeWordsEnd("result")
            ),
            TaskResult.ActivityResult(
                ActivityType.DESCRIBE_IMAGE,
                TaskResult.ActivityResult.DescribeImage("result")
            ),
            TaskResult.ActivityResult(
                ActivityType.READ_TEXT_ALOUD,
                TaskResult.ActivityResult.ReadTextAloud("result")
            ),
            TaskResult.ActivityResult(
                ActivityType.ANSWER_VERBALLY,
                TaskResult.ActivityResult.AnswerVerbally("result")
            ),
            TaskResult.ActivityResult(
                ActivityType.ANSWER_WRITTEN,
                TaskResult.ActivityResult.AnswerWritten("result")
            )
        )

        every {
            subjectInfoRepository.findByStudyIdAndSubjectId(studyId, userId)
        } answers {
            SubjectInfo(studyId, "sub1", SubjectStatus.PARTICIPATING, userId).toEntity().toMono()
        }

        every {
            taskResultRepository.existsByStudyIdAndTaskIdAndSubjectId(studyId, taskId, userId)
        } answers {
            false.toMono()
        }

        results.forEach {
            val taskResult = TaskResult(
                studyId,
                "t1",
                Instant.now().toEpochMilli(),
                Instant.now().plusMillis(3600000).toEpochMilli(),
                if (it is TaskResult.SurveyResult) {
                    TaskType.SURVEY
                } else {
                    TaskType.ACTIVITY
                },
                it,
                32400000
            )
            val taskResultEntity = taskResult.toEntity(userId)

            every {
                taskResultRepository.save(any())
            } returns taskResultEntity.toMono()

            assertDoesNotThrow {
                adapter.createTaskResult(userId, taskResult)
            }
        }
    }
}
