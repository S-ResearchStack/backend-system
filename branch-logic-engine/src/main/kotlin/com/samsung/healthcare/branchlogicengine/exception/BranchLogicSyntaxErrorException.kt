package com.samsung.healthcare.branchlogicengine.exception

class BranchLogicSyntaxErrorException(position: Int, message: String) : RuntimeException("position $position: $message")
