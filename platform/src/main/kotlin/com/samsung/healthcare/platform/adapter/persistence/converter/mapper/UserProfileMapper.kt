package com.samsung.healthcare.platform.adapter.persistence.converter.mapper

import com.samsung.healthcare.platform.adapter.persistence.entity.project.UserProfileEntity
import com.samsung.healthcare.platform.domain.project.UserProfile
import com.samsung.healthcare.platform.domain.project.UserProfile.UserId
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers

@Mapper
abstract class UserProfileMapper {
    companion object {
        val INSTANCE: UserProfileMapper = Mappers.getMapper(UserProfileMapper::class.java)
    }

    @Mapping(target = "userId", source = "userId.value")
    abstract fun toEntity(userProfile: UserProfile): UserProfileEntity

    @Mapping(target = "userId", source = ".")
    abstract fun toDomain(userProfileEntity: UserProfileEntity): UserProfile

    fun mapUserId(value: UserProfileEntity): UserId =
        UserId.from(value.userId)
}
