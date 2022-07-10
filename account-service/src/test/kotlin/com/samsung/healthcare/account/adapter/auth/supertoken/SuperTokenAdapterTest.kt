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
import com.samsung.healthcare.account.adapter.auth.supertoken.PathConstant.GET_ACCOUNT_PATH
import com.samsung.healthcare.account.adapter.auth.supertoken.PathConstant.SUPER_TOKEN_ASSIGN_ROLE_PATH
import com.samsung.healthcare.account.adapter.auth.supertoken.PathConstant.SUPER_TOKEN_CREATE_ROLE_PATH
import com.samsung.healthcare.account.adapter.auth.supertoken.PathConstant.SUPER_TOKEN_GENERATE_RESET_TOKEN_PATH
import com.samsung.healthcare.account.adapter.auth.supertoken.PathConstant.SUPER_TOKEN_GET_USER_ROLE_PATH
import com.samsung.healthcare.account.adapter.auth.supertoken.PathConstant.SUPER_TOKEN_REMOVE_USER_ROLE_PATH
import com.samsung.healthcare.account.adapter.auth.supertoken.PathConstant.SUPER_TOKEN_RESET_PASSWORD_PATH
import com.samsung.healthcare.account.adapter.auth.supertoken.PathConstant.SUPER_TOKEN_SIGN_IN_PATH
import com.samsung.healthcare.account.adapter.auth.supertoken.PathConstant.SUPER_TOKEN_SIGN_UP_PATH
import com.samsung.healthcare.account.adapter.auth.supertoken.PathConstant.SUPER_TOKEN_USER_META_DATA_PATH
import com.samsung.healthcare.account.application.exception.AlreadyExistedEmailException
import com.samsung.healthcare.account.application.exception.InvalidResetTokenException
import com.samsung.healthcare.account.application.exception.SignInException
import com.samsung.healthcare.account.application.exception.UnknownAccountIdException
import com.samsung.healthcare.account.application.exception.UnknownEmailException
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.account.domain.Role
import com.samsung.healthcare.account.domain.Role.ProjectRole.HeadResearcher
import com.samsung.healthcare.account.domain.Role.ProjectRole.ProjectOwner
import com.samsung.healthcare.account.domain.Role.ProjectRole.Researcher
import com.samsung.healthcare.account.domain.Role.TeamAdmin
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
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

    @Test
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
            superTokenAdapter.generateResetToken("accountId")
        ).expectNext(resetToken)
            .verifyComplete()
    }

    @Test
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
            superTokenAdapter.generateResetToken("accountId")
        ).verifyError<UnknownAccountIdException>()
    }

    @Test
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
    fun `assignRoles should not emit event when supertoken returns ok`() {
        wm.put {
            url equalTo SUPER_TOKEN_ASSIGN_ROLE_PATH
        } returnsJson {
            body =
                """{
  "status": "OK"
}"""
        }

        StepVerifier.create(
            superTokenAdapter.assignRoles("account-id", listOf(TeamAdmin, ProjectOwner("project-x")))
        ).verifyComplete()
    }

    @Test
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
            superTokenAdapter.assignRoles("account-id", listOf(TeamAdmin, ProjectOwner("project-x")))
        ).verifyError<Exception>()
    }

    @Test
    fun `removeRolesFromAccount should not emit event when supertoken returns ok`() {
        wm.post {
            url equalTo SUPER_TOKEN_REMOVE_USER_ROLE_PATH
        } returnsJson {
            body =
                """{
  "status": "OK"
}"""
        }

        StepVerifier.create(
            superTokenAdapter.removeRolesFromAccount("account-id", listOf(TeamAdmin, ProjectOwner("project-x")))
        ).verifyComplete()
    }

    @Test
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
    fun `listUserRoles should return roles when supertoken returns ok`() {
        val accountId = "account-id"
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
}
