package com.samsung.healthcare.platform.application.port.input.project.task

import com.samsung.healthcare.platform.enums.TaskType

data class CreateTaskCommand(
    val type: TaskType
)
