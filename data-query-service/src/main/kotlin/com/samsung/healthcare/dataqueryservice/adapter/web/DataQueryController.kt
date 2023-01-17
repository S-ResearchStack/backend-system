package com.samsung.healthcare.dataqueryservice.adapter.web

import com.samsung.healthcare.dataqueryservice.application.context.AuthContext
import com.samsung.healthcare.dataqueryservice.application.port.input.QueryDataCommand
import com.samsung.healthcare.dataqueryservice.application.port.input.QueryDataResultSet
import com.samsung.healthcare.dataqueryservice.application.port.input.QueryDataUseCase
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/projects")
class DataQueryController(
    private val queryDataUseCase: QueryDataUseCase,
) {
    @PostMapping("/{projectId}/sql")
    fun executeQuery(
        @PathVariable("projectId") projectId: String,
        @RequestBody queryCommand: QueryDataCommand,
    ): QueryDataResultSet {
        return queryDataUseCase.execute(
            projectId,
            AuthContext.getValue(AuthContext.ACCOUNT_ID_KEY_NAME),
            queryCommand,
        )
    }
}
