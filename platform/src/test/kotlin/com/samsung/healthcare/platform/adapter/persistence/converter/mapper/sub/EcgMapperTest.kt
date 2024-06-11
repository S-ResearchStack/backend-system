package com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub

import com.samsung.healthcare.platform.POSITIVE_TEST
import com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata.toEntity
import com.samsung.healthcare.platform.domain.project.UserProfile
import com.samsung.healthcare.platform.domain.project.healthdata.Ecg
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneOffset

internal class EcgMapperTest {
    @Test
    @Tag(POSITIVE_TEST)
    fun `should convert domain to entity`() {
        val localDateTime = LocalDateTime.of(2022, 9, 7, 12, 0, 0)
        val ecg = Ecg.newEcg(
            localDateTime.toInstant(ZoneOffset.UTC),
            0.1,
            1.0,
            1,
            null,
            0.1,
            0.2,
            0.3,
            0.4,
            0.5,
            null,
            0.7,
            null,
            0.9,
            null,
        )

        val userId = UserProfile.UserId.from("user-id")

        val ecgEntity = ecg.toEntity(userId)

        assertThat(ecgEntity.id).isEqualTo(ecg.id?.value)
        assertThat(ecgEntity.userId).isEqualTo(userId.value)
        assertThat(ecgEntity.time).isEqualTo(LocalDateTime.ofInstant(ecg.time, ZoneOffset.UTC))
        assertThat(ecgEntity.ecg1Mv).isEqualTo(ecg.ecg1Mv)
        assertThat(ecgEntity.ecg2Mv).isEqualTo(ecg.ecg2Mv)
        assertThat(ecgEntity.ecg3Mv).isEqualTo(ecg.ecg3Mv)
        assertThat(ecgEntity.ecg4Mv).isEqualTo(ecg.ecg4Mv)
        assertThat(ecgEntity.ecg5Mv).isEqualTo(ecg.ecg5Mv)
        assertThat(ecgEntity.ecg6Mv).isEqualTo(ecg.ecg6Mv)
        assertThat(ecgEntity.ecg7Mv).isEqualTo(ecg.ecg7Mv)
        assertThat(ecgEntity.ecg8Mv).isEqualTo(ecg.ecg8Mv)
        assertThat(ecgEntity.ecg9Mv).isEqualTo(ecg.ecg9Mv)
        assertThat(ecgEntity.ecg10Mv).isEqualTo(ecg.ecg10Mv)
        assertThat(ecgEntity.ppg1).isEqualTo(ecg.ppg1)
        assertThat(ecgEntity.ppg2).isEqualTo(ecg.ppg2)
        assertThat(ecgEntity.minThresholdMv).isEqualTo(ecg.minThresholdMv)
        assertThat(ecgEntity.maxThresholdMv).isEqualTo(ecg.maxThresholdMv)
    }
}
