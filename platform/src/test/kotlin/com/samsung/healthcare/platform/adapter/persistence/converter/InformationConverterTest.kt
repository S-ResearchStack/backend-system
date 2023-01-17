package com.samsung.healthcare.platform.adapter.persistence.converter

import com.fasterxml.jackson.databind.ObjectMapper
import com.samsung.healthcare.platform.POSITIVE_TEST
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

internal class InformationConverterTest {
    private val objectMapper = ObjectMapper()

    private val informationReadConverter = JsonReadConverter(objectMapper)
    private val informationWriteConverter = JsonWriteConverter(objectMapper)

    @Test
    @Tag(POSITIVE_TEST)
    fun `convert should return decoded conditions from encoded json`() {
        val information = mapOf(
            "key" to "value",
            "string" to 1,
        )

        val encodedJson = informationWriteConverter.convert(information)
        val decodeInformation = informationReadConverter.convert(encodedJson)
        assertEquals(information, decodeInformation)
    }
}
