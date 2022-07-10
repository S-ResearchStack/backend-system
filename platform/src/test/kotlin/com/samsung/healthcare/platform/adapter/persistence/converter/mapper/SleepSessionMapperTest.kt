package com.samsung.healthcare.platform.adapter.persistence.converter.mapper

import com.samsung.healthcare.platform.adapter.persistence.converter.mapper.sub.SleepSessionMapper
import com.samsung.healthcare.platform.domain.project.UserProfile.UserId
import com.samsung.healthcare.platform.domain.project.healthdata.HealthData.HealthDataId
import com.samsung.healthcare.platform.domain.project.healthdata.SleepSession
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneOffset

internal class SleepSessionMapperTest {
    @Test
    fun `should convert domain to entity`() {
        val startTime = LocalDateTime.of(2022, 8, 7, 12, 0, 0)
        val endTime = LocalDateTime.of(2022, 9, 7, 12, 0, 0)
        val sleepSession = SleepSession(
            HealthDataId.from(1),
            startTime.toInstant(ZoneOffset.UTC),
            endTime.toInstant(ZoneOffset.UTC),
            "title-sample",
            "notes-sample"
        )
        val userId = UserId.from("jjyun.do")

        val sleepSessionEntity = SleepSessionMapper.INSTANCE.toEntity(sleepSession, userId)

        assertThat(sleepSessionEntity.id).isEqualTo(sleepSession.id?.value)
        assertThat(sleepSessionEntity.startTime).isEqualTo(
            LocalDateTime.ofInstant(
                sleepSession.startTime,
                ZoneOffset.UTC
            )
        )
        assertThat(sleepSessionEntity.endTime).isEqualTo(
            LocalDateTime.ofInstant(
                sleepSession.endTime,
                ZoneOffset.UTC
            )
        )
        assertThat(sleepSessionEntity.userId).isEqualTo(userId.value)
        assertThat(sleepSessionEntity.title).isEqualTo(sleepSession.title)
        assertThat(sleepSessionEntity.notes).isEqualTo(sleepSession.notes)
    }
}
