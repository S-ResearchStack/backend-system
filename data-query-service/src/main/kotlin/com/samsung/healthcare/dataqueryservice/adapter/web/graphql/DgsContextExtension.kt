package com.samsung.healthcare.dataqueryservice.adapter.web.graphql

import com.netflix.graphql.dgs.context.DgsContext
import com.samsung.healthcare.dataqueryservice.adapter.web.PROJECT_ID
import com.samsung.healthcare.dataqueryservice.application.context.AuthContext

internal object DgsContextExtension {
    fun DgsContext.getProjectId(): String =
        this.requestData?.headers?.get(PROJECT_ID)?.first() ?: throw IllegalArgumentException("require projectId")

    fun DgsContext.getAccount(): String =
        this.requestData?.headers?.get(AuthContext.ACCOUNT_ID_KEY_NAME)?.first()
            ?: throw IllegalArgumentException("require account")
}
