package com.samsung.healthcare.account.application.service

import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSSigner
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jwt.JWT
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import com.samsung.healthcare.account.NEGATIVE_TEST
import com.samsung.healthcare.account.POSITIVE_TEST
import com.samsung.healthcare.account.domain.Email
import com.samsung.healthcare.account.domain.Role
import com.samsung.healthcare.account.domain.Role.ProjectRole.ProjectOwner
import com.samsung.healthcare.account.domain.Role.ProjectRole.Researcher
import com.samsung.healthcare.account.domain.Role.TeamAdmin
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.security.oauth2.jwt.BadJwtException
import org.springframework.security.oauth2.jwt.JwtException
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import reactor.kotlin.test.verifyError
import reactor.test.StepVerifier
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPublicKey
import java.time.Instant
import java.time.temporal.ChronoUnit
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
    private val issueTime = Instant.now()
    private val expirationTime = issueTime.plus(1, ChronoUnit.HOURS)

    @ParameterizedTest
    @MethodSource("providesRoles")
    @Tag(POSITIVE_TEST)
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
    @Tag(NEGATIVE_TEST)
    fun `getAccountFromToken should throw JwtException when role is invalid`() {
        val encodedString = SignedJWT(
            jwsHeader,
            JWTClaimsSet.Builder()
                .apply {
                    subject(subject)
                    issueTime(Date.from(issueTime))
                    expirationTime(Date.from(expirationTime))
                    claim("roles", listOf("invalid"))
                    claim("email", email.value)
                }
                .build()
        )
            .apply { this.sign(jwsSigner) }
            .serialize()

        StepVerifier.create(
            getAccountService.getAccountFromToken(encodedString)
        ).verifyError<JwtException>()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getAccountFromToken should throw JwtException when email is invalid`() {
        val encodedString = SignedJWT(
            jwsHeader,
            JWTClaimsSet.Builder()
                .apply {
                    subject(subject)
                    issueTime(Date.from(issueTime))
                    expirationTime(Date.from(expirationTime))
                    claim("roles", listOf<String>())
                    claim("email", "invalid-email")
                }
                .build()
        )
            .apply { this.sign(jwsSigner) }
            .serialize()

        StepVerifier.create(
            getAccountService.getAccountFromToken(encodedString)
        ).verifyError<JwtException>()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getAccountFromToken should return account with given email`() {
        val encodedString = generateJWT(subject, email, emptyList()).serialize()
        StepVerifier.create(
            getAccountService.getAccountFromToken(encodedString)
        ).expectNextMatches { account ->
            account.email == email
        }.verifyComplete()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getAccountFromToken should return account with given subject id`() {
        val encodedString = generateJWT(subject, email, emptyList()).serialize()
        StepVerifier.create(
            getAccountService.getAccountFromToken(encodedString)
        ).expectNextMatches { account ->
            account.id == subject
        }.verifyComplete()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getAccountFromToken should throw JwtException when subject is not given`() {
        val encodedString =
            buildClaim(email = email, roles = emptyList(), issueTime = issueTime, expirationTime = expirationTime)
                .toEncodedString()

        StepVerifier.create(
            getAccountService.getAccountFromToken(encodedString)
        ).verifyError<JwtException>()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getAccountFromToken should throw JwtException when email is not set`() {
        val encodedString =
            buildClaim(subject = subject, roles = emptyList(), issueTime = issueTime, expirationTime = expirationTime)
                .toEncodedString()

        StepVerifier.create(
            getAccountService.getAccountFromToken(encodedString)
        ).verifyError<JwtException>()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getAccountFromToken should throw JwtException when role is not set`() {
        val encodedString =
            buildClaim(subject = subject, email = email, issueTime = issueTime, expirationTime = expirationTime)
                .toEncodedString()

        StepVerifier.create(
            getAccountService.getAccountFromToken(encodedString)
        ).verifyError<JwtException>()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getAccountFromToken should throw JwtException when expirationTime is not set`() {
        val encodedString =
            buildClaim(subject = subject, email = email, roles = emptyList(), issueTime = issueTime)
                .toEncodedString()

        StepVerifier.create(
            getAccountService.getAccountFromToken(encodedString)
        ).verifyError<JwtException>()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getAccountFromToken should throw JwtException when expirationTime is expired`() {
        val encodedString =
            buildClaim(
                subject = subject,
                email = email,
                roles = emptyList(),
                issueTime = issueTime,
                expirationTime = issueTime.minus(100L, ChronoUnit.DAYS)
            )
                .toEncodedString()
        StepVerifier.create(
            getAccountService.getAccountFromToken(encodedString)
        ).verifyError<JwtException>()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getAccountFromToken should throw BadJwtException when signer is not matched`() {
        val anotherSigner = RSASSASigner(kpg.generateKeyPair().private)

        val encodedString =
            buildClaim(subject = subject, email = email, roles = emptyList(), issueTime = issueTime)
                .toEncodedString(anotherSigner)

        assertThrows<BadJwtException> {
            getAccountService.getAccountFromToken(encodedString)
        }
    }

    private fun generateJWT(
        subject: String,
        email: Email,
        roles: Collection<Role>,
        issueTime: Instant = Instant.now(),
        expirationTime: Instant = Instant.now().plusSeconds(1000)
    ): JWT =
        SignedJWT(jwsHeader, buildClaim(subject, email, roles, issueTime, expirationTime))
            .apply { this.sign(jwsSigner) }

    private fun buildClaim(
        subject: String? = null,
        email: Email? = null,
        roles: Collection<Role>? = null,
        issueTime: Instant? = null,
        expirationTime: Instant? = null
    ): JWTClaimsSet {
        return JWTClaimsSet.Builder()
            .apply {
                subject?.let { subject(it) }
                issueTime?.let { issueTime(Date.from(it)) }
                expirationTime?.let { expirationTime(Date.from(it)) }
                roles?.let {
                    claim("roles", roles.map { it.roleName })
                }
                email?.let { claim("email", it.value) }
            }
            .build()
    }

    private fun JWTClaimsSet.toEncodedString(signer: JWSSigner = jwsSigner) =
        SignedJWT(
            jwsHeader,
            this
        ).apply { this.sign(signer) }
            .serialize()

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
