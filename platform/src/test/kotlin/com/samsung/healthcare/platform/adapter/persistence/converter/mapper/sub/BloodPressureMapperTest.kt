package com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub

import com.samsung.healthcare.platform.POSITIVE_TEST
import com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata.toEntity
import com.samsung.healthcare.platform.domain.project.UserProfile
import com.samsung.healthcare.platform.domain.project.healthdata.BloodPressure
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.time.LocalDateTime
import java.time.ZoneOffset

internal class BloodPressureMapperTest {
    @Test
    @Tag(POSITIVE_TEST)
    fun `should convert domain to entity`() {
        val localDateTime = LocalDateTime.of(2022, 9, 7, 12, 0, 0)
        val bloodPressure = BloodPressure.newBloodPressure(
            localDateTime.toInstant(ZoneOffset.UTC),
            120.0,
            80.0,
        )

        val userId = UserProfile.UserId.from("jjyun.do")
        val bloodPressureEntity = bloodPressure.toEntity(userId)

        assertAll(
            "Blood pressure mapping",
            { assertEquals(userId.value, bloodPressureEntity.userId) },
            { assertEquals(bloodPressure.id?.value, bloodPressureEntity.id) },
            { assertEquals(LocalDateTime.ofInstant(bloodPressure.time, ZoneOffset.UTC), bloodPressureEntity.time) },
            { assertEquals(bloodPressure.systolic, bloodPressureEntity.systolic) },
            { assertEquals(bloodPressure.diastolic, bloodPressureEntity.diastolic) },
            { assertEquals(bloodPressure.bodyPosition?.name, bloodPressureEntity.bodyPosition) },
            { assertEquals(bloodPressure.measurementLocation?.name, bloodPressureEntity.measurementLocation) }
        )

        val bodyPosition = BloodPressure.BodyPosition.SITTING_DOWN
        val measurementLocation = BloodPressure.MeasurementLocation.LEFT_UPPER_ARM

        val detailedBloodPressure = BloodPressure.newBloodPressure(
            localDateTime.toInstant(ZoneOffset.UTC),
            120.0,
            80.0,
            bodyPosition,
            measurementLocation
        )

        assertThat(detailedBloodPressure.bodyPosition!!.value).isEqualTo(bodyPosition.value)
        assertThat(detailedBloodPressure.measurementLocation!!.value).isEqualTo(measurementLocation.value)
    }
}
