package com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub

import com.samsung.healthcare.platform.POSITIVE_TEST
import com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata.toEntity
import com.samsung.healthcare.platform.domain.project.UserProfile
import com.samsung.healthcare.platform.domain.project.healthdata.Height
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneOffset

internal class HeightMapperTest {
    @Test
    @Tag(POSITIVE_TEST)
    fun `should convert domain to entity`() {
        val time = LocalDateTime.of(2022, 9, 7, 12, 0, 0)
        val height = Height.newHeight(
            time.toInstant(ZoneOffset.UTC),
            1000.0,
        )

        val userId = UserProfile.UserId.from("user-id")

        val heightEntity = height.toEntity(userId)

        Assertions.assertThat(heightEntity.id).isEqualTo(height.id?.value)
        Assertions.assertThat(heightEntity.time).isEqualTo(LocalDateTime.ofInstant(height.time, ZoneOffset.UTC))
        Assertions.assertThat(heightEntity.meters).isEqualTo(height.meters)
    }
}
