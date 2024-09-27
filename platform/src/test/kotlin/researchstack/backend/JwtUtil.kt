package researchstack.backend

import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jwt.JWT
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPublicKey
import java.time.Instant
import java.util.Date

class JwtUtil {
    companion object {
        const val clientId = "test-client-id"

        private val kpg = KeyPairGenerator.getInstance("RSA")

        private val rsaKey: KeyPair by lazy {
            kpg.initialize(2048)
            kpg.generateKeyPair()
        }

        private val jwsHeader = JWSHeader.Builder(JWSAlgorithm.RS256)
            .apply { type(JOSEObjectType.JWT) }
            .build()

        private val jwsSigner = RSASSASigner(rsaKey.private)

        val reactiveJwtDecoder = NimbusReactiveJwtDecoder(rsaKey.public as RSAPublicKey)

        fun generateJWT(
            subject: String,
            clientId: String? = null,
            issueTime: Instant = Instant.now(),
            expirationTime: Instant = Instant.now().plusSeconds(1000)
        ): JWT =
            SignedJWT(jwsHeader, buildClaim(subject, clientId, issueTime, expirationTime))
                .apply { this.sign(jwsSigner) }

        private fun buildClaim(
            subject: String? = null,
            clientId: String? = null,
            issueTime: Instant? = null,
            expirationTime: Instant? = null
        ): JWTClaimsSet {
            return JWTClaimsSet.Builder()
                .apply {
                    subject?.let { subject(it) }
                    issueTime?.let { issueTime(Date.from(it)) }
                    expirationTime?.let { expirationTime(Date.from(it)) }
                    clientId?.let { claim("aud", clientId) }
                }
                .build()
        }
    }
}
