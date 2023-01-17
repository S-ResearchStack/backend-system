package com.samsung.healthcare.account.adapter.auth.supertoken

import com.samsung.healthcare.account.adapter.auth.supertoken.PathConstant.GET_ACCOUNT_PATH
import com.samsung.healthcare.account.adapter.auth.supertoken.PathConstant.SUPER_TOKEN_ASSIGN_ROLE_PATH
import com.samsung.healthcare.account.adapter.auth.supertoken.PathConstant.SUPER_TOKEN_COUNT_USERS_PATH
import com.samsung.healthcare.account.adapter.auth.supertoken.PathConstant.SUPER_TOKEN_CREATE_ROLE_PATH
import com.samsung.healthcare.account.adapter.auth.supertoken.PathConstant.SUPER_TOKEN_GENERATE_EMAIL_VERIFICATION_TOKEN_PATH
import com.samsung.healthcare.account.adapter.auth.supertoken.PathConstant.SUPER_TOKEN_GENERATE_RESET_TOKEN_PATH
import com.samsung.healthcare.account.adapter.auth.supertoken.PathConstant.SUPER_TOKEN_GET_ROLE_USER_PATH
import com.samsung.healthcare.account.adapter.auth.supertoken.PathConstant.SUPER_TOKEN_GET_USER_ROLE_PATH
import com.samsung.healthcare.account.adapter.auth.supertoken.PathConstant.SUPER_TOKEN_JWT_PATH
import com.samsung.healthcare.account.adapter.auth.supertoken.PathConstant.SUPER_TOKEN_LIST_USERS_PATH
import com.samsung.healthcare.account.adapter.auth.supertoken.PathConstant.SUPER_TOKEN_REMOVE_USER_ROLE_PATH
import com.samsung.healthcare.account.adapter.auth.supertoken.PathConstant.SUPER_TOKEN_RESET_PASSWORD_PATH
import com.samsung.healthcare.account.adapter.auth.supertoken.PathConstant.SUPER_TOKEN_SIGN_IN_PATH
import com.samsung.healthcare.account.adapter.auth.supertoken.PathConstant.SUPER_TOKEN_SIGN_UP_PATH
import com.samsung.healthcare.account.adapter.auth.supertoken.PathConstant.SUPER_TOKEN_USER_META_DATA_PATH
import com.samsung.healthcare.account.adapter.auth.supertoken.PathConstant.SUPER_TOKEN_VERIFY_EMAIL_PATH
import feign.Headers
import feign.Param
import feign.RequestLine
import reactor.core.publisher.Mono

@Headers("Accept: application/json")
interface SuperTokensApi {
    @RequestLine("POST $SUPER_TOKEN_SIGN_IN_PATH")
    @Headers("Content-Type: application/json")
    fun signIn(signInRequest: SignRequest): Mono<AccountResponse>

    @RequestLine("POST $SUPER_TOKEN_SIGN_UP_PATH")
    @Headers("Content-Type: application/json")
    fun signUp(signUpRequest: SignRequest): Mono<AccountResponse>

    @RequestLine("POST $SUPER_TOKEN_RESET_PASSWORD_PATH")
    @Headers("Content-Type: application/json")
    fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Mono<ResetPasswordResponse>

    @RequestLine("POST $SUPER_TOKEN_GENERATE_RESET_TOKEN_PATH")
    @Headers("Content-Type: application/json")
    fun generateResetToken(userId: UserId): Mono<ResetTokenResponse>

    @RequestLine("PUT $SUPER_TOKEN_ASSIGN_ROLE_PATH")
    @Headers("Content-Type: application/json")
    fun assignRoles(roleBinding: RoleBinding): Mono<StatusResponse>

    @RequestLine("POST $SUPER_TOKEN_REMOVE_USER_ROLE_PATH")
    @Headers("Content-Type: application/json")
    fun removeUserRole(roleBinding: RoleBinding): Mono<StatusResponse>

    @RequestLine("PUT $SUPER_TOKEN_CREATE_ROLE_PATH")
    @Headers("Content-Type: application/json")
    fun createRoles(createRoleRequest: CreateRoleRequest): Mono<StatusResponse>

    @RequestLine("GET $SUPER_TOKEN_GET_USER_ROLE_PATH?userId={userId}")
    fun listUserRoles(@Param userId: String): Mono<RolesResponse>

    @RequestLine("GET $SUPER_TOKEN_LIST_USERS_PATH")
    fun listUsers(): Mono<ListUserResponse>

    @RequestLine("POST $SUPER_TOKEN_JWT_PATH")
    @Headers("Content-Type: application/json")
    fun generateSignedJwt(generateJwtRequest: GenerateJwtRequest): Mono<JwtResponse>

    @RequestLine("GET $GET_ACCOUNT_PATH?email={email}")
    fun getAccountWithEmail(@Param email: String): Mono<AccountResponse>

    @RequestLine("GET $GET_ACCOUNT_PATH?userId={id}")
    fun getAccountWithId(@Param id: String): Mono<AccountResponse>

    @RequestLine("GET $SUPER_TOKEN_USER_META_DATA_PATH?userId={userId}")
    fun getMetaData(@Param userId: String): Mono<MetaDataResponse>

    @RequestLine("PUT $SUPER_TOKEN_USER_META_DATA_PATH")
    fun updateMetadata(metadataUpdateRequest: MetadataUpdateRequest): Mono<MetaDataResponse>

    @RequestLine("GET $SUPER_TOKEN_GET_ROLE_USER_PATH?role={role}")
    fun listUsersOfRole(@Param role: String): Mono<RoleUsersResponse>

    @RequestLine("POST $SUPER_TOKEN_GENERATE_EMAIL_VERIFICATION_TOKEN_PATH")
    @Headers("Content-Type: application/json")
    fun generateEmailVerificationToken(
        generateEmailVerificationTokenRequest: GenerateEmailVerificationTokenRequest,
    ): Mono<GenerateEmailVerificationTokenResponse>

    @RequestLine("POST $SUPER_TOKEN_VERIFY_EMAIL_PATH")
    @Headers("Content-Type: application/json")
    fun verifyEmail(
        verifyEmailRequest: VerifyEmailRequest,
    ): Mono<VerifyEmailResponse>

    @RequestLine("GET $SUPER_TOKEN_VERIFY_EMAIL_PATH?userId={userId}&email={email}")
    fun isVerifiedEmail(
        @Param userId: String,
        @Param email: String,
    ): Mono<IsVerifiedEmailResponse>

    @RequestLine("GET $SUPER_TOKEN_COUNT_USERS_PATH")
    fun countUsers(): Mono<CountUsersResponse>

    class ResetPasswordRequest(
        val token: String,
        val newPassword: String,
    ) {
        val method = "token"
    }

    data class StatusResponse(val status: Status)

    data class ResetTokenResponse(val status: Status, val token: String?)

    data class ResetPasswordResponse(val status: Status, val userId: String?)

    data class SignRequest(
        val email: String,
        val password: String
    )

    data class AccountResponse(val status: Status, val user: User?)

    data class ListUserResponse(val status: Status, val users: Collection<UserWrapper>)

    data class MetadataUpdateRequest(val userId: String, val metadataUpdate: Map<String, Any>)
    data class MetaDataResponse(val status: Status, val metadata: Map<String, Any>)

    data class RoleUsersResponse(val status: Status, val users: Collection<String> = emptyList())

    data class UserWrapper(val user: User)

    data class User(val id: String, val email: String)

    data class UserId(val userId: String)

    data class RoleBinding(val userId: String, val role: String)

    data class CreateRoleRequest(val role: String)

    data class RolesResponse(val roles: Collection<String>)

    data class GenerateJwtRequest(
        val payload: Map<String, Any>,
        val jwksDomain: String,
        val validity: Long
    ) {
        val algorithm = "RS256"
    }

    data class JwtResponse(
        val jwt: String
    )

    data class GenerateEmailVerificationTokenRequest(
        val userId: String,
        val email: String,
    )

    data class GenerateEmailVerificationTokenResponse(
        val status: Status,
        val token: String?,
    )

    data class VerifyEmailRequest(
        val token: String,
    ) {
        val method = "token"
    }

    data class VerifyEmailResponse(
        val status: Status,
        val userId: String?,
        val email: String?,
    )

    data class IsVerifiedEmailResponse(
        val status: Status,
        val isVerified: Boolean?,
    )

    data class CountUsersResponse(
        val status: Status,
        val count: Int,
    )

    enum class Status {
        OK,
        EMAIL_ALREADY_EXISTS_ERROR,
        EMAIL_ALREADY_VERIFIED_ERROR,
        EMAIL_VERIFICATION_INVALID_TOKEN_ERROR,
        WRONG_CREDENTIALS_ERROR,
        UNKNOWN_USER_ID_ERROR,
        RESET_PASSWORD_INVALID_TOKEN_ERROR,
        UNKNOWN_ROLE_ERROR,
        UNKNOWN_EMAIL_ERROR
    }
}
