package com.samsung.healthcare.platform.adapter.persistence.entity.project

import com.samsung.healthcare.platform.domain.project.InLabVisit
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("in_lab_visit")
data class InLabVisitEntity(
    @Id
    val id: Int? = null,
    val userId: String,
    val checkedInBy: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val notes: String? = null,
) {
    fun toDomain(): InLabVisit = InLabVisit(
        id, userId, checkedInBy, startTime, endTime, notes, "in-lab-visit/$id",
    )
}

fun InLabVisit.toEntity(): InLabVisitEntity = InLabVisitEntity(
    id, userId, checkedInBy, startTime, endTime, notes
)
