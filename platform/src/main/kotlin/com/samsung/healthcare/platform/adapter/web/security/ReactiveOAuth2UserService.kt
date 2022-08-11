package com.samsung.healthcare.platform.adapter.web.security

import org.springframework.security.oauth2.client.userinfo.DefaultReactiveOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

internal const val REGISTRATION_ID_KEY = "registrationId"
internal const val EMAIL_KEY = "email"
internal const val SUBJECT_KEY = "sub"

@Component
class ReactiveOAuth2UserService : DefaultReactiveOAuth2UserService() {
    override fun loadUser(userRequest: OAuth2UserRequest): Mono<OAuth2User> =
        super.loadUser(userRequest)
            .map { oAuth2User ->
                DefaultOAuth2User(
                    oAuth2User.authorities,
                    buildMap {
                        putAll(oAuth2User.attributes)
                        put(REGISTRATION_ID_KEY, userRequest.clientRegistration.registrationId)
                    },
                    EMAIL_KEY
                )
            }
}
