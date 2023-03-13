package com.samsung.healthcare.platform.domain.project.task

/**
 * Represents a result of an [Item].
 *
 * @property id id associated with an ItemResult instance.
 * @property revisionId revisionId of the associated [Task] instance to which the associated Item instance belongs.
 * @property taskId taskId of the associated Task instance to which the associated Item instance belongs.
 * @property userId userId of he [UserProfile][com.samsung.healthcare.platform.domain.project.UserProfile] submitting an ItemResult instance.
 * @property itemName The name of the associated Item instance.
 * @property result The submitted result.
 * @constructor Create empty Item result
 */
data class ItemResult(
    val id: Int?,
    val revisionId: RevisionId,
    val taskId: String,
    val userId: String,
    val itemName: String,
    val result: String,
) {
    companion object {
        const val ITEM_RESULT_LENGTH = 320
        fun newItemResult(
            taskResult: TaskResult,
            itemName: String,
            result: String,
        ): ItemResult {
            return ItemResult(null, taskResult.revisionId, taskResult.taskId, taskResult.userId, itemName, result)
        }
    }
}
