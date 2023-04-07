package com.samsung.healthcare.platform.domain.project.task

import com.samsung.healthcare.platform.enums.TaskStatus
import com.samsung.healthcare.platform.enums.TaskType
import java.time.LocalDateTime
import java.util.UUID

/**
 * Represents a task assigned to participants.
 *
 * @property revisionId revisionId keeps track of updated Task versions.
 * @property id id associated with a Task instance.
 * @property properties Map of the properties of a Task instance, such as Title, Description, or Schedule.
 * @property status Current status of Task.
 * @property createdAt Indicates when a Task instance was created.
 * @property publishedAt Indicates when the latest version of a Task instance was published.
 * @property outdatedAt Indicates when a Task instance was outdated.
 * @property deletedAt Indicates when a Task instance was designated as deleted. Deletion flag used to prevent unwanted data loss.
 */
data class Task(
    val revisionId: RevisionId?,
    val id: String,
    val properties: Map<String, Any>,
    val status: TaskStatus,
    val type: TaskType,
    val createdAt: LocalDateTime? = null,
    var publishedAt: LocalDateTime? = null,
    val outdatedAt: LocalDateTime? = null,
    val deletedAt: LocalDateTime? = null,
) {
    companion object {
        const val TASK_ID_LENGTH = 320
        fun newTask(type: TaskType): Task =
            Task(
                null,
                UUID.randomUUID().toString(),
                emptyMap(),
                TaskStatus.DRAFT,
                type
            )
    }

    fun unrollTask(): MutableMap<String, Any?> {
        val ret = mutableMapOf<String, Any?>()
        ret["revisionId"] = this.revisionId?.value
        ret["id"] = this.id
        ret["status"] = this.status
        ret["type"] = this.type
        ret["createdAt"] = this.createdAt
        ret["publishedAt"] = this.publishedAt
        ret["outdatedAt"] = this.outdatedAt
        ret["deletedAt"] = this.deletedAt
        this.properties.forEach {
            ret[it.key] = it.value
        }
        return ret
    }
}

class RevisionId private constructor(val value: Int) {
    companion object {
        fun from(value: Int?): RevisionId {
            requireNotNull(value)
            require(0 < value)
            return RevisionId(value)
        }
    }
}
