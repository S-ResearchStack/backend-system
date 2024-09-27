package researchstack.backend.adapter.outgoing.supertokens

import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.github.tomakehurst.wiremock.junit5.WireMockExtension
import com.marcinziolo.kotlin.wiremock.equalTo
import com.marcinziolo.kotlin.wiremock.post
import com.marcinziolo.kotlin.wiremock.returnsJson
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.RegisterExtension
import reactivefeign.webclient.WebReactiveFeign
import reactor.kotlin.test.verifyError
import reactor.test.StepVerifier
import researchstack.backend.NEGATIVE_TEST
import researchstack.backend.POSITIVE_TEST
import researchstack.backend.adapter.outgoing.supertokens.PathConstant.SUPER_TOKENS_JWT_PATH
import researchstack.backend.adapter.outgoing.supertokens.PathConstant.SUPER_TOKENS_SIGN_IN_PATH
import researchstack.backend.adapter.outgoing.supertokens.PathConstant.SUPER_TOKENS_SIGN_UP_PATH
import researchstack.backend.application.exception.AlreadyExistsException
import researchstack.backend.application.exception.UnauthorizedException
import researchstack.backend.application.port.outgoing.auth.JwtGenerationCommand
import researchstack.backend.domain.auth.Account
import researchstack.backend.domain.common.Email
import java.util.*

internal class SuperTokensAdapterTest {
    companion object {
        @JvmField
        @RegisterExtension
        val wm = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build()
    }

    private val superTokenAdapter by lazy {
        SuperTokensAdapter(
            WebReactiveFeign.builder<SuperTokensApi>()
                .target(SuperTokensApi::class.java, "http://localhost:${wm.port}")
        )
    }

    private val email = Email("test@research-hub.test.com")
    private val id = UUID.randomUUID().toString()

    private val jwtGenerationCommand = JwtGenerationCommand(
        issuer = "research-hub.test.com",
        subject = "test",
        email = "test@research-hub.test.com",
        lifeTime = 1 * 60 * 60 * 24
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `registerNewUser should work properly`() {
        wm.post {
            url equalTo SUPER_TOKENS_SIGN_UP_PATH
        } returnsJson {
            body =
                """{
    "status": "OK",
    "user": {
        "email": "${email.value}",
        "id": "$id",
        "timeJoined": 1659407200104
    }
}"""
        }

        StepVerifier.create(
            superTokenAdapter.registerNewUser(email, "secret")
        ).expectNext(Account(id, email))
            .verifyComplete()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `registerNewUser should throw AlreadyExistsException when supertokens returns EMAIL_ALREADY_EXISTS_ERROR`() {
        wm.post {
            url equalTo SUPER_TOKENS_SIGN_UP_PATH
        } returnsJson {
            body =
                """{
    "status": "EMAIL_ALREADY_EXISTS_ERROR"
}"""
        }

        StepVerifier.create(
            superTokenAdapter.registerNewUser(email, "secret")
        ).verifyError<AlreadyExistsException>()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `signIn should work properly`() {
        wm.post {
            url equalTo SUPER_TOKENS_SIGN_IN_PATH
        } returnsJson {
            body =
                """{
    "status": "OK",
    "user": {
        "email": "${email.value}",
        "id": "$id",
        "timeJoined": 1659407200104
    }
}"""
        }

        StepVerifier.create(
            superTokenAdapter.signIn(email, "secret")
        ).expectNextMatches { account ->
            account.id == id &&
                account.email == email
        }.verifyComplete()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `signIn should throw UnauthorizedException when supertokens returns WRONG_CREDENTIALS_ERROR`() {
        wm.post {
            url equalTo SUPER_TOKENS_SIGN_IN_PATH
        } returnsJson {
            body =
                """{
    "status": "WRONG_CREDENTIALS_ERROR"
}"""
        }

        StepVerifier.create(
            superTokenAdapter.signIn(email, "secret")
        ).verifyError<UnauthorizedException>()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `generateSignedJwt should work properly`() {
        wm.post {
            url equalTo SUPER_TOKENS_JWT_PATH
        } returnsJson {
            body = """{
  "status": "OK",
  "jwt": "randomjwtasdfasdf"
}"""
        }

        StepVerifier.create(
            superTokenAdapter.generateSignedJwt(jwtGenerationCommand)
        ).expectNext("randomjwtasdfasdf").verifyComplete()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `generateSignedJwt should throw IllegalArgumentException when lifetime is 0`() {
        assertThrows<IllegalArgumentException> {
            superTokenAdapter.generateSignedJwt(
                jwtGenerationCommand.copy(lifeTime = 0)
            )
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `generateSignedJwt should throw IllegalArgumentException when lifetime is negative`() {
        assertThrows<IllegalArgumentException> {
            superTokenAdapter.generateSignedJwt(
                jwtGenerationCommand.copy(lifeTime = -1000)
            )
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `generateSignedJwt should throw IllegalArgumentException when email is not valid`() {
        assertThrows<IllegalArgumentException> {
            superTokenAdapter.generateSignedJwt(
                jwtGenerationCommand.copy(email = ".invalid@invalid.com")
            )
        }
    }
}
