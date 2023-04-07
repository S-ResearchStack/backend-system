package com.samsung.healthcare.branchlogicengine

import com.samsung.healthcare.branchlogicengine.BranchLogicParser.ExpressionContext
import com.samsung.healthcare.branchlogicengine.exception.BranchLogicSyntaxErrorException
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

private fun parse(expression: String, errorListener: BranchLogicErrorListener): ExpressionContext {
    val lexer = BranchLogicLexer(CharStreams.fromString(expression))
    lexer.removeErrorListeners()
    lexer.addErrorListener(errorListener)

    val parser = BranchLogicParser(CommonTokenStream(lexer))
    parser.removeErrorListeners()
    parser.addErrorListener(errorListener)

    return parser.expression()
}

fun validateExpression(expression: String): Set<BranchLogicSyntaxErrorException> {
    val errorListener = BranchLogicErrorListener()
    parse(expression, errorListener)
    return errorListener.getErrors()
}

fun evalExpression(expression: String, variables: Map<String, Any>): Boolean {
    val errorListener = BranchLogicErrorListener()

    val expressionContext = parse(expression, errorListener)

    if (errorListener.getErrors().isNotEmpty()) {
        // if the expression is invalid, return false to avoid unintended question skips.
        return false
    }

    return try {
        val visitor = BranchLogicTreeVisitor(variables)
        visitor.visitExpression(expressionContext)
    } catch (_: RuntimeException) {
        false
    }
}
