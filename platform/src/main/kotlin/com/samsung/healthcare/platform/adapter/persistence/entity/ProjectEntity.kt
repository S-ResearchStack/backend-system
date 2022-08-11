package com.samsung.healthcare.platform.adapter.persistence.entity

import com.samsung.healthcare.platform.domain.Project
import com.samsung.healthcare.platform.domain.Project.ProjectId
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("projects")
data class ProjectEntity(
    @Id
    val id: Int?,
    val name: String,
    val info: Map<String, Any>,
    val isOpen: Boolean,
    private val createdAt: LocalDateTime,
    private val deletedAt: LocalDateTime?,
) {
    fun toDomain(): Project {
        requireNotNull(this.id)
        return Project(ProjectId.from(this.id), this.name, this.info, this.isOpen, this.createdAt)
    }
}

fun Project.toEntity(): ProjectEntity =
    ProjectEntity(this.id?.value, this.name, this.info, this.isOpen, this.createdAt, this.deletedAt)
