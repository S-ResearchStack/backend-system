package com.samsung.healthcare.account.domain

// REF: https://owasp.org/www-community/OWASP_Validation_Regex_Repository
private val EMAIL_REGEX: Regex =
    "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$".toRegex()

data class Email(val value: String) {
    init {
        require(EMAIL_REGEX matches value)
    }
}
