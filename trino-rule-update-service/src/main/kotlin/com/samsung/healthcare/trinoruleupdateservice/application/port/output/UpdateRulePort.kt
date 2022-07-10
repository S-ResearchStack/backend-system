package com.samsung.healthcare.trinoruleupdateservice.application.port.output

import com.samsung.healthcare.trinoruleupdateservice.domain.trino.rule.Rule

interface UpdateRulePort {
    fun updateAccessControlConfigFile(rule: Rule)
}
