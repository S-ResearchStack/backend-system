package com.samsung.healthcare.branchlogicengine.exception

class UnknownContextException(variable: String) : RuntimeException("Variable unknown or unset: $variable")
