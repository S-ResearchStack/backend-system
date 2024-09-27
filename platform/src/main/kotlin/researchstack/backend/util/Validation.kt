package researchstack.backend.util

import researchstack.backend.adapter.exception.ExceptionMessage

fun validateContext(value: String?, exceptionMessage: String) {
    require(!value.isNullOrBlank()) { exceptionMessage }
}

fun validateContextList(list: List<String>, exceptionMessage: String) {
    require(list.isNotEmpty()) { exceptionMessage }
    require(list.none { it.isBlank() }) { exceptionMessage }
}

fun validateGoogleAuthCode(code: String? = null): String {
    require(!code.isNullOrBlank()) { ExceptionMessage.EMPTY_AUTH_CODE }
    return code
}

inline fun <reified T : Enum<T>> validateEnum(enum: String, exceptionMessage: String) {
    require(enumValues<T>().any { it.name == enum }) { exceptionMessage }
}
