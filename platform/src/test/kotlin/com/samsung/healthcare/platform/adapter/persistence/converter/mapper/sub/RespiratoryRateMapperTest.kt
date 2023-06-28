package com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub

import com.samsung.healthcare.platform.POSITIVE_TEST
import com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata.toEntity
import com.samsung.healthcare.platform.domain.project.UserProfile
import com.samsung.healthcare.platform.domain.project.healthdata.RespiratoryRate
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneOffset

internal class RespiratoryRateMapperTest {
    @Test
    @Tag(POSITIVE_TEST)
    fun `should convert domain to entity`() {
        val time = LocalDateTime.of(2022, 9, 7, 12, 0, 0)
        val respiratoryRate = RespiratoryRate.newRespiratoryRate(
            time.toInstant(ZoneOffset.UTC),
            100.0
        )

        val userId = UserProfile.UserId.from("user-id")

        val respiratoryRateEntity = respiratoryRate.toEntity(userId)

        Assertions.assertThat(respiratoryRateEntity.id).isEqualTo(respiratoryRate.id?.value)
        Assertions.assertThat(respiratoryRateEntity.time).isEqualTo(
            LocalDateTime.ofInstant(respiratoryRate.time, ZoneOffset.UTC)
        )
        Assertions.assertThat(respiratoryRateEntity.rpm).isEqualTo(respiratoryRate.rpm)
    }
}
