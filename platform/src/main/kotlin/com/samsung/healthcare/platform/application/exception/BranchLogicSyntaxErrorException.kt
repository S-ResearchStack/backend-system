package com.samsung.healthcare.platform.application.exception

class BranchLogicSyntaxErrorException(
    expression: String,
    message: String
) : RuntimeException("expression '$expression' has error $message")
