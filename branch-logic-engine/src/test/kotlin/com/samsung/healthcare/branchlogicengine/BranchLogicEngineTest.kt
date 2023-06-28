package com.samsung.healthcare.branchlogicengine

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

internal class BranchLogicEngineTest {

    @Test
    @Tag(POSITIVE_TEST)
    fun `validateExpression should return empty set with valid expressions`() {
        assert(validateExpression("gt cnt2 5").isEmpty())
        assert(validateExpression("eq val1 1.1").isEmpty())
        assert(validateExpression("eq val3 -0.2").isEmpty())
        assert(validateExpression("eq val3 \"ha\"").isEmpty())
        assert(validateExpression("and eq val1 1 neq val2 2").isEmpty())
        assert(validateExpression("contains val3 \"ha\"").isEmpty())
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `validateExpression should return a set of BranchLogicSyntaxErrorExceptions`() {
        assert(validateExpression("eq eq eq").isNotEmpty())
        assert(validateExpression("and eq eq").isNotEmpty())
        assert(validateExpression("eqn val1 1").isNotEmpty())
        assert(validateExpression("contains eq eq").isNotEmpty())
        assert(validateExpression("contains val1 val2").isNotEmpty())
        assert(validateExpression("notcontains eq eq").isNotEmpty())
        assert(validateExpression("notcontains val1 val2").isNotEmpty())
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `evalExpression should work properly`() {
        assertEquals(true, evalExpression("eq val1 1", mapOf("val1" to "1")))
        assertEquals(true, evalExpression("lt val1 1", mapOf("val1" to "0")))
        assertEquals(true, evalExpression("lte val1 1", mapOf("val1" to "0")))
        assertEquals(true, evalExpression("neq val1 1", mapOf("val1" to "3")))
        assertEquals(true, evalExpression("gte val1 1", mapOf("val1" to "2")))

        assertEquals(true, evalExpression("gt cnt1 2", mapOf("val1" to "1,2,3")))
        assertEquals(true, evalExpression("gte cnt1 2", mapOf("val1" to "1,2,3")))
        assertEquals(true, evalExpression("lt cnt1 2", mapOf("val1" to "1")))
        assertEquals(true, evalExpression("lte cnt1 2", mapOf("val1" to "1")))
        assertEquals(true, evalExpression("neq cnt1 2", mapOf("val1" to "1,2,3")))
        assertEquals(true, evalExpression("neq cnt1 \"2\"", mapOf("val1" to "1,2,3")))
        assertEquals(true, evalExpression("neq cnt1 \"2\"", mapOf("val1" to "1.0")))

        assertEquals(true, evalExpression("eq val1 1.1", mapOf("val1" to 1.1)))
        assertEquals(true, evalExpression("contains val1 1", mapOf("val1" to "1,2,3")))

        assertEquals(true, evalExpression("notcontains cnt1 \"1\"", mapOf("val1" to "1.0")))
        assertEquals(true, evalExpression("notcontains val1 1", mapOf("val1" to 3)))
        assertEquals(true, evalExpression("notcontains val1 \"1\"", mapOf("val1" to 1)))
        assertEquals(true, evalExpression("notcontains val1 9", mapOf("val1" to "1,2,3")))
        assertEquals(true, evalExpression("or eq val1 1 eq val2 2", mapOf("val1" to "9", "val2" to "2")))
        assertEquals(true, evalExpression("and eq val1 1 eq val2 2", mapOf("val1" to "1", "val2" to "2")))

        assertEquals(true, evalExpression("eq val1 1.00", mapOf("val1" to "1")))
        assertEquals(true, evalExpression("eq val1 1", mapOf("val1" to "1.00")))
        assertEquals(true, evalExpression("eq val1 1.00", mapOf("val1" to "1.0000")))
        assertEquals(true, evalExpression("eq val1 1.00", mapOf("val1" to 1)))
        assertEquals(true, evalExpression("eq val1 1", mapOf("val1" to 1.0)))

        assertEquals(true, evalExpression("eq val1 \"test\"", mapOf("val1" to "test")))
        assertEquals(true, evalExpression("eq val1 \"\"", mapOf("val1" to "")))
        assertEquals(true, evalExpression("gt val1 \"0\"", mapOf("val1" to "1")))
        assertEquals(true, evalExpression("contains val1 \"I don't know\"", mapOf("val1" to "bad,I don't know")))
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `evalExperssion should return false properly`() {
        assertEquals(false, evalExpression("gt val1 1", mapOf("val1" to "0")))
        assertEquals(false, evalExpression("gte val1 1", mapOf("val1" to "0")))
        assertEquals(false, evalExpression("lt val1 1", mapOf("val1" to "2")))
        assertEquals(false, evalExpression("lte val1 1", mapOf("val1" to "2")))
        assertEquals(false, evalExpression("neq val1 1", mapOf("val1" to "1")))

        assertEquals(false, evalExpression("eq val1 1", mapOf("val1" to "9")))
        assertEquals(false, evalExpression("contains val1 9", mapOf("val1" to "1,2,3")))
        assertEquals(false, evalExpression("notcontains val1 1", mapOf("val1" to 1)))
        assertEquals(false, evalExpression("notcontains val1 1", mapOf("val1" to "1,2,3")))
        assertEquals(false, evalExpression("or eq val1 1 eq val2 2", mapOf("val1" to "9", "val2" to "9")))
        assertEquals(false, evalExpression("and eq val1 1 eq val2 2", mapOf("val1" to "9", "val2" to "2")))
        assertEquals(false, evalExpression("eq eq val1 1 eq val2 2", mapOf("val1" to "9", "val2" to "2")))
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `evalExpression should return false with invalid expressions`() {
        assertEquals(false, evalExpression("eq val1 v", emptyMap()))
        assertEquals(false, evalExpression("zxcv eq val1 1 eq val2 2", mapOf("val1" to "9", "val2" to "2")))
        assertEquals(false, evalExpression("and eq val1 1 eq vax2 2", mapOf("val1" to "9", "val2" to "2")))
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `evalExpression should return false with invalid context variables`() {
        assertEquals(false, evalExpression("zxcv eq val1 1 eq val2 2", mapOf("val1" to "9", "val2" to "2")))
        assertEquals(false, evalExpression("eq val1 1.00", mapOf("val1" to "asdf")))
        assertEquals(false, evalExpression("eq val1 1.00", mapOf("val1" to "1.0abc")))
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `antlr generated codes should work properly`() {
        val expression = "eq val1 0"

        val lexer = BranchLogicLexer(CharStreams.fromString(expression))
        val lexerErrorListener = BranchLogicErrorListener()
        lexer.removeErrorListeners()
        lexer.addErrorListener(lexerErrorListener)

        val parser = BranchLogicParser(CommonTokenStream(lexer))
        val parserErrorListener = BranchLogicErrorListener()
        parser.removeErrorListeners()
        parser.addErrorListener(parserErrorListener)

        val expressionContext = parser.expression()

        assertEquals("java-escape", parser.grammarFileName)
        assert(parser.ruleNames is Array<String>)
        assert(parser.serializedATN is String)

        assertNull(expressionContext.logicalExpression())
        assertNotNull(expressionContext.comparisonExpression())
        assertNull(expressionContext.membershipExpression())
        assertEquals(0, expressionContext.ruleIndex)

        val listener = BranchLogicBaseListener()
        val tree = ParseTreeWalker()
        tree.walk(listener, expressionContext)

        expressionContext.enterRule(listener)
        expressionContext.exitRule(listener)

        val logicalExpressionContext = parser.logicalExpression()
        logicalExpressionContext.expression()
        logicalExpressionContext.expression(0)
        logicalExpressionContext.ruleIndex
        logicalExpressionContext.enterRule(listener)
        logicalExpressionContext.exitRule(listener)

        val logicalContext = logicalExpressionContext.logicalOperator()
        logicalContext.AND()
        logicalContext.OR()

        val membershipExpressionContext = parser.membershipExpression()
        membershipExpressionContext.identifier()
        membershipExpressionContext.ruleIndex
        membershipExpressionContext.enterRule(listener)
        membershipExpressionContext.exitRule(listener)

        val membershipOperatorContext = membershipExpressionContext.membershipOperator()
        membershipOperatorContext.CONTAINS()
        membershipOperatorContext.NCONTAINS()

        val comparisonExpressionContext = parser.comparisonExpression()
        comparisonExpressionContext.identifier()
        comparisonExpressionContext.ruleIndex
        comparisonExpressionContext.enterRule(listener)
        comparisonExpressionContext.exitRule(listener)

        val comparisonOperatorContext = comparisonExpressionContext.comparisonOperator()
        comparisonOperatorContext.EQ()
        comparisonOperatorContext.NEQ()
        comparisonOperatorContext.GT()
        comparisonOperatorContext.GTE()
        comparisonOperatorContext.LT()
        comparisonOperatorContext.LTE()
    }
}
