package com.samsung.healthcare.platform.application.service.project.task

import com.samsung.healthcare.platform.POSITIVE_TEST
import com.samsung.healthcare.platform.application.port.input.project.task.UploadTaskResultCommand
import com.samsung.healthcare.platform.application.port.output.project.task.ItemResultOutputPort
import com.samsung.healthcare.platform.application.port.output.project.task.TaskResultOutputPort
import com.samsung.healthcare.platform.domain.project.task.ItemResult
import com.samsung.healthcare.platform.domain.project.task.TaskResult
import io.mockk.MockKMatcherScope
import io.mockk.coJustRun
import io.mockk.coVerifySequence
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

internal class UploadTaskResultServiceTest {
    private val taskResultOutputPort = mockk<TaskResultOutputPort>()
    private val itemResultOutputPort = mockk<ItemResultOutputPort>()
    private val uploadTaskResultService = UploadTaskResultService(
        taskResultOutputPort,
        itemResultOutputPort
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `should generate corresponding task and item results`() = runTest {
        val updateItemResultCommand1 = UploadTaskResultCommand.UpdateItemResultCommand("Q1", "Yes")
        val uploadTaskResultCommand1 = UploadTaskResultCommand(
            1,
            "test-task-1",
            "user1",
            LocalDateTime.parse("2022-10-01T09:45", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            LocalDateTime.parse("2022-10-01T10:10", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            listOf(updateItemResultCommand1)
        )
        val updateItemResultCommand2 = UploadTaskResultCommand.UpdateItemResultCommand("Q1", "No")
        val uploadTaskResultCommand2 = UploadTaskResultCommand(
            1,
            "test-task-1",
            "user2",
            LocalDateTime.parse("2022-10-01T08:05", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            LocalDateTime.parse("2022-10-01T14:20", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            listOf(updateItemResultCommand2)
        )
        val updateItemResultCommand3 = UploadTaskResultCommand.UpdateItemResultCommand("Question 1", "42")
        val uploadTaskResultCommand3 = UploadTaskResultCommand(
            2,
            "test-task-2",
            "user3",
            LocalDateTime.parse("2022-10-01T16:55", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            LocalDateTime.parse("2022-10-01T17:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            listOf(updateItemResultCommand3)
        )
        coJustRun {
            taskResultOutputPort.create(any())
        }
        coJustRun {
            itemResultOutputPort.create(any())
        }

        uploadTaskResultService.uploadResults(
            listOf(
                uploadTaskResultCommand1,
                uploadTaskResultCommand2,
                uploadTaskResultCommand3
            )
        )

        coVerifySequence {
            taskResultOutputPort.create(taskMatchesCommand(uploadTaskResultCommand1))
            itemResultOutputPort.create(itemMatchesCommand(uploadTaskResultCommand1, updateItemResultCommand1))
            taskResultOutputPort.create(taskMatchesCommand(uploadTaskResultCommand2))
            itemResultOutputPort.create(itemMatchesCommand(uploadTaskResultCommand2, updateItemResultCommand2))
            taskResultOutputPort.create(taskMatchesCommand(uploadTaskResultCommand3))
            itemResultOutputPort.create(itemMatchesCommand(uploadTaskResultCommand3, updateItemResultCommand3))
        }
    }

    private fun MockKMatcherScope.taskMatchesCommand(uploadTaskResultCommand: UploadTaskResultCommand): TaskResult =
        match {
            it.taskId == uploadTaskResultCommand.taskId &&
                it.userId == uploadTaskResultCommand.userId &&
                it.startedAt == uploadTaskResultCommand.startedAt &&
                it.submittedAt == uploadTaskResultCommand.submittedAt
        }

    private fun MockKMatcherScope.itemMatchesCommand(
        uploadTaskResultCommand: UploadTaskResultCommand,
        updateItemResultCommand: UploadTaskResultCommand.UpdateItemResultCommand
    ): ItemResult =
        match {
            it.taskId == uploadTaskResultCommand.taskId &&
                it.userId == uploadTaskResultCommand.userId &&
                it.itemName == updateItemResultCommand.itemName &&
                it.result == updateItemResultCommand.result
        }
}
