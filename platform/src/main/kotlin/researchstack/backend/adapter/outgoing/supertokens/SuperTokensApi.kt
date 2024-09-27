package researchstack.backend.adapter.outgoing.supertokens

import feign.Headers
import feign.Param
import feign.RequestLine
import reactor.core.publisher.Mono
import researchstack.backend.adapter.outgoing.supertokens.PathConstant.SUPER_TOKENS_JWT_PATH
import researchstack.backend.adapter.outgoing.supertokens.PathConstant.SUPER_TOKENS_SIGN_IN_PATH
import researchstack.backend.adapter.outgoing.supertokens.PathConstant.SUPER_TOKENS_SIGN_UP_PATH
import researchstack.backend.adapter.outgoing.supertokens.PathConstant.SUPER_TOKENS_USER_META_DATA_PATH

@Headers("Accept: application/json")
interface SuperTokensApi {
    @RequestLine("POST $SUPER_TOKENS_SIGN_IN_PATH")
    @Headers("Content-Type: application/json")
    fun signIn(signInRequest: SignRequest): Mono<AccountResponse>

    @RequestLine("POST $SUPER_TOKENS_SIGN_UP_PATH")
    @Headers("Content-Type: application/json")
    fun signUp(signUpRequest: SignRequest): Mono<AccountResponse>

    @RequestLine("POST $SUPER_TOKENS_JWT_PATH")
    @Headers("Content-Type: application/json")
    fun generateSignedJwt(generateJwtRequest: GenerateJwtRequest): Mono<JwtResponse>

    @RequestLine("GET $SUPER_TOKENS_USER_META_DATA_PATH?userId={userId}")
    fun getMetaData(@Param userId: String): Mono<MetaDataResponse>

    data class SignRequest(
        val email: String,
        val password: String
    )

    data class AccountResponse(val status: Status, val user: User?)

    data class MetaDataResponse(val status: Status, val metadata: Map<String, Any>)

    data class User(val id: String, val email: String)

    data class UserId(val userId: String)

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
