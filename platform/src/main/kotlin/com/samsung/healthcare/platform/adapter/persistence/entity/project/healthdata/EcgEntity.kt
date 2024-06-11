package com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata

import com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub.EcgMapper
import com.samsung.healthcare.platform.domain.project.UserProfile
import com.samsung.healthcare.platform.domain.project.healthdata.Ecg
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("ecg")
data class EcgEntity(
    override val id: Int? = null,
    override val userId: String,
    override val time: LocalDateTime,
    val minThresholdMv: Double,
    val maxThresholdMv: Double,
    val ppg1: Long,
    val ppg2: Long? = null,
    val ecg1Mv: Double,
    val ecg2Mv: Double,
    val ecg3Mv: Double,
    val ecg4Mv: Double,
    val ecg5Mv: Double,
    val ecg6Mv: Double? = null,
    val ecg7Mv: Double? = null,
    val ecg8Mv: Double? = null,
    val ecg9Mv: Double? = null,
    val ecg10Mv: Double? = null,
) : SampleDataEntity(id, userId, time)

fun Ecg.toEntity(userId: UserProfile.UserId): EcgEntity =
    EcgMapper.INSTANCE.toEntity(this, userId)
