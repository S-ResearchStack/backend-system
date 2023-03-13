package com.samsung.healthcare.platform.application.port.input

import com.samsung.healthcare.platform.domain.Project.Companion.PROJECT_NAME_LENGTH

data class CreateProjectCommand(
    val name: String,
    val info: Map<String, Any> = emptyMap(),
    val isOpen: Boolean = true
) {
    init {
        require(name.isNotBlank() && name.length < PROJECT_NAME_LENGTH)
    }
}
