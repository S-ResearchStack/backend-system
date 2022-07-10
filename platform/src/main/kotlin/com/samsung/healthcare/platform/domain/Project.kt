package com.samsung.healthcare.platform.domain

import java.time.LocalDateTime

/**
 * Represents research project instances.
 *
 * @property id [ProjectId] associated with the project.
 * @property name Name of project.
 * @property info
 * @property isOpen Boolean indicating whether the project is open.
 * @property createdAt Time of creation.
 * @property deletedAt Time of deletion.
 * @constructor Create empty Project
 */
data class Project(
    val id: ProjectId?,
    val name: String,
    val info: Map<String, Any>,
    val isOpen: Boolean,
    val createdAt: LocalDateTime? = null,
    val deletedAt: LocalDateTime? = null,
) {
    companion object {
        fun newProject(name: String, info: Map<String, Any>, isOpen: Boolean): Project =
            Project(null, name, info, isOpen)
    }

    data class ProjectId private constructor(val value: Int) {
        companion object {
            fun from(value: Int?): ProjectId {
                requireNotNull(value)
                require(0 < value)
                return ProjectId(value)
            }
        }

        override fun toString(): String =
            value.toString()
    }
}
