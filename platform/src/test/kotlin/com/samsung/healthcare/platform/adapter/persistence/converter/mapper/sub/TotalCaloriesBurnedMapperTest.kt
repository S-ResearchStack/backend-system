package com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub

import com.samsung.healthcare.platform.POSITIVE_TEST
import com.samsung.healthcare.platform.adapter.persistence.entity.project.healthdata.toEntity
import com.samsung.healthcare.platform.domain.project.UserProfile
import com.samsung.healthcare.platform.domain.project.healthdata.TotalCaloriesBurned
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneOffset

internal class TotalCaloriesBurnedMapperTest {
    @Test
    @Tag(POSITIVE_TEST)
    fun `should convert domain to entity`() {
        val startTime = LocalDateTime.of(2022, 9, 7, 12, 0, 0)
        val endTime = LocalDateTime.of(2022, 9, 7, 13, 0, 0)
        val totalCaloriesBurned = TotalCaloriesBurned.newTotalCaloriesBurned(
            startTime.toInstant(ZoneOffset.UTC),
            endTime.toInstant(ZoneOffset.UTC),
            1000.0
        )

        val userId = UserProfile.UserId.from("user-id")

        val totalCaloriesBurnedEntity = totalCaloriesBurned.toEntity(userId)

        Assertions.assertThat(totalCaloriesBurnedEntity.id).isEqualTo(totalCaloriesBurned.id?.value)
        Assertions.assertThat(totalCaloriesBurnedEntity.startTime).isEqualTo(
            LocalDateTime.ofInstant(
                totalCaloriesBurned.startTime, ZoneOffset.UTC
            )
        )
        Assertions.assertThat(totalCaloriesBurnedEntity.endTime).isEqualTo(
            LocalDateTime.ofInstant(
                totalCaloriesBurned.endTime, ZoneOffset.UTC
            )
        )
        Assertions.assertThat(totalCaloriesBurnedEntity.calories).isEqualTo(totalCaloriesBurned.calories)
    }
}
