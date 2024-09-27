package researchstack.backend.adapter.incoming.decorator

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtException
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import org.springframework.stereotype.Component
import researchstack.backend.config.OidcGoogleProperties
import java.time.Instant

@Component
class GoogleJwtAuthorizer(
    @Qualifier("google")
    private val reactiveJwtDecoder: ReactiveJwtDecoder,
    private val oidcGoogleProperties: OidcGoogleProperties
) : JwtAuthorizer(reactiveJwtDecoder) {
    override fun validate(jwt: Jwt) {
        jwt.issuedAt ?: throw JwtException("issuedAt is required")
        val expiredAt = jwt.expiresAt ?: throw JwtException("expiredAt is required")
        if (expiredAt.isBefore(Instant.now())) throw JwtException("expired token")
        if (jwt.audience?.contains(oidcGoogleProperties.clientId) != true) throw JwtException("token has invalid client id")
    }
}
