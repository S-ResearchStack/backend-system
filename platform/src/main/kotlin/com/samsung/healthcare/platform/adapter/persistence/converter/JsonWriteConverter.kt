package com.samsung.healthcare.platform.adapter.persistence.converter

import com.fasterxml.jackson.databind.ObjectMapper
import io.r2dbc.postgresql.codec.Json
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter

@WritingConverter
class JsonWriteConverter(
    private val objectMapper: ObjectMapper
) : Converter<Map<String, Any>, Json> {
    override fun convert(conditions: Map<String, Any>): Json =
        Json.of(objectMapper.writeValueAsString(conditions))
}
