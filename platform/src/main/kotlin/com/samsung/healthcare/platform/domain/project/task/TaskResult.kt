package com.samsung.healthcare.platform.domain.project.task

import java.time.LocalDateTime

/**
 * Represents a result of a [Task].
 *
 * @property id id associated with a TaskResult instance.
 * @property revisionId The revisionId of the relevant Task version.
 * @property taskId The taskId of the relevant Task.
 * @property userId The userId of the [UserProfile][com.samsung.healthcare.platform.domain.project.UserProfile] submitting a TaskResult instance.
 * @property startedAt Indicates when the user began the Task.
 * @property submittedAt Indicates when the user submitted the Task.
 */
data class TaskResult(
    val id: Int?,
    val revisionId: RevisionId,
    val taskId: String,
    val userId: String,
    val startedAt: LocalDateTime,
    val submittedAt: LocalDateTime,
) {
    companion object {
        fun newTaskResult(
            revisionId: RevisionId,
            taskId: String,
            userId: String,
            startedAt: LocalDateTime,
            submittedAt: LocalDateTime,
        ): TaskResult =
            TaskResult(null, revisionId, taskId, userId, startedAt, submittedAt)
    }
}
