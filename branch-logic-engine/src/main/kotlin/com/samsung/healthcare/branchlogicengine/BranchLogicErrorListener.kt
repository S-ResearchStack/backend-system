package com.samsung.healthcare.branchlogicengine

import com.samsung.healthcare.branchlogicengine.exception.BranchLogicSyntaxErrorException
import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer

class BranchLogicErrorListener : BaseErrorListener() {
    private val errorSet = mutableSetOf<BranchLogicSyntaxErrorException>()

    override fun syntaxError(
        recognizer: Recognizer<*, *>?,
        offendingSymbol: Any?,
        line: Int,
        charPositionInLine: Int,
        msg: String?,
        e: RecognitionException?
    ) {
        errorSet.add(BranchLogicSyntaxErrorException(charPositionInLine, msg ?: ""))
    }

    fun getErrors(): Set<BranchLogicSyntaxErrorException> = errorSet.toSet()
}
