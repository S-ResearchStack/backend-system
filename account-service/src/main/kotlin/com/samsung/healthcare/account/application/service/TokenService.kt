package com.samsung.healthcare.account.application.service

import com.samsung.healthcare.account.application.config.TokenLifetimeProperties
import com.samsung.healthcare.account.application.exception.ExpiredRefreshTokenException
import com.samsung.healthcare.account.application.exception.InvalidTokenException
import com.samsung.healthcare.account.application.port.input.TokenRefreshCommand
import com.samsung.healthcare.account.application.port.input.TokenRefreshResponse
import com.samsung.healthcare.account.application.port.input.TokenRefreshUsecase
import com.samsung.healthcare.account.application.port.output.AuthServicePort
import com.samsung.healthcare.account.application.port.output.JwtGenerationCommand
import com.samsung.healthcare.account.application.port.output.TokenSigningPort
import com.samsung.healthcare.account.application.port.output.TokenStoragePort
import com.samsung.healthcare.account.domain.Account
import com.samsung.healthcare.account.domain.Token
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.time.Instant

@Service
class TokenService(
    private val tokenLifetimeProperties: TokenLifetimeProperties,
    private val tokenSigningPort: TokenSigningPort,
    private val tokenStoragePort: TokenStoragePort,
    private val authServicePort: AuthServicePort,
) : TokenRefreshUsecase {

    internal fun generateToken(account: Account): Mono<Token> = tokenSigningPort.generateSignedJWT(
        JwtGenerationCommand(
            // TODO config issuer
            issuer = "research-hub.com",
            subject = account.id,
            email = account.email.value,
            roles = account.roles,
            lifeTime = tokenLifetimeProperties.accessToken,
        )
    ).map { accessToken ->
        Token.generateToken(account.id, accessToken, tokenLifetimeProperties.refreshToken)
    }.flatMap { token ->
        tokenStoragePort.save(token).thenReturn(token)
    }

    override fun refreshToken(tokenRefreshCommand: TokenRefreshCommand): Mono<TokenRefreshResponse> =
        tokenStoragePort.findByAccessTokenAndRefreshToken(
            tokenRefreshCommand.jwt,
            tokenRefreshCommand.refreshToken
        ).switchIfEmpty { Mono.error(InvalidTokenException()) }
            .filter { token -> token.expiredAt.isAfter(Instant.now()) }
            .switchIfEmpty { Mono.error(ExpiredRefreshTokenException()) }
            .flatMap { token -> authServicePort.getAccount(token.accountId) }
            .flatMap { account -> generateToken(account) }
            .flatMap { token ->
                tokenStoragePort.deleteByRefreshToken(tokenRefreshCommand.refreshToken).subscribe()
                Mono.fromCallable { TokenRefreshResponse(token.accessToken, token.refreshToken) }
            }
}
