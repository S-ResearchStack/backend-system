package com.samsung.healthcare.platform.adapter.persistence.converter.mapper

import com.samsung.healthcare.platform.adapter.persistence.entity.project.UserProfileEntity
import com.samsung.healthcare.platform.domain.project.UserProfile
import com.samsung.healthcare.platform.domain.project.UserProfile.UserId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class UserProfileMapperTest {
    @Test
    fun `should convert domain to entity`() {
        val userProfile = UserProfile(
            UserId.from("test-user-id"),
            emptyMap(),
            LocalDateTime.now(),
        )

        val userProfileEntity = UserProfileMapper.INSTANCE.toEntity(userProfile)

        assertThat(userProfileEntity.userId).isEqualTo(userProfile.userId.value)
    }

    @Test
    fun `should convert entity to domain`() {
        val userProfileEntity = UserProfileEntity(
            "test-user-id",
            emptyMap(),
            LocalDateTime.now(),
        )

        val userProfile = UserProfileMapper.INSTANCE.toDomain(userProfileEntity)

        assertThat(userProfile.userId.value).isEqualTo(userProfileEntity.userId)
    }
}
