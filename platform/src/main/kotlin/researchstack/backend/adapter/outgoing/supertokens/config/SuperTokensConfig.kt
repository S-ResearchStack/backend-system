package researchstack.backend.adapter.outgoing.supertokens.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactivefeign.webclient.WebReactiveFeign
import researchstack.backend.adapter.outgoing.supertokens.SuperTokensApi

@Configuration
class SuperTokensConfig {

    @Value("\${super-tokens.url}")
    private var url = "http://localhost:3567"

    @Bean
    fun superTokensApi() =
        WebReactiveFeign.builder<SuperTokensApi>()
            .target(SuperTokensApi::class.java, url)
}
