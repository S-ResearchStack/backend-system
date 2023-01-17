package com.samsung.healthcare.dataqueryservice.adapter.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.graphql.dgs.DgsQueryExecutor
import com.netflix.graphql.dgs.mvc.DgsRestController
import com.samsung.healthcare.dataqueryservice.application.context.AuthContext
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.WebRequest
import org.springframework.web.multipart.MultipartFile

internal const val PROJECT_ID = "projectId"

@RestController
@RequestMapping("/api/projects")
class GraphqlRestController(dgsQueryExecutor: DgsQueryExecutor, mapper: ObjectMapper) {
    private val dgsRestController = DgsRestController(dgsQueryExecutor, mapper)

    @RequestMapping(
        "/{projectId}/graphql",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun graphql(
        @PathVariable("projectId") projectId: String,
        @RequestBody body: ByteArray?,
        @RequestParam fileParams: Map<String, MultipartFile>?,
        @RequestParam(name = "operations") operation: String?,
        @RequestParam(name = "map") mapParam: String?,
        @RequestHeader headers: HttpHeaders,
        webRequest: WebRequest
    ): ResponseEntity<Any> {
        //
        headers[PROJECT_ID] = projectId
        headers[AuthContext.ACCOUNT_ID_KEY_NAME] = AuthContext.getValue(AuthContext.ACCOUNT_ID_KEY_NAME)
        return dgsRestController.graphql(body, fileParams, operation, mapParam, headers, webRequest)
    }
}
