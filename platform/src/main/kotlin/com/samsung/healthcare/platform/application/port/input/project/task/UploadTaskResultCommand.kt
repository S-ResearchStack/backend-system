package com.samsung.healthcare.platform.application.port.input.project.task

import com.samsung.healthcare.platform.domain.project.UserProfile.Companion.USER_PROFILE_LENGTH
import com.samsung.healthcare.platform.domain.project.task.Item.Companion.ITEM_NAME_LENGTH
import com.samsung.healthcare.platform.domain.project.task.Task.Companion.TASK_ID_LENGTH
import java.time.LocalDateTime

data class UploadTaskResultCommand(
    val revisionId: Int,
    val taskId: String,
    val userId: String,
    val startedAt: LocalDateTime,
    val submittedAt: LocalDateTime,
    val itemResults: List<UpdateItemResultCommand>
) {
    init {
        require(taskId.length < TASK_ID_LENGTH)
        require(userId.length < USER_PROFILE_LENGTH)
    }

    data class UpdateItemResultCommand(
        val itemName: String,
        val result: String,
    ) {
        init {
            require(itemName.length < ITEM_NAME_LENGTH)
        }
    }
}
