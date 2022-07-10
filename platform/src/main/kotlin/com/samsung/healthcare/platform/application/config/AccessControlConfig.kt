package com.samsung.healthcare.platform.application.config

import com.samsung.healthcare.account.application.accesscontrol.AccessControlAspect
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AccessControlConfig {
    @Bean
    fun accessControlAspect() = AccessControlAspect()
}
