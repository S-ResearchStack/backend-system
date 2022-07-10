package com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata

import com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub.BloodPressureMapper
import com.samsung.healthcare.platform.domain.project.UserProfile
import com.samsung.healthcare.platform.domain.project.healthdata.BloodPressure
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("blood_pressures")
data class BloodPressureEntity(
    override val id: Int? = null,
    override val userId: String,
    override val time: LocalDateTime,
    val systolic: Double,
    val diastolic: Double,
    val bodyPosition: String? = null,
    val measurementLocation: String? = null,
) : SampleDataEntity(id, userId, time)

fun BloodPressure.toEntity(userId: UserProfile.UserId): BloodPressureEntity =
    BloodPressureMapper.INSTANCE.toEntity(this, userId)
