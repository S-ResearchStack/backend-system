package com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata

import com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub.HeightMapper
import com.samsung.healthcare.platform.domain.project.UserProfile
import com.samsung.healthcare.platform.domain.project.healthdata.Height
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("heights")
data class HeightEntity(
    override val id: Int? = null,
    override val userId: String,
    override val time: LocalDateTime,
    val meters: Double,
) : SampleDataEntity(id, userId, time)

fun Height.toEntity(userId: UserProfile.UserId): HeightEntity =
    HeightMapper.INSTANCE.toEntity(this, userId)
