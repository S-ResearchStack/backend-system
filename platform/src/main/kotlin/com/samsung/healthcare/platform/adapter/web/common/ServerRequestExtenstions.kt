package com.samsung.healthcare.platform.adapter.web.common

import org.springframework.web.reactive.function.server.ServerRequest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun ServerRequest.getProjectId(): String = this.pathVariable("projectId")

fun ServerRequest.getUserId(): String = this.pathVariable("userId")

fun ServerRequest.getStartTime(): LocalDateTime? = this.queryParam("start_time").map {
    LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
}.orElse(null)

fun ServerRequest.getEndTime(): LocalDateTime? = this.queryParam("end_time").map {
    LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
}.orElse(null)

fun ServerRequest.getLastSyncTime(): LocalDateTime? = this.queryParam("last_sync_time").map {
    LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
}.orElse(null)

fun ServerRequest.getRevisionId(): Int? = try {
    this.queryParam("revision_id").map {
        it.toInt()
    }.orElse(null)
} catch (_: NumberFormatException) {
    throw IllegalArgumentException()
}

fun ServerRequest.getTaskId(): String = this.pathVariable("taskId")

fun ServerRequest.getInLabVisitId(): Int = this.pathVariable("inLabVisitId").toIntOrNull()
    ?: throw IllegalArgumentException("illegal inLabVisitId")
