package com.samsung.healthcare.platform.application.port.input.project.task

import com.fasterxml.jackson.databind.exc.ValueInstantiationException
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import com.fasterxml.jackson.module.kotlin.readValue
import com.samsung.healthcare.platform.NEGATIVE_TEST
import com.samsung.healthcare.platform.POSITIVE_TEST
import com.samsung.healthcare.platform.enums.ItemType.ITEM
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UpdateTaskCommandTest {

    private val objectMapper = jacksonMapperBuilder().addModule(JavaTimeModule()).build()

    private val validRequest =
        """
        {
          "title": "daily task",
          "description": "hello",
          "schedule": "0 0 12 1/1 * ? *",
          "startTime": "2022-09-12T12:00:00",
          "endTime": "2022-10-20T12:00:00",
          "validTime": 600,
          "status" : "PUBLISHED",
          "type": "SURVEY",
          "items": [
            {
                  "type": "QUESTION",
                  "contents": {
                    "title": "good morning",
                    "required": true,
                    "type": "CHOICE",
                    "properties": {
                      "tag": "RADIO",
                      "options": [
                        {
                          "value": "string"
                        }
                      ]
                    }
                  },
                  "sequence": 0
                }
          ],
          "condition": {}
          }
    """

    private lateinit var requestBody: MutableMap<String, Any>

    @BeforeEach
    fun beforeEach() {
        requestBody = objectMapper.readValue<MutableMap<String, Any>>(validRequest)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should return instance when json sting is valid`() {
        val command = objectMapper.readValue<UpdateTaskCommand>(validRequest)
        assertNotNull(command)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should throw ValueInstantiationException when title is not blank`() {
        requestBody["title"] = "     "
        assertThrows<ValueInstantiationException> {
            objectMapper.readValue<UpdateTaskCommand>(
                objectMapper.writeValueAsString(requestBody)
            )
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should throw ValueInstantiationException when status is published and startTime is not given`() {
        requestBody.remove("startTime")
        assertThrows<ValueInstantiationException> {
            objectMapper.readValue<UpdateTaskCommand>(
                objectMapper.writeValueAsString(requestBody)
            )
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should throw ValueInstantiationException when status is published and schedule is not given`() {
        requestBody.remove("schedule")
        assertThrows<ValueInstantiationException> {
            objectMapper.readValue<UpdateTaskCommand>(
                objectMapper.writeValueAsString(requestBody)
            )
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should throw ValueInstantiationException when status is published and validTime is not given`() {
        requestBody.remove("validTime")
        assertThrows<ValueInstantiationException> {
            objectMapper.readValue<UpdateTaskCommand>(
                objectMapper.writeValueAsString(requestBody)
            )
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    @Suppress("UNCHECKED_CAST")
    fun `should throw ValueInstantiationException when status is published and item type is invalid`() {
        (requestBody["items"] as List<MutableMap<String, Any>>)[0]["type"] = ITEM.toString()
        assertThrows<ValueInstantiationException> {
            objectMapper.readValue<UpdateTaskCommand>(
                objectMapper.writeValueAsString(requestBody)
            )
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `should return instance when items is empty`() {
        requestBody["items"] = emptyList<Any>()
        val command = objectMapper.readValue<UpdateTaskCommand>(
            objectMapper.writeValueAsString(requestBody)
        )
        assertNotNull(command)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    @Suppress("UNCHECKED_CAST")
    fun `should throw ValueInstantiationException when title is not string`() {
        val contents = (requestBody["items"] as List<MutableMap<String, Any>>)[0]["contents"] as MutableMap<String, Any>
        contents["title"] = mapOf("not" to "string")
        assertThrows<ValueInstantiationException> {
            objectMapper.readValue<UpdateTaskCommand>(
                objectMapper.writeValueAsString(requestBody)
            )
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    @Suppress("UNCHECKED_CAST")
    fun `should throw ValueInstantiationException when properties is not object`() {
        val contents = (requestBody["items"] as List<MutableMap<String, Any>>)[0]["contents"] as MutableMap<String, Any>
        contents["properties"] = "properties"
        assertThrows<ValueInstantiationException> {
            objectMapper.readValue<UpdateTaskCommand>(
                objectMapper.writeValueAsString(requestBody)
            )
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    @Suppress("UNCHECKED_CAST")
    fun `should throw ValueInstantiationException when tag is not given`() {
        val contents = (requestBody["items"] as List<MutableMap<String, Any>>)[0]["contents"] as MutableMap<String, Any>
        val props = contents["properties"] as MutableMap<String, Any>
        props.remove("tag")
        assertThrows<ValueInstantiationException> {
            objectMapper.readValue<UpdateTaskCommand>(
                objectMapper.writeValueAsString(requestBody)
            )
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    @Suppress("UNCHECKED_CAST")
    fun `should throw ValueInstantiationException when tag is not valid`() {
        val contents = (requestBody["items"] as List<MutableMap<String, Any>>)[0]["contents"] as MutableMap<String, Any>
        val props = contents["properties"] as MutableMap<String, Any>
        props["tag"] = "invalid-tag"
        assertThrows<ValueInstantiationException> {
            objectMapper.readValue<UpdateTaskCommand>(
                objectMapper.writeValueAsString(requestBody)
            )
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    @Suppress("UNCHECKED_CAST")
    fun `should throw ValueInstantiationException when options is string`() {
        val contents = (requestBody["items"] as List<MutableMap<String, Any>>)[0]["contents"] as MutableMap<String, Any>
        val props = contents["properties"] as MutableMap<String, Any>
        props["options"] = "options-string"
        assertThrows<ValueInstantiationException> {
            objectMapper.readValue<UpdateTaskCommand>(
                objectMapper.writeValueAsString(requestBody)
            )
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    @Suppress("UNCHECKED_CAST")
    fun `should throw ValueInstantiationException when options value is not string`() {
        val contents = (requestBody["items"] as List<MutableMap<String, Any>>)[0]["contents"] as MutableMap<String, Any>
        val props = contents["properties"] as MutableMap<String, Any>
        val opt = (props["options"] as List<MutableMap<String, Any>>)[0]
        opt["value"] = mapOf("not" to "string")
        assertThrows<ValueInstantiationException> {
            objectMapper.readValue<UpdateTaskCommand>(
                objectMapper.writeValueAsString(requestBody)
            )
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    @Suppress("UNCHECKED_CAST")
    fun `should throw ValueInstantiationException when options value is not given`() {
        val contents = (requestBody["items"] as List<MutableMap<String, Any>>)[0]["contents"] as MutableMap<String, Any>
        val props = contents["properties"] as MutableMap<String, Any>
        val opt = (props["options"] as List<MutableMap<String, Any>>)[0]
        opt.remove("value")
        assertThrows<ValueInstantiationException> {
            objectMapper.readValue<UpdateTaskCommand>(
                objectMapper.writeValueAsString(requestBody)
            )
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    @Suppress("UNCHECKED_CAST")
    fun `should throw ValueInstantiationException when explanation is not string`() {
        val contents = (requestBody["items"] as List<MutableMap<String, Any>>)[0]["contents"] as MutableMap<String, Any>
        contents["explanation"] = mapOf("not" to "string")
        assertThrows<ValueInstantiationException> {
            objectMapper.readValue<UpdateTaskCommand>(
                objectMapper.writeValueAsString(requestBody)
            )
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    @Suppress("UNCHECKED_CAST")
    fun `should throw ValueInstantiationException when required is not boolean`() {
        val contents = (requestBody["items"] as List<MutableMap<String, Any>>)[0]["contents"] as MutableMap<String, Any>
        contents["required"] = "not-boolean"
        assertThrows<ValueInstantiationException> {
            objectMapper.readValue<UpdateTaskCommand>(
                objectMapper.writeValueAsString(requestBody)
            )
        }
    }
}
