package researchstack.backend.adapter.incoming.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import researchstack.backend.config.JwkProperties

@Configuration
class JwtDecoderConfig(
    private val jwkProperties: JwkProperties
) {
    @Bean("samsung-account")
    fun getSamsungAccountJwtDecoder(): ReactiveJwtDecoder =
        NimbusReactiveJwtDecoder(jwkProperties.samsungAccount)

    @Bean("google")
    fun getGoogleJwtDecoder(): ReactiveJwtDecoder =
        NimbusReactiveJwtDecoder(jwkProperties.google)

    @Bean("cognito")
    fun getCognitoJwtDecoder(): ReactiveJwtDecoder =
        NimbusReactiveJwtDecoder(jwkProperties.cognito)

    @Bean("super-tokens")
    fun getSuperTokensJwtDecoder(): ReactiveJwtDecoder =
        NimbusReactiveJwtDecoder(jwkProperties.superTokens)
}
