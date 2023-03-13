package com.samsung.healthcare.platform.application.port.input.project.task

import com.samsung.healthcare.platform.enums.ItemType
import com.samsung.healthcare.platform.enums.TaskStatus
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
                                validateOptionField(it)
                            }
                        }

                        else -> require(false)
                    }
                }

                else -> require(false)
            }
        }

        private fun validateOptionField(option: Map<String, Any>) {
            if (option.containsKey("goToAction")) require(option["goToAction"] is String)
            if (option.containsKey("goToItemId")) require(option["goToItemId"] is String)
        }

        private fun validateContentOptionalField(contents: Map<String, Any>) {
            if (contents.containsKey("required")) require(contents["required"] is Boolean)
            if (contents.containsKey("explanation")) require(contents["explanation"] is String)
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
