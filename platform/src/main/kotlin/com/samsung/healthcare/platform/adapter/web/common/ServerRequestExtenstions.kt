package com.samsung.healthcare.platform.adapter.web

import com.samsung.healthcare.platform.application.exception.NotFoundException
import com.samsung.healthcare.platform.domain.User
import com.samsung.healthcare.platform.domain.healthdata.HealthData
import org.springframework.web.reactive.function.server.ServerRequest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun ServerRequest.getProjectId(): String = this.pathVariable("projectId")

fun ServerRequest.getStartDate(): LocalDateTime? = this.queryParam("startDate").map { datetimeString ->
    LocalDateTime.parse(datetimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
}.orElse(null)

fun ServerRequest.getEndDate(): LocalDateTime? = this.queryParam("endDate").map { datetimeString ->
    LocalDateTime.parse(datetimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
}.orElse(null)

fun ServerRequest.getUsers(): List<User.UserId> = this.queryParam("users").map {
    it.split(",").map { User.UserId.from(it) }
}.orElse(emptyList())

fun ServerRequest.getTypes(): List<HealthData.HealthDataType> =
    this.queryParam("types").get().split(",").map {
        HealthData.HealthDataType.fromString(it)
            ?: throw NotFoundException("$it is not supported HealthDataType")
    }
