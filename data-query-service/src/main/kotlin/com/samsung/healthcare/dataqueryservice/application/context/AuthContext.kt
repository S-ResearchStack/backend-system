package com.samsung.healthcare.dataqueryservice.application.context

import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

object AuthContext {
    private fun getRequest() = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request

    fun setValue(key: String, value: String) = getRequest().setAttribute(key, value)

    fun getValue(key: String): String? = getRequest().getAttribute(key) as? String

    const val ACCOUNT_ID_KEY_NAME = "account-id"
}
