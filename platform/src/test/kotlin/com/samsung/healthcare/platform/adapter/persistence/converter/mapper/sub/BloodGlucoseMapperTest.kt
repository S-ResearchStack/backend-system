package com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub

import com.samsung.healthcare.platform.POSITIVE_TEST
import com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata.toEntity
import com.samsung.healthcare.platform.domain.project.UserProfile
import com.samsung.healthcare.platform.domain.project.healthdata.BloodGlucose
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneOffset

internal class BloodGlucoseMapperTest {
    @Test
    @Tag(POSITIVE_TEST)
    fun `should convert domain to entity`() {
        val localDateTime = LocalDateTime.of(2022, 9, 7, 12, 0, 0)
        val bloodGlucose = BloodGlucose.newBloodGlucose(
            localDateTime.toInstant(ZoneOffset.UTC),
            10.0,
        )

        val userId = UserProfile.UserId.from("user-id")

        val bloodGlucoseEntity = bloodGlucose.toEntity(userId)

        assertThat(bloodGlucoseEntity.id).isEqualTo(bloodGlucose.id?.value)
        assertThat(bloodGlucoseEntity.userId).isEqualTo(userId.value)
        assertThat(bloodGlucoseEntity.time).isEqualTo(LocalDateTime.ofInstant(bloodGlucose.time, ZoneOffset.UTC))
        assertThat(bloodGlucoseEntity.millimolesPerLiter).isEqualTo(bloodGlucose.millimolesPerLiter)
    }
}
