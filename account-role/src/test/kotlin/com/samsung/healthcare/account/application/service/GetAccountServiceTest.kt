package com.samsung.healthcare.account.application.service

import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jwt.JWT
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.account.domain.Role
import com.samsung.healthcare.account.domain.Role.ProjectRole.ProjectOwner
import com.samsung.healthcare.account.domain.Role.ProjectRole.Researcher
import com.samsung.healthcare.account.domain.Role.TeamAdmin
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.security.oauth2.jwt.JwtException
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import reactor.kotlin.test.verifyError
import reactor.test.StepVerifier
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPublicKey
import java.time.Instant
import java.util.Date
import java.util.stream.Stream

internal class GetAccountServiceTest {

    private val kpg = KeyPairGenerator.getInstance("RSA")

    private val rsaKey: KeyPair by lazy {
        kpg.initialize(2048)
        kpg.generateKeyPair()
    }

    private val jwsHeader = JWSHeader.Builder(JWSAlgorithm.RS256)
        .apply { type(JOSEObjectType.JWT) }
        .build()

    private val jwsSigner = RSASSASigner(rsaKey.private)

    private val getAccountService =
        GetAccountService(NimbusReactiveJwtDecoder(rsaKey.public as RSAPublicKey))

    private val subject = "account-id"
    private val email = Email("cubist@healthcare-research-hub.test.com")

    @ParameterizedTest
    @MethodSource("providesRoles")
    fun `getAccountFromToken should return account with given roles`(givenRoles: Collection<Role>) {
        val encodedString = generateJWT(subject, email, givenRoles).serialize()
        StepVerifier.create(
            getAccountService.getAccountFromToken(encodedString)
        ).expectNextMatches { account ->
            if (givenRoles.size != account.roles.size) false
            else account.roles.containsAll(givenRoles)
        }.verifyComplete()
    }

    @Test
    fun `getAccountFromToken should return account with given email`() {
        val encodedString = generateJWT(subject, email, emptyList()).serialize()
        StepVerifier.create(
            getAccountService.getAccountFromToken(encodedString)
        ).expectNextMatches { account ->
            account.email == email
        }.verifyComplete()
    }

    @Test
    fun `getAccountFromToken should return account with given subject id`() {
        val encodedString = generateJWT(subject, email, emptyList()).serialize()
        StepVerifier.create(
            getAccountService.getAccountFromToken(encodedString)
        ).expectNextMatches { account ->
            account.id == subject
        }.verifyComplete()
    }

    @Test
    fun `getAccountFromToken should throw JwtException when encoded string is not valid`() {
        val encodedString = SignedJWT(
            jwsHeader,
            JWTClaimsSet.Builder()
                .subject("subject")
                .build()
        ).apply { this.sign(jwsSigner) }
            .serialize()

        StepVerifier.create(
            getAccountService.getAccountFromToken(encodedString)
        ).verifyError<JwtException>()
    }

    private fun generateJWT(subject: String, email: Email, roles: Collection<Role>): JWT =
        SignedJWT(jwsHeader, buildClaim(subject, email, roles))
            .apply { this.sign(jwsSigner) }

    private fun buildClaim(subject: String, email: Email, roles: Collection<Role>): JWTClaimsSet {
        val now = Instant.now()
        return JWTClaimsSet.Builder()
            .apply {
                subject(subject)
                issueTime(Date.from(now))
                expirationTime(Date.from(now.plusSeconds(1000)))
                claim("roles", roles.map { it.roleName })
                claim("email", email.value)
            }
            .build()
    }

    companion object {
        @JvmStatic
        private fun providesRoles() =
            Stream.of(
                Arguments.of(listOf(ProjectOwner("project-x"))),
                Arguments.of(listOf(ProjectOwner("project-y"), Researcher("project-z"))),
                Arguments.of(listOf(TeamAdmin)),
            )
    }
}
