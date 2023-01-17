package com.samsung.healthcare.platform.adapter.persistence.converter.mapper

import com.samsung.healthcare.platform.POSITIVE_TEST
import com.samsung.healthcare.platform.adapter.persistence.entity.project.UserProfileEntity
import com.samsung.healthcare.platform.adapter.persistence.entity.project.toEntity
import com.samsung.healthcare.platform.domain.project.UserProfile
import com.samsung.healthcare.platform.domain.project.UserProfile.UserId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class UserProfileMapperTest {
    @Test
    @Tag(POSITIVE_TEST)
    fun `should convert domain to entity`() {
        val userProfile = UserProfile(
            UserId.from("test-user-id"),
            emptyMap(),
            LocalDateTime.now(),
        )

        val userProfileEntity = userProfile.toEntity()

        assertThat(userProfileEntity.userId).isEqualTo(userProfile.userId.value)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should convert entity to domain`() {
        val userProfileEntity = UserProfileEntity(
            "test-user-id",
            emptyMap(),
            LocalDateTime.now(),
        )

        val userProfile = userProfileEntity.toDomain()

        assertThat(userProfile.userId.value).isEqualTo(userProfileEntity.userId)
    }
}
