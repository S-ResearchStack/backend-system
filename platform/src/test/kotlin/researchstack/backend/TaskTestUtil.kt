package researchstack.backend

import researchstack.backend.application.port.incoming.task.CreateTaskSpecCommand
import researchstack.backend.application.port.incoming.task.Question
import researchstack.backend.application.port.incoming.task.Section
import researchstack.backend.application.port.incoming.task.SurveyTaskResponse
import researchstack.backend.application.port.incoming.task.TaskSpecResponse
import researchstack.backend.application.port.incoming.task.UpdateTaskSpecCommand
import researchstack.backend.domain.task.TaskSpec
import researchstack.backend.enums.QuestionTag
import researchstack.backend.enums.TaskStatus
import researchstack.backend.enums.TaskType
import java.time.LocalDateTime

class TaskTestUtil {
    companion object {
        private val now = LocalDateTime.now()
        private const val taskId = "mentalSurveyTask"
        private const val title = "basic survey for mentalcare study"
        private const val description = "conduct survey for mentalcare study"
        private val status = TaskStatus.PUBLISHED
        private const val schedule = "0 0 0 * * ? *"
        private val startTime = now
        private val endTime = now.plusDays(20)
        private const val validMin = 1440L
        private const val duration = "takes 5 minutes"
        private const val iconUrl = "http://www.test.com"
        private const val inClinic = true
        private val taskType = TaskType.SURVEY
        private val task = mapOf(
            "sections" to listOf(
                Section(
                    listOf(
                        Question(
                            "1",
                            "1 title",
                            "1 explanation",
                            QuestionTag.SLIDER,
                            Question.ScaleProperties(
                                0,
                                10,
                                "low 0",
                                "high 10"
                            ),
                            true
                        ),
                        Question(
                            "2",
                            "2 title",
                            "2 explanation",
                            QuestionTag.TEXT,
                            Question.TextProperties(),
                            true
                        ),
                        Question(
                            "3",
                            "3 title",
                            "3 explanation",
                            QuestionTag.RADIO,
                            Question.ChoiceProperties(
                                listOf(
                                    Question.Option(
                                        "yes",
                                        "yes"
                                    ),
                                    Question.Option(
                                        "no",
                                        "no"
                                    )
                                )
                            ),
                            true
                        ),
                        Question(
                            "4",
                            "4 title",
                            "4 explanation",
                            QuestionTag.DATETIME,
                            Question.DateTimeProperties(
                                true,
                                true,
                                true
                            ),
                            true
                        )
                    )
                )
            )
        )

        private val surveyTask = SurveyTaskResponse(
            listOf(
                Section(
                    listOf(
                        Question(
                            id = "1",
                            title = "How old are you?",
                            explanation = "aa",
                            tag = QuestionTag.SLIDER,
                            itemProperties = Question.ScaleProperties(
                                0,
                                1100,
                                "ll",
                                "hl"
                            ),
                            required = true
                        )
                    )
                )
            )
        )
        private const val studyId = "mentalCareStudy"

        private val taskSpecResponse = TaskSpecResponse(
            id = taskId,
            studyId = studyId,
            title = title,
            description = description,
            schedule = schedule,
            startTime = startTime,
            endTime = endTime,
            validMin = validMin,
            duration = duration,
            iconUrl = iconUrl,
            inClinic = inClinic,
            taskType = taskType,
            task = surveyTask
        )

        fun createCreateTaskSpecCommand(schedule: String): CreateTaskSpecCommand = CreateTaskSpecCommand(
            title,
            description,
            status,
            schedule,
            startTime,
            endTime,
            validMin,
            duration,
            iconUrl,
            inClinic,
            taskType,
            task
        )

        fun createTaskSpecResponse(): TaskSpecResponse = taskSpecResponse

        private const val newTitle = "new $title"
        private const val newDescription = "new $description"
        fun createUpdateTaskSpecCommand(schedule: String): UpdateTaskSpecCommand = UpdateTaskSpecCommand(
            newTitle,
            newDescription,
            status,
            schedule,
            null,
            null,
            startTime,
            endTime,
            validMin,
            duration,
            iconUrl,
            inClinic,
            taskType,
            task
        )

        fun createActivityTaskSpec(): TaskSpec {
            val command = CreateTaskSpecCommand(
                title = "test title",
                description = "test description",
                status = TaskStatus.PUBLISHED,
                schedule = "0 0 0 * * ? *",
                startTime = LocalDateTime.now(),
                endTime = LocalDateTime.now(),
                validMin = 123,
                duration = "10s",
                iconUrl = "example.com",
                taskType = TaskType.ACTIVITY,
                task = mapOf(
                    "completionTitle" to "ct",
                    "completionDescription" to "cd",
                    "type" to "STROOP_TEST"
                )
            )

            return command.toDomain("test-study-id")
        }
    }
}
