package com.samsung.healthcare.platform.adapter.persistence.entity

import com.samsung.healthcare.platform.adapter.persistence.converter.mapper.ProjectMapper
import com.samsung.healthcare.platform.domain.Project
import org.springframework.data.annotation.CreatedDate
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
    @CreatedDate
    val createdAt: LocalDateTime? = null,
    val deletedAt: LocalDateTime? = null,
) {
    fun toDomain(): Project = ProjectMapper.INSTANCE.toDomain(this)
}

fun Project.toEntity(): ProjectEntity =
    ProjectMapper.INSTANCE.toEntity(this)
