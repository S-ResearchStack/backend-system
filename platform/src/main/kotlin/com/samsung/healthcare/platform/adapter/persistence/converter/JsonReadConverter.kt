package com.samsung.healthcare.platform.adapter.persistence.converter

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.r2dbc.postgresql.codec.Json
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter

@ReadingConverter
class JsonReadConverter(
    private val objectMapper: ObjectMapper
) : Converter<Json, Map<String, Any>> {
    override fun convert(json: Json): Map<String, Any> =
        objectMapper.readValue(json.asString())
}
