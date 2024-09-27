package researchstack.backend.adapter.incoming.decorator

import com.linecorp.armeria.common.HttpRequest
import com.linecorp.armeria.server.ServiceRequestContext
import com.linecorp.armeria.server.auth.Authorizer
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtException
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import researchstack.backend.application.exception.UnauthorizedException
import researchstack.backend.config.USER_EMAIL_KEY
import researchstack.backend.config.USER_ID_KEY
import java.time.Instant
import java.util.concurrent.CompletionStage

open class JwtAuthorizer(
    private val reactiveJwtDecoder: ReactiveJwtDecoder
) : Authorizer<HttpRequest> {
    private val bearerPrefix = "Bearer "

    override fun authorize(ctx: ServiceRequestContext, data: HttpRequest): CompletionStage<Boolean> {
        val bearerString = data.headers().find {
            it.key.toString().equals("authorization", true)
        }?.value ?: throw UnauthorizedException("no token passed")
        if (!bearerString.startsWith(bearerPrefix)) throw UnauthorizedException("invalid bearer string")

        return reactiveJwtDecoder.decode(bearerString.substring(bearerPrefix.length))
            .map { jwt ->
                validate(jwt)
                ctx.setAttr(USER_ID_KEY, jwt.subject)
                ctx.setAttr(USER_EMAIL_KEY, jwt.getClaim("email") as String?)
                true
            }.toFuture()
    }

    open fun validate(jwt: Jwt) {
        jwt.issuedAt ?: throw JwtException("issuedAt is required")
        val expiredAt = jwt.expiresAt ?: throw JwtException("expiredAt is required")
        if (expiredAt.isBefore(Instant.now())) throw JwtException("expired token")
    }
}
