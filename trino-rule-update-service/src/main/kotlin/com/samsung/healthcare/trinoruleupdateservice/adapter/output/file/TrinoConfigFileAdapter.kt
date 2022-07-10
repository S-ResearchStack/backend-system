package com.samsung.healthcare.trinoruleupdateservice.adapter.output.file

import com.google.gson.GsonBuilder
import com.samsung.healthcare.trinoruleupdateservice.application.config.ApplicationProperties
import com.samsung.healthcare.trinoruleupdateservice.application.port.output.UpdateRulePort
import com.samsung.healthcare.trinoruleupdateservice.domain.trino.rule.Rule
import org.springframework.stereotype.Component
import java.io.FileWriter

@Component
class TrinoConfigFileAdapter(
    private val config: ApplicationProperties,
) : UpdateRulePort {
    override fun updateAccessControlConfigFile(rule: Rule) {
        FileWriter(config.trino.accessControl.configFilePath).use { fw ->
            GsonBuilder().setPrettyPrinting().create().toJson(rule, fw)
            fw.flush()
        }
    }
}
