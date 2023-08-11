package com.ckenergy.cet.kcp

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionExpression
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.ir.util.render
import org.jetbrains.kotlin.ir.util.statements

private const val TAG = "ComposeClickTransformer"

class ComposeClickTransformer(
    private val pluginContext: IrPluginContext,
) : IrElementTransformerVoidWithContext() {

    override fun visitCall(expression: IrCall): IrExpression {
        val origin = super.visitCall(expression) as IrCall
        if (expression.symbol.owner.kotlinFqName.asString() == Constants.COMPOSE_CLICK) {
            Log.w(TAG,"StatementTransformer visitCall  ${expression.symbol.owner.kotlinFqName.asString()}:: ${expression.render()}")
            val lambda = origin.getValueArgument(origin.valueArgumentsCount - 1) as? IrFunctionExpression
            if (lambda?.function?.body != null)
                lambda.function.body = lambda.function.addIrClickable(pluginContext, lambda.function.body!!)
        }
        return origin
    }

}