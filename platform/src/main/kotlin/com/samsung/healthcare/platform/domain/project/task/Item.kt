package com.samsung.healthcare.platform.domain.project.task

import com.samsung.healthcare.platform.enums.ItemType

/**
 * Represents items, which are actionables within a [Task].
 *
 * @property id id associated with an Item instance.
 * @property revisionId revisionId of the associated Task instance.
 * @property taskId taskId of the associated Task instance.
 * @property name Name of an Item instance.
 * @property contents The contents associated with an Item instance.
 * @property type The type associated with an Item instance.
 * @property sequence Indicates the order of an Item within the associated Task sequence.
 */
data class Item(
    val id: Int?,
    val revisionId: RevisionId,
    val taskId: String,
    val name: String,
    val contents: Map<String, Any>,
    val type: ItemType,
    val sequence: Int
) {
    companion object {
        const val ITEM_NAME_LENGTH = 320
        private const val QUESTION_PREFIX = "Question"
        fun newItem(
            task: Task,
            contents: Map<String, Any>,
            type: ItemType,
            sequence: Int
        ): Item {
            requireNotNull(task.revisionId)
            return Item(null, task.revisionId, task.id, "$QUESTION_PREFIX$sequence", contents, type, sequence)
        }
    }

    init {
        // TODO: validate contents field by type
    }

    fun unrollItem(): Map<String, Any?> {
        val ret = mutableMapOf<String, Any?>()
        ret["name"] = this.name
        ret["type"] = this.type
        ret["contents"] = this.contents
        ret["sequence"] = this.sequence
        return ret
    }
}
