package com.samsung.healthcare.trinoruleupdateservice.application.service

import com.samsung.healthcare.trinoruleupdateservice.application.config.ApplicationProperties
import com.samsung.healthcare.trinoruleupdateservice.application.port.output.GetUsersPort
import com.samsung.healthcare.trinoruleupdateservice.application.port.output.UpdateRulePort
import com.samsung.healthcare.trinoruleupdateservice.domain.trino.rule.Rule
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class SchedulingService(
    private val config: ApplicationProperties,
    private val getUsersPort: GetUsersPort,
    private val updateRulePort: UpdateRulePort,
) {
    val logger = KotlinLogging.logger {}

    @Scheduled(fixedDelayString = "\${fixed-delay}")
    fun fetchUsers() {
        logger.info { "Start Updating..." }
        val users = getUsersPort.getUsers()
        logger.info { "${users.size} users found" }
        updateRulePort.updateAccessControlConfigFile(
            Rule.newRule(users, config)
        )
        logger.info { "Finish Updating." }
    }
}
