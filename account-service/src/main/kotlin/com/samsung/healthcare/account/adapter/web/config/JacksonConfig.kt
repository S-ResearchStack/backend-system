package com.samsung.healthcare.account.adapter.web.config

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken.VALUE_STRING
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.std.StringDeserializer
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.IOException

@Configuration
class JacksonConfig {
    @Bean
    fun objectMapper(): ObjectMapper =
        jacksonMapperBuilder()
            .addModule(
                SimpleModule().apply {
                    addDeserializer(String::class.java, CoercionLessStringDeserializer())
                }
            )
            .build()

    class CoercionLessStringDeserializer : StringDeserializer() {
        @Throws(IOException::class)
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): String {
            if (p.currentToken != VALUE_STRING) {
                throw MismatchedInputException.from(p, String::class.java, "invalid")
            }
            return super.deserialize(p, ctxt)
        }
    }
}
