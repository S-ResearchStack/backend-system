package com.samsung.healthcare.cloudstorageservice.adapter.web.common

import org.springframework.web.reactive.function.server.ServerRequest

fun ServerRequest.getProjectId(): String = this.pathVariable("projectId")
