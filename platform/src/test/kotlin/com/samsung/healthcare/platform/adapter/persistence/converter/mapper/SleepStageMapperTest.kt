package com.samsung.healthcare.platform.adapter.persistence.converter.mapper

import com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub.SleepStageMapper
import com.samsung.healthcare.platform.domain.project.UserProfile.UserId
import com.samsung.healthcare.platform.domain.project.healthdata.HealthData.HealthDataId
import com.samsung.healthcare.platform.domain.project.healthdata.SleepStage
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneOffset

internal class SleepStageMapperTest {
    @Test
    fun `should convert domain to entity`() {
        val startTime = LocalDateTime.of(2022, 8, 7, 12, 0, 0)
        val endTime = LocalDateTime.of(2022, 9, 7, 12, 0, 0)
        val sleepStage = SleepStage(
            HealthDataId.from(1),
            startTime.toInstant(ZoneOffset.UTC),
            endTime.toInstant(ZoneOffset.UTC),
            "stage-sample",
        )
        val userId = UserId.from("jjyun.do")

        val sleepStageEntity = SleepStageMapper.INSTANCE.toEntity(sleepStage, userId)

        assertThat(sleepStageEntity.id).isEqualTo(sleepStage.id?.value)
        assertThat(sleepStageEntity.startTime).isEqualTo(
            LocalDateTime.ofInstant(
                sleepStage.startTime,
                ZoneOffset.UTC
            )
        )
        assertThat(sleepStageEntity.endTime).isEqualTo(
            LocalDateTime.ofInstant(
                sleepStage.endTime,
                ZoneOffset.UTC
            )
        )
        assertThat(sleepStageEntity.userId).isEqualTo(userId.value)
        assertThat(sleepStageEntity.stage).isEqualTo(sleepStage.stage)
    }
}
