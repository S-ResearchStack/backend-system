package com.samsung.healthcare.platform.adapter.persistence.converter.mapper

import com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub.HeartRateMapper
import com.samsung.healthcare.platform.domain.project.UserProfile.UserId
import com.samsung.healthcare.platform.domain.project.healthdata.HealthData
import com.samsung.healthcare.platform.domain.project.healthdata.HeartRate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneOffset

internal class HeartRateMapperTest {
    @Test
    fun `should convert domain to entity`() {
        val localDateTime = LocalDateTime.of(2022, 9, 7, 12, 0, 0)
        val heartRate = HeartRate(
            HealthData.HealthDataId.from(1),
            localDateTime.toInstant(ZoneOffset.UTC),
            180
        )
        val userId = UserId.from("jjyun.do")

        val heartRateEntity = HeartRateMapper.INSTANCE.toEntity(heartRate, userId)

        assertThat(heartRateEntity.id).isEqualTo(heartRate.id?.value)
        assertThat(heartRateEntity.userId).isEqualTo(userId.value)
        assertThat(heartRateEntity.time).isEqualTo(LocalDateTime.ofInstant(heartRate.time, ZoneOffset.UTC))
        assertThat(heartRateEntity.bpm).isEqualTo(heartRate.bpm)
    }
}
