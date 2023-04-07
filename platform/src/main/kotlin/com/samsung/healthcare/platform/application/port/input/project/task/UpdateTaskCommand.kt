package com.samsung.healthcare.platform.application.port.input.project.task

import com.samsung.healthcare.branchlogicengine.validateExpression
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
        }
        when (this.type) {
            TaskType.SURVEY -> require(items.all { it.type == ItemType.QUESTION || it.type == ItemType.SECTION })
            TaskType.ACTIVITY -> require(items.all { it.type == ItemType.ACTIVITY })
        }
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
                            require(listOf("RADIO", "CHECKBOX", "DROPDOWN").contains(props["tag"]))
                            val options = props["options"] as List<Map<String, Any>>
                            options.forEach {
                                require(it["value"] is String)
                            }
                            if (props.containsKey("skip_logic")) {
                                val skipLogics = props["skip_logic"] as List<Map<String, Any>>
                                skipLogics.forEach {
                                    validateSkipLogic(it)
                                }
                            }
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
            require(validateExpression(option["condition"] as String).isEmpty())
            require(option.containsKey("goToItemSequence"))
            require(option["goToItemSequence"] is Int)
        }

        private fun validateContentOptionalField(contents: Map<String, Any>) {
            if (contents.containsKey("required")) require(contents["required"] is Boolean)
            if (contents.containsKey("explanation")) require(contents["explanation"] is String)
            if (contents.containsKey("completionDescription")) require(contents["completionDescription"] is String)
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
