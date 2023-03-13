package com.samsung.healthcare.platform.adapter.persistence.project

import com.samsung.healthcare.platform.NEGATIVE_TEST
import com.samsung.healthcare.platform.adapter.persistence.entity.project.toEntity
import com.samsung.healthcare.platform.application.exception.UserAlreadyExistsException
import com.samsung.healthcare.platform.domain.project.UserProfile
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.dao.DuplicateKeyException

@kotlinx.coroutines.ExperimentalCoroutinesApi
class UserProfileDatabaseAdapterTest {
    private val userProfileRepository = mockk<UserProfileRepository>()
    private val userProfileDatabaseAdapter = UserProfileDatabaseAdapter(userProfileRepository)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should throw UserAlreadyExistsException if uid already exists`() = runTest {
        val profile = UserProfile.newUserProfile("uid", emptyMap())
        val entity = profile.toEntity()

        coEvery { userProfileRepository.save(entity) } throws DuplicateKeyException("id already exists")

        assertThrows<UserAlreadyExistsException> { userProfileDatabaseAdapter.create(profile) }
    }
}
