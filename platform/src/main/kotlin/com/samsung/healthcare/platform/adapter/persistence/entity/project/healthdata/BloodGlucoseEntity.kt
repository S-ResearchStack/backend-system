package com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata

import com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub.BloodGlucoseMapper
import com.samsung.healthcare.platform.domain.project.UserProfile
import com.samsung.healthcare.platform.domain.project.healthdata.BloodGlucose
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("blood_glucoses")
data class BloodGlucoseEntity(
    override val id: Int? = null,
    override val userId: String,
    override val time: LocalDateTime,
    val millimolesPerLiter: Double,
) : SampleDataEntity(id, userId, time)

fun BloodGlucose.toEntity(userId: UserProfile.UserId): BloodGlucoseEntity =
    BloodGlucoseMapper.INSTANCE.toEntity(this, userId)
