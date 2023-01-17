package com.samsung.healthcare.platform.application.port.input

data class CreateProjectCommand(
    val name: String,
    val info: Map<String, Any> = emptyMap(),
    val isOpen: Boolean = true
) {
    init {
        require(name.isNotBlank())
    }
}
