package com.samsung.healthcare.account.adapter.auth.supertoken

import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.github.tomakehurst.wiremock.junit5.WireMockExtension
import com.marcinziolo.kotlin.wiremock.contains
import com.marcinziolo.kotlin.wiremock.equalTo
import com.marcinziolo.kotlin.wiremock.get
import com.marcinziolo.kotlin.wiremock.post
import com.marcinziolo.kotlin.wiremock.put
import com.marcinziolo.kotlin.wiremock.returns
import com.marcinziolo.kotlin.wiremock.returnsJson
import com.samsung.healthcare.account.NEGATIVE_TEST
import com.samsung.healthcare.account.POSITIVE_TEST
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
import com.samsung.healthcare.account.application.exception.AlreadyExistedEmailException
import com.samsung.healthcare.account.application.exception.InvalidEmailVerificationTokenException
import com.samsung.healthcare.account.application.exception.InvalidResetTokenException
import com.samsung.healthcare.account.application.exception.SignInException
import com.samsung.healthcare.account.application.exception.UnknownAccountIdException
import com.samsung.healthcare.account.application.exception.UnknownEmailException
import com.samsung.healthcare.account.application.exception.UnknownRoleException
import com.samsung.healthcare.account.application.port.output.JwtGenerationCommand
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.account.domain.Role
import com.samsung.healthcare.account.domain.Role.ProjectRole.HeadResearcher
import com.samsung.healthcare.account.domain.Role.ProjectRole.ProjectOwner
import com.samsung.healthcare.account.domain.Role.ProjectRole.Researcher
import com.samsung.healthcare.account.domain.Role.TeamAdmin
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.RegisterExtension
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import reactivefeign.utils.HttpStatus
import reactivefeign.webclient.WebReactiveFeign
import reactor.kotlin.test.verifyError
import reactor.test.StepVerifier
import java.net.URLEncoder
import java.util.UUID

internal class SuperTokenAdapterTest {
    @JvmField
    @RegisterExtension
    val wm = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort())
        .build()

    private val superTokenAdapter by lazy {
        SuperTokenAdapter(
            WebReactiveFeign.builder<SuperTokensApi>()
                .target(SuperTokensApi::class.java, "localhost:${wm.port}")
        )
    }

    private val email = Email("cubist@research-hub.tset.com")
    private val id = UUID.randomUUID().toString()

    private val jwtGenerationCommand = JwtGenerationCommand(
        issuer = "research-hub.test.com",
        subject = "test",
        email = "test@research-hub.test.com",
        roles = emptyList(),
        lifeTime = 1 * 60 * 60 * 24,
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `signIn should return account when supertoken returns ok`() {

        wm.post {
            url equalTo SUPER_TOKEN_SIGN_IN_PATH
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

        wm.get {
            url equalTo "$SUPER_TOKEN_GET_USER_ROLE_PATH?userId=$id"
        } returnsJson {
            body =
                """{
  "status": "OK",
   "roles": [ ]
}"""
        }

        wm.get {
            url equalTo "$SUPER_TOKEN_USER_META_DATA_PATH?userId=$id"
        } returnsJson {
            body =
                """{
  "status": "OK",
  "metadata": {
    "preferences": {
      "theme": "dark"
    },
    "notifications": {
      "email": true
    },
    "todos": [
      "example"
    ]
  }
}"""
        }

        StepVerifier.create(
            superTokenAdapter.signIn(email, "secret")
        ).expectNextMatches { account ->
            account.id == id &&
                account.email == email &&
                account.roles.isEmpty() &&
                account.profiles.contains("preferences")
        }.verifyComplete()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `signIn should throw SignInException when supertoken returns 404`() {
        wm.post {
            url equalTo SUPER_TOKEN_SIGN_IN_PATH
        } returns {
            this.statusCode = HttpStatus.SC_NOT_FOUND
        }

        StepVerifier.create(
            superTokenAdapter.signIn(email, "secret")
        ).verifyError<SignInException>()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `signIn should throw SignInException when supertoken returns WRONG_CREDENTIALS_ERROR`() {
        wm.post {
            url equalTo SUPER_TOKEN_SIGN_IN_PATH
        } returnsJson {
            body =
                """{
    "status": "WRONG_CREDENTIALS_ERROR"
}"""
        }

        StepVerifier.create(
            superTokenAdapter.signIn(email, "secret")
        ).verifyError<SignInException>()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `registerNewUser should return account when supertoken returns ok`() {
        wm.post {
            url equalTo SUPER_TOKEN_SIGN_UP_PATH
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
        ).expectNext(Account(id, email, emptyList()))
            .verifyComplete()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `registerNewUser should throw AlreadyExistedEmailEx when supertoken returns EMAIL_ALREADY_EXISTS_ERROR`() {
        wm.post {
            url equalTo SUPER_TOKEN_SIGN_UP_PATH
        } returnsJson {
            body =
                """{
    "status": "EMAIL_ALREADY_EXISTS_ERROR"
}"""
        }

        StepVerifier.create(
            superTokenAdapter.registerNewUser(email, "secret")
        ).verifyError<AlreadyExistedEmailException>()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `generateResetToken should return reset token when supertoken returns ok`() {
        val resetToken = "ZTRiOTBjNz...jI5MTZlODkxw"
        wm.post {
            url equalTo SUPER_TOKEN_GENERATE_RESET_TOKEN_PATH
        } returnsJson {
            body =
                """{
  "status": "OK",
  "token": "$resetToken"
}"""
        }

        StepVerifier.create(
            superTokenAdapter.generateResetToken(UUID.randomUUID().toString())
        ).expectNext(resetToken)
            .verifyComplete()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `generateResetToken should throw UnknownAccountIdException when supertoken returns UNKNOWN_USER_ID_ERROR`() {
        wm.post {
            url equalTo SUPER_TOKEN_GENERATE_RESET_TOKEN_PATH
        } returnsJson {
            body =
                """{
  "status": "UNKNOWN_USER_ID_ERROR"
}"""
        }

        StepVerifier.create(
            superTokenAdapter.generateResetToken(UUID.randomUUID().toString())
        ).verifyError<UnknownAccountIdException>()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `generateResetToken with email should return reset token when supertoken returns ok`() {
        val resetToken = "ZTRiOTBjNz...jI5MTZlODkxw"
        wm.post {
            url equalTo SUPER_TOKEN_GENERATE_RESET_TOKEN_PATH
        } returnsJson {
            body =
                """{
  "status": "OK",
  "token": "$resetToken"
}"""
        }

        wm.get {
            url equalTo "$GET_ACCOUNT_PATH?email=${URLEncoder.encode(email.value, "utf-8")}"
        } returnsJson {
            body =
                """{
  "status": "OK",
  "user": {
    "email": "$email",
    "id": "$id",
    "timeJoined": 1659407200104
    }
}
"""
        }

        StepVerifier.create(
            superTokenAdapter.generateResetToken(email)
        ).expectNext(resetToken)
            .verifyComplete()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `generateResetToken with email throws UnknownEmailException`() {
        wm.get {
            url equalTo "$GET_ACCOUNT_PATH?email=${URLEncoder.encode(email.value, "utf-8")}"
        } returnsJson {
            body =
                """{
  "status": "UNKNOWN_EMAIL_ERROR"
}
"""
        }

        StepVerifier.create(
            superTokenAdapter.generateResetToken(email)
        ).verifyError<UnknownEmailException>()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `resetPassword should not emit event when supertoken returns ok`() {
        wm.post {
            url equalTo SUPER_TOKEN_RESET_PASSWORD_PATH
        } returnsJson {
            body =
                """{
  "status": "OK"
}"""
        }

        StepVerifier.create(
            superTokenAdapter.resetPassword("resetToken", "new-pw")
        ).verifyComplete()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `resetPassword should throw InvalidResetTokenEx event when supertoken returns RESET_PW_INVALID_TOKEN_ERROR`() {
        wm.post {
            url equalTo SUPER_TOKEN_RESET_PASSWORD_PATH
        } returnsJson {
            body =
                """{
  "status": "RESET_PASSWORD_INVALID_TOKEN_ERROR"
}"""
        }

        StepVerifier.create(
            superTokenAdapter.resetPassword("resetToken", "new-pw")
        ).verifyError<InvalidResetTokenException>()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `assignRoles should not emit event when supertoken returns ok`() {
        val accountId = UUID.randomUUID().toString()
        wm.get {
            url equalTo "$GET_ACCOUNT_PATH?userId=$accountId"
        } returnsJson {
            body =
                """{
  "status": "OK",
  "user": {
    "email": "$email",
    "id": "$accountId",
    "timeJoined": 1659407200104
    }
}
"""
        }

        wm.put {
            url equalTo SUPER_TOKEN_ASSIGN_ROLE_PATH
        } returnsJson {
            body =
                """{
  "status": "OK"
}"""
        }

        StepVerifier.create(
            superTokenAdapter.assignRoles(accountId, listOf(TeamAdmin, ProjectOwner("project-x")))
        ).verifyComplete()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `assignRoles should throw UnknownRoleException when supertoken returns UNKNOWN_ROLE_ERROR`() {
        val accountId = UUID.randomUUID().toString()
        wm.get {
            url equalTo "$GET_ACCOUNT_PATH?userId=$accountId"
        } returnsJson {
            body =
                """{
  "status": "OK",
  "user": {
    "email": "$email",
    "id": "$accountId",
    "timeJoined": 1659407200104
    }
}
"""
        }

        wm.put {
            url equalTo SUPER_TOKEN_ASSIGN_ROLE_PATH
        } returnsJson {
            body =
                """{
  "status": "UNKNOWN_ROLE_ERROR"
}
"""
        }

        StepVerifier.create(
            superTokenAdapter.assignRoles(accountId, listOf(TeamAdmin, ProjectOwner("project-x")))
        ).verifyError<UnknownRoleException>()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `assignRolesWithEmail should not emit event when supertoken returns an account and ok`() {
        val email = "cubist@test.com"
        val encodedEmail = URLEncoder.encode(email, "utf-8")
        val accountId = UUID.randomUUID().toString()
        wm.get {
            url equalTo "$GET_ACCOUNT_PATH?email=$encodedEmail"
        } returnsJson {
            body =
                """{
  "status": "OK",
  "user": {
    "email": "$email",
    "id": "$accountId",
    "timeJoined": 1659407200104
    }
}
"""
        }

        wm.get {
            url equalTo "$GET_ACCOUNT_PATH?userId=$accountId"
        } returnsJson {
            body =
                """{
  "status": "OK",
  "user": {
    "email": "$email",
    "id": "$accountId",
    "timeJoined": 1659407200104
    }
}
"""
        }

        wm.put {
            url equalTo SUPER_TOKEN_ASSIGN_ROLE_PATH
            body contains "userId" equalTo accountId
        } returnsJson {
            body =
                """{
  "status": "OK"
}"""
        }
        StepVerifier.create(
            superTokenAdapter.assignRoles(Email(email), listOf(TeamAdmin, ProjectOwner("project-x")))
        ).verifyComplete()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `assignRolesWithEmail should throw UnknownEmailException when supertoken returns UnknownEmailError`() {
        val email = "cubist@test.com"
        val encodedEmail = URLEncoder.encode(email, "utf-8")
        wm.get {
            url equalTo "$GET_ACCOUNT_PATH?email=$encodedEmail"
        } returnsJson {
            body =
                """{
  "status": "UNKNOWN_EMAIL_ERROR"
}
"""
        }

        StepVerifier.create(
            superTokenAdapter.assignRoles(Email(email), listOf(TeamAdmin, ProjectOwner("project-x")))
        ).verifyError<UnknownEmailException>()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `assignRoles should throw exception when supertoken returns some error`() {
        wm.put {
            url equalTo SUPER_TOKEN_ASSIGN_ROLE_PATH
            body contains "role" equalTo Role.TEAM_ADMIN
        } returns {

            this.statusCode = HttpStatus.SC_BAD_REQUEST
        }

        wm.put {
            url equalTo SUPER_TOKEN_ASSIGN_ROLE_PATH
            body contains "role" equalTo ProjectOwner("project-x").roleName
        } returns {
            this.statusCode = HttpStatus.SC_OK
        }

        StepVerifier.create(
            superTokenAdapter.assignRoles(
                UUID.randomUUID().toString(), listOf(TeamAdmin, ProjectOwner("project-x"))
            )
        ).verifyError<Exception>()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `removeRolesFromAccount should not emit event when supertoken returns ok`() {
        val accountId = UUID.randomUUID().toString()

        wm.get {
            url equalTo "$GET_ACCOUNT_PATH?userId=$accountId"
        } returnsJson {
            body =
                """{
  "status": "OK",
  "user": {
    "email": "$email",
    "id": "$accountId",
    "timeJoined": 1659407200104
    }
}
"""
        }

        wm.post {
            url equalTo SUPER_TOKEN_REMOVE_USER_ROLE_PATH
        } returnsJson {
            body =
                """{
  "status": "OK"
}"""
        }

        StepVerifier.create(
            superTokenAdapter.removeRolesFromAccount(accountId, listOf(TeamAdmin, ProjectOwner("project-x")))
        ).verifyComplete()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `createRoles should not emit event when supertoken returns ok`() {
        wm.put {
            url equalTo SUPER_TOKEN_CREATE_ROLE_PATH
        } returnsJson {
            body =
                """{
  "status": "OK"
}"""
        }

        val projectId = "project-x"
        StepVerifier.create(
            superTokenAdapter.createRoles(
                listOf(
                    ProjectOwner(projectId),
                    Researcher(projectId),
                    HeadResearcher(projectId)
                )
            )
        ).verifyComplete()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `listUserRoles should return roles when supertoken returns ok`() {
        val accountId = UUID.randomUUID().toString()
        val projectRole = HeadResearcher("project-x")
        wm.get {
            url equalTo "$SUPER_TOKEN_GET_USER_ROLE_PATH?userId=$accountId"
        } returnsJson {
            body =
                """{
  "status": "OK",
   "roles": [
              "${TeamAdmin.roleName}",
              "${projectRole.roleName}"
            ]
}"""
        }

        StepVerifier.create(
            superTokenAdapter.listUserRoles(accountId)
        ).expectNext(listOf(TeamAdmin, projectRole))
            .verifyComplete()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `listUserRoles should throw IllegalArgumentException when account-id is empty`() {
        assertThrows<IllegalArgumentException> {
            superTokenAdapter.listUserRoles("")
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `registerNewUser should throw IllegalArgumentException when password is empty`() {
        assertThrows<IllegalArgumentException> {
            superTokenAdapter.registerNewUser(Email("test@test.com"), "")
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `generateResetToken should throw IllegalArgumentException when user-id is empty`() {
        assertThrows<IllegalArgumentException> {
            superTokenAdapter.generateResetToken("")
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `resetPassword should throw IllegalArgumentException when resetToken is empty`() {
        assertThrows<IllegalArgumentException> {
            superTokenAdapter.resetPassword("", "secret!@#!")
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `resetPassword should throw IllegalArgumentException when newPassword is empty`() {
        assertThrows<IllegalArgumentException> {
            superTokenAdapter.resetPassword("reset-otken", "")
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `assignRolesWithEmail should throw IllegalArgumentException when roles is empty`() {
        assertThrows<IllegalArgumentException> {
            superTokenAdapter.assignRoles(Email("test@test.com"), emptyList())
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `assignRolesWithAccountId should throw IllegalArgumentException when roles is empty`() {
        assertThrows<IllegalArgumentException> {
            superTokenAdapter.assignRoles(UUID.randomUUID().toString(), emptyList())
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `assignRolesWithAccountId should throw IllegalArgumentException when accountId is empty`() {
        assertThrows<IllegalArgumentException> {
            superTokenAdapter.assignRoles("", listOf(TeamAdmin))
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `removeRolesFromAccount should throw IllegalArgumentException when roles is empty`() {
        assertThrows<IllegalArgumentException> {
            superTokenAdapter.removeRolesFromAccount(UUID.randomUUID().toString(), emptyList())
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `removeRolesFromAccount should throw IllegalArgumentException when accountId is empty`() {
        assertThrows<IllegalArgumentException> {
            superTokenAdapter.removeRolesFromAccount("", listOf(TeamAdmin))
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `createRoles should throw IllegalArgumentException when roles is empty`() {
        assertThrows<IllegalArgumentException> {
            superTokenAdapter.createRoles(emptyList())
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `signIn should throw IllegalArgumentException when password is empty`() {
        assertThrows<IllegalArgumentException> {
            superTokenAdapter.signIn(email, "")
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `retrieveUsersAssociatedWithRoles should throw IllegalArgumentException when projectRoles is empty`() {
        assertThrows<IllegalArgumentException> {
            superTokenAdapter.retrieveUsersAssociatedWithRoles(emptyList())
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `updateAccountProfile should throw IllegalArgumentException when projectRoles is empty`() {
        assertThrows<IllegalArgumentException> {
            superTokenAdapter.updateAccountProfile("", emptyMap())
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `generateSignedJWT should throw IllegalArgumentException when lifetime is 0`() {
        assertThrows<IllegalArgumentException> {
            superTokenAdapter.generateSignedJWT(
                jwtGenerationCommand.copy(lifeTime = 0)
            )
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `generateSignedJWT should throw IllegalArgumentException when lifetime is negative`() {
        assertThrows<IllegalArgumentException> {
            superTokenAdapter.generateSignedJWT(
                jwtGenerationCommand.copy(lifeTime = -1000)
            )
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `generateSignedJWT should throw IllegalArgumentException when email is not valid`() {
        assertThrows<IllegalArgumentException> {
            superTokenAdapter.generateSignedJWT(
                jwtGenerationCommand.copy(email = ".invalid@invalid.com")
            )
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `listUsers should return all accounts`() {
        val accountId = UUID.randomUUID().toString()
        val projectRole = HeadResearcher("project-x")

        wm.get {
            url equalTo SUPER_TOKEN_LIST_USERS_PATH
        } returnsJson {
            body =
                """{
  "status": "OK",
  "users": [
    {
      "user": {
        "email": "${email.value}",
        "id": "$accountId",
        "timeJoined": 1659407200104
      },
      "recipeId": "emailpassword"
    }
  ]
}"""
        }

        wm.get {
            url equalTo "$SUPER_TOKEN_GET_USER_ROLE_PATH?userId=$accountId"
        } returnsJson {
            body =
                """{
  "status": "OK",
  "roles": [ "${projectRole.roleName}" ]
}"""
        }

        wm.get {
            url equalTo "$SUPER_TOKEN_USER_META_DATA_PATH?userId=$accountId"
        } returnsJson {
            body =
                """{
  "status": "OK",
  "metadata": {}
}"""
        }

        StepVerifier.create(
            superTokenAdapter.listUsers()
        ).expectNext(
            listOf(
                Account(accountId, email, listOf(projectRole))
            )
        ).verifyComplete()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `retrieveUsersAssociatedWithRoles should return users with roles`() {
        val headResearcherRole = HeadResearcher("project-x")

        val headResearcherAccount =
            Account(UUID.randomUUID().toString(), Email("email1@research-hub.test.com"), listOf(headResearcherRole))

        wm.get {
            url equalTo
                "$SUPER_TOKEN_GET_ROLE_USER_PATH?role=${URLEncoder.encode(headResearcherRole.roleName, "utf-8")}"
        } returnsJson {
            body = """{
  "status": "OK",
  "users": [
    "${headResearcherAccount.id}"
  ]
}"""
        }

        wm.get {
            url equalTo "$GET_ACCOUNT_PATH?userId=${headResearcherAccount.id}"
        } returnsJson {
            body =
                """{
  "status": "OK",
  "user": {
    "email": "${headResearcherAccount.email.value}",
    "id": "${headResearcherAccount.id}",
    "timeJoined": 1659407200104
    }
}
"""
        }

        wm.get {
            url equalTo "$SUPER_TOKEN_GET_USER_ROLE_PATH?userId=${headResearcherAccount.id}"
        } returnsJson {
            body =
                """{
  "status": "OK",
   "roles": ["${headResearcherRole.roleName}"]
}"""
        }

        wm.get {
            url equalTo "$SUPER_TOKEN_USER_META_DATA_PATH?userId=${headResearcherAccount.id}"
        } returnsJson {
            body =
                """{
  "status": "OK",
  "metadata": {}
}"""
        }

        StepVerifier.create(
            superTokenAdapter.retrieveUsersAssociatedWithRoles(listOf(headResearcherRole))
        ).expectNext(listOf(headResearcherAccount)).verifyComplete()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `generateSignedJWT should generate a token`() {

        wm.post {
            url equalTo SUPER_TOKEN_JWT_PATH
        } returnsJson {
            body = """{
  "status": "OK",
  "jwt": "randomjwtasdfasdf"
}"""
        }

        StepVerifier.create(
            superTokenAdapter.generateSignedJWT(jwtGenerationCommand)
        ).expectNext("randomjwtasdfasdf").verifyComplete()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `generateEmailVerificationToken with id and email should return token string when supertoken returns ok`() {
        val token = "token"

        wm.post {
            url equalTo SUPER_TOKEN_GENERATE_EMAIL_VERIFICATION_TOKEN_PATH
        } returnsJson {
            body =
                """{
    "status": "OK",
    "token": "$token"
}"""
        }

        StepVerifier.create(
            superTokenAdapter.generateEmailVerificationToken(id, email)
        ).expectNext(token)
            .verifyComplete()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `generateEmailVerificationToken with id and email throws AlreadyExistedEmailException`() {
        wm.post {
            url equalTo SUPER_TOKEN_GENERATE_EMAIL_VERIFICATION_TOKEN_PATH
        } returnsJson {
            body =
                """{
    "status": "EMAIL_ALREADY_VERIFIED_ERROR"
}"""
        }

        StepVerifier.create(
            superTokenAdapter.generateEmailVerificationToken(id, email)
        ).verifyError<AlreadyExistedEmailException>()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `generateEmailVerificationToken with email should return token string when supertoken returns ok`() {
        val token = "token"

        wm.post {
            url equalTo SUPER_TOKEN_GENERATE_EMAIL_VERIFICATION_TOKEN_PATH
        } returnsJson {
            body =
                """{
    "status": "OK",
    "token": "$token"
}"""
        }

        wm.get {
            url equalTo "$GET_ACCOUNT_PATH?email=${URLEncoder.encode(email.value, "utf-8")}"
        } returnsJson {
            body =
                """{
  "status": "OK",
  "user": {
    "email": "$email",
    "id": "$id",
    "timeJoined": 1659407200104
    }
}
"""
        }

        StepVerifier.create(
            superTokenAdapter.generateEmailVerificationToken(email)
        ).expectNext(token)
            .verifyComplete()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `generateEmailVerificationToken with email throws UnknownEmailException`() {
        wm.get {
            url equalTo "$GET_ACCOUNT_PATH?email=${URLEncoder.encode(email.value, "utf-8")}"
        } returnsJson {
            body =
                """{
  "status": "UNKNOWN_EMAIL_ERROR"
}
"""
        }

        StepVerifier.create(
            superTokenAdapter.generateEmailVerificationToken(email)
        ).verifyError<UnknownEmailException>()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `generateEmailVerificationToken with email throws AlreadyExistedEmailException`() {
        wm.post {
            url equalTo SUPER_TOKEN_GENERATE_EMAIL_VERIFICATION_TOKEN_PATH
        } returnsJson {
            body =
                """{
    "status": "EMAIL_ALREADY_VERIFIED_ERROR"
}"""
        }

        wm.get {
            url equalTo "$GET_ACCOUNT_PATH?email=${URLEncoder.encode(email.value, "utf-8")}"
        } returnsJson {
            body =
                """{
  "status": "OK",
  "user": {
    "email": "$email",
    "id": "$id",
    "timeJoined": 1659407200104
    }
}
"""
        }

        StepVerifier.create(
            superTokenAdapter.generateEmailVerificationToken(email)
        ).verifyError<AlreadyExistedEmailException>()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `verifyEmail should return account when supertoken returns ok`() {
        wm.post {
            url equalTo SUPER_TOKEN_VERIFY_EMAIL_PATH
        } returnsJson {
            body =
                """{
  "status": "OK",
  "userId": "$id",
  "email": "${email.value}"
}"""
        }

        wm.get {
            url equalTo "$SUPER_TOKEN_GET_USER_ROLE_PATH?userId=$id"
        } returnsJson {
            body =
                """{
  "status": "OK",
   "roles": []
}"""
        }

        wm.get {
            url equalTo "$SUPER_TOKEN_USER_META_DATA_PATH?userId=$id"
        } returnsJson {
            body =
                """{
  "status": "OK",
  "metadata": {
    "todos": [
      "example"
    ]
  }
}"""
        }

        StepVerifier.create(
            superTokenAdapter.verifyEmail("token")
        ).expectNextMatches { account ->
            account.id == id &&
                account.email == email &&
                account.roles.isEmpty() &&
                account.profiles.contains("todos")
        }.verifyComplete()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should throw InvalidEmailVerificationTokenE when supertoken returns EMAIL_VERIFICATION_INVALID_TOKEN_ERROR`() {
        wm.post {
            url equalTo SUPER_TOKEN_VERIFY_EMAIL_PATH
        } returnsJson {
            body =
                """{
    "status": "EMAIL_VERIFICATION_INVALID_TOKEN_ERROR"
}"""
        }

        StepVerifier.create(
            superTokenAdapter.verifyEmail("token")
        ).verifyError<InvalidEmailVerificationTokenException>()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `verifyEmail should throw IllegalArgumentException when userId was empty`() {
        wm.post {
            url equalTo SUPER_TOKEN_VERIFY_EMAIL_PATH
        } returnsJson {
            body =
                """{
  "status": "OK",
  "email": "${email.value}"
}"""
        }

        wm.get {
            url equalTo "$SUPER_TOKEN_GET_USER_ROLE_PATH?userId=$id"
        } returnsJson {
            body =
                """{
  "status": "OK",
   "roles": []
}"""
        }

        wm.get {
            url equalTo "$SUPER_TOKEN_USER_META_DATA_PATH?userId=$id"
        } returnsJson {
            body =
                """{
  "status": "OK",
  "metadata": {}
}"""
        }

        StepVerifier.create(
            superTokenAdapter.verifyEmail("token")
        )
            .expectError(IllegalArgumentException::class.java)
            .verify()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `verifyEmail should throw IllegalArgumentException when email was empty`() {
        wm.post {
            url equalTo SUPER_TOKEN_VERIFY_EMAIL_PATH
        } returnsJson {
            body =
                """{
  "status": "OK",
  "userId": "$id"
}"""
        }

        wm.get {
            url equalTo "$SUPER_TOKEN_GET_USER_ROLE_PATH?userId=$id"
        } returnsJson {
            body =
                """{
  "status": "OK",
   "roles": []
}"""
        }

        wm.get {
            url equalTo "$SUPER_TOKEN_USER_META_DATA_PATH?userId=$id"
        } returnsJson {
            body =
                """{
  "status": "OK",
  "metadata": {}
}"""
        }

        StepVerifier.create(
            superTokenAdapter.verifyEmail("token")
        )
            .expectError(IllegalArgumentException::class.java)
            .verify()
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    @Tag(POSITIVE_TEST)
    fun `isVerifiedEmail should return boolean when supertoken returns ok`(isVerified: Boolean) {
        wm.get {
            url equalTo "$SUPER_TOKEN_VERIFY_EMAIL_PATH" +
                "?userId=${URLEncoder.encode(id, "utf-8")}&email=${URLEncoder.encode(email.value, "utf-8")}"
        } returnsJson {
            body =
                """{
    "status": "OK",
    "isVerified": $isVerified
}"""
        }

        StepVerifier.create(
            superTokenAdapter.isVerifiedEmail(id, email)
        ).expectNext(isVerified)
            .verifyComplete()
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 2, 10, 100])
    @Tag(POSITIVE_TEST)
    fun `countUsers should return the number of users when supertoken returns ok`(count: Int) {
        wm.get {
            url equalTo SUPER_TOKEN_COUNT_USERS_PATH
        } returnsJson {
            body =
                """{
  "status": "OK",
  "count": $count
}"""
        }

        StepVerifier.create(
            superTokenAdapter.countUsers()
        ).expectNext(count)
            .verifyComplete()
    }
}
