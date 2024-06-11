package com.samsung.healthcare.platform.application.port.input.project.task

import com.samsung.healthcare.branchlogicengine.validateExpression
import com.samsung.healthcare.platform.application.exception.BranchLogicSyntaxErrorException
import com.samsung.healthcare.platform.enums.ActivityType
import com.samsung.healthcare.platform.enums.ItemType
import com.samsung.healthcare.platform.enums.TaskStatus
import com.samsung.healthcare.platform.enums.TaskType
import org.quartz.CronExpression
import java.time.LocalDateTime

data class UpdateTaskCommand(
    val title: String,
    val description: String? = null,
    val schedule: String? = null,
    val startTime: LocalDateTime? = null,
    var endTime: LocalDateTime? = null,
    val validTime: Int? = null,
    val status: TaskStatus,
    val type: TaskType,
    val items: List<UpdateItemCommand>,
    val condition: Map<String, Any> = emptyMap(),
) {
    companion object {
        private const val DEFAULT_END_TIME_MONTH: Long = 3
    }

    init {
        require(title.isNotBlank())
        if (status == TaskStatus.PUBLISHED) {
            requireNotNull(schedule)
            require(CronExpression.isValidExpression(schedule))
            requireNotNull(startTime)
            requireNotNull(validTime)
            if (endTime == null)
                endTime = startTime.plusMonths(DEFAULT_END_TIME_MONTH)
            require(items.isNotEmpty())
        }
        if (this.type == TaskType.SURVEY) {
            require(items.all { it.type == ItemType.QUESTION || it.type == ItemType.SECTION })

            if (items.any { it.type == ItemType.SECTION }) {
                require(items.first().type == ItemType.SECTION)
                require(items.last().type == ItemType.QUESTION)

                require(
                    items.windowed(2).all { it[0].type == ItemType.QUESTION || it[1].type == ItemType.QUESTION }
                )
            }
        } else require(items.all { it.type == ItemType.ACTIVITY })
    }

    data class UpdateItemCommand(
        val contents: Map<String, Any>,
        val type: ItemType,
        val sequence: Int?,
    ) {
        init {
            requireNotNull(sequence)
            try {
                requireValidContents()
            } catch (e: NullPointerException) {
                throw IllegalArgumentException()
            } catch (e: ClassCastException) {
                throw IllegalArgumentException()
            }
        }

        @Suppress("UNCHECKED_CAST")
        private fun requireValidContents() {
            when (this.type) {
                ItemType.QUESTION -> {
                    when (contents["type"]) {
                        "CHOICE" -> {
                            require(contents["title"] is String)
                            validateContentOptionalField(contents)
                            val props = contents["properties"] as Map<*, *>
                            require(
                                listOf("RADIO", "CHECKBOX", "DROPDOWN", "IMAGE", "MULTIIMAGE").contains(props["tag"])
                            )
                            val options = props["options"] as List<Map<String, Any>>
                            val optionKeys = options.distinctBy { it.keys }.single().keys
                            if (props["tag"] == "IMAGE" || props["tag"] == "MULTIIMAGE") {
                                require(optionKeys == setOf("value") || optionKeys == setOf("value", "label"))
                            } else {
                                require(optionKeys == setOf("value"))
                            }
                            require(options.flatMap { it.values }.all { it is String })
                            if (props.containsKey("skip_logic")) {
                                val skipLogics = props["skip_logic"] as List<Map<String, Any>>
                                skipLogics.forEach {
                                    validateSkipLogic(it)
                                }
                            }
                        }
                        "TEXT" -> {
                            require(contents["title"] is String)
                            validateContentOptionalField(contents)
                            val props = contents["properties"] as Map<*, *>
                            require(props["tag"] == "TEXT")
                        }
                        "RANK" -> {
                            require(contents["title"] is String)
                            validateContentOptionalField(contents)
                            val props = contents["properties"] as Map<*, *>
                            require(props["tag"] == "RANK")
                            val options = props["options"] as List<Map<String, Any>>
                            options.forEach {
                                require(it["value"] is String)
                            }
                        }
                        "SCALE" -> {
                            require(contents["title"] is String)
                            validateContentOptionalField(contents)
                            val props = contents["properties"] as Map<*, *>
                            require(props["tag"] == "SLIDER")
                            require(props["low"] is Int && props["high"] is Int)
                            if (props.containsKey("lowLabel")) require(props["lowLabel"] is String)
                            if (props.containsKey("highLabel")) require(props["highLabel"] is String)
                        }
                        "DATETIME" -> {
                            require(contents["title"] is String)
                            validateContentOptionalField(contents)
                            val props = contents["properties"] as Map<*, *>
                            require(props["tag"] == "DATETIME")
                            validateDateTimeOptionalProperties(props)
                        }
                        else -> require(false)
                    }
                }
                ItemType.ACTIVITY -> {
                    require(contents.containsKey("completionTitle"))
                    require(
                        contents.containsKey("type") && ActivityType.values()
                            .contains(ActivityType.valueOf(contents["type"] as String))
                    )
                }
                ItemType.SECTION -> {
                }

                else -> require(false)
            }
        }

        private fun validateSkipLogic(option: Map<String, Any>) {
            require(option.containsKey("condition"))
            require(option["condition"] is String)

            val expressionErrors = validateExpression(option["condition"] as String)
            if (expressionErrors.isNotEmpty()) {
                throw BranchLogicSyntaxErrorException(
                    option["condition"] as String,
                    expressionErrors.first().message ?: ""
                )
            }

            require(option.containsKey("goToItemSequence"))
            require(option["goToItemSequence"] is Int)
        }

        private fun validateContentOptionalField(contents: Map<String, Any>) {
            if (contents.containsKey("required")) require(contents["required"] is Boolean)
            if (contents.containsKey("explanation")) require(contents["explanation"] is String)
            if (contents.containsKey("completionDescription")) require(contents["completionDescription"] is String)
        }

        private fun validateDateTimeOptionalProperties(properties: Map<*, *>) {
            val isTime: Boolean = if (properties.containsKey("isTime")) {
                require(properties["isTime"] is Boolean)
                properties["isTime"] as Boolean
            } else {
                true
            }
            val isDate: Boolean = if (properties.containsKey("isDate")) {
                require(properties["isDate"] is Boolean)
                properties["isDate"] as Boolean
            } else {
                true
            }
            if (properties.containsKey("isRange")) require(properties["isRange"] is Boolean)
            require(isTime || isDate)
        }
    }

    @Suppress("UNCHECKED_CAST")
    val properties = mapOf<String, Any?>(
        "title" to title,
        "description" to description,
        "schedule" to schedule,
        "startTime" to startTime,
        "endTime" to endTime,
        "validTime" to validTime
    ).filterValues { it != null } as Map<String, Any>
}
