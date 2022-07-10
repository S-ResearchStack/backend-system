package com.samsung.healthcare.platform.application.port.output.project.task

import com.samsung.healthcare.platform.domain.project.task.ItemResult

interface ItemResultOutputPort {
    suspend fun create(itemResult: ItemResult)
}
