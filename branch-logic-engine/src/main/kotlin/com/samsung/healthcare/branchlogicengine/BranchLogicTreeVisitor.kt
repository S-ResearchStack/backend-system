package com.samsung.healthcare.branchlogicengine

import com.samsung.healthcare.branchlogicengine.exception.UnknownContextException

class BranchLogicTreeVisitor(val variables: Map<String, Any>) : BranchLogicBaseVisitor<Boolean>() {

    override fun visitExpression(ctx: BranchLogicParser.ExpressionContext): Boolean {
        return super.visitExpression(ctx)
    }

    override fun visitLogicalExpression(ctx: BranchLogicParser.LogicalExpressionContext): Boolean =
        if (ctx.op.AND() != null) { visit(ctx.left) && visit(ctx.right) } else visit(ctx.left) || visit(ctx.right)

    override fun visitComparisonExpression(ctx: BranchLogicParser.ComparisonExpressionContext): Boolean {
        val leftValue = visitLeftValue(ctx.left)
        val rightValue = ctx.right.text.toFloatOrNull()
            ?: ctx.right.text.substring(1, ctx.right.text.length - 1).toFloatOrNull()
            ?: ctx.right.text.substring(1, ctx.right.text.length - 1)
        val isFloat = (leftValue is Float) && (rightValue is Float)

        return when {
            ctx.op.GT() != null && isFloat -> leftValue as Float > rightValue as Float
            ctx.op.GTE() != null && isFloat -> leftValue as Float >= rightValue as Float
            ctx.op.LT() != null && isFloat -> (leftValue as Float) < rightValue as Float
            ctx.op.LTE() != null && isFloat -> leftValue as Float <= rightValue as Float
            ctx.op.EQ() != null -> leftValue == rightValue
            else -> leftValue != rightValue
        }
    }

    override fun visitMembershipExpression(ctx: BranchLogicParser.MembershipExpressionContext): Boolean {
        val leftValue = visitLeftValue(ctx.left).toString().split(",").map { it.toFloatOrNull() ?: it }
        val rightValue = ctx.right.text.toFloatOrNull() ?: ctx.right.text

        return if (ctx.op.CONTAINS() != null) rightValue in leftValue
        else rightValue !in leftValue
    }

    private fun visitLeftValue(ctx: BranchLogicParser.IdentifierContext): Any {
        val variableKey = ctx.text.replace("cnt", "val")
        val value = variables[variableKey]?.toString() ?: throw UnknownContextException(variableKey)

        return if (ctx.VAR_ANS() != null) value.toFloatOrNull() ?: value
        else value.split(",").size.toFloat()
    }
}
