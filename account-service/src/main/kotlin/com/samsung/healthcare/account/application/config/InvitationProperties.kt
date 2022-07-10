package com.samsung.healthcare.account.application.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("app.invitation")
data class InvitationProperties(
    val url: String
)
