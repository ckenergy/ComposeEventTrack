package com.ckenergy.cet.kcp

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.impl.IrSimpleFunctionSymbolImpl
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.impl.IrSimpleTypeImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.ir.util.render

private const val TAG = "ComposeClickTransformer"

class ComposeClickTransformer(
    private val pluginContext: IrPluginContext,
) : IrElementTransformerVoidWithContext() {

    override fun visitCall(expression: IrCall): IrExpression {
        val origin = super.visitCall(expression) as IrCall
        if (expression.symbol.owner.kotlinFqName.asString() == Constants.COMPOSE_CLICK) {
            Log.w(
                TAG,
                "StatementTransformer visitCall  ${expression.symbol.owner.kotlinFqName.asString()}:: ${expression.render()}"
            )
            val value = origin.getValueArgument(origin.valueArgumentsCount - 1)
            var track = false
            if (value is IrFunctionExpression) {
                value.function.let {
                    if (it.body != null) {
                        track = true
                        it.body = it.addIrClickable(pluginContext, it.body)
                    } else if (it is IrSimpleFunctionSymbol) {
                        track = true
                        val call = IrCallImpl(
                            startOffset = SYNTHETIC_OFFSET,
                            endOffset = SYNTHETIC_OFFSET,
                            type = it.returnType,
                            symbol = it,
                            typeArgumentsCount = 1,
                            valueArgumentsCount = 0
                        )
                        val newBlock = pluginContext.createLambda0(it.returnType, it, listOf(call))
                        newBlock.function.body =
                            it.addIrClickable(pluginContext, newBlock.function.body)
                        origin.putValueArgument(origin.valueArgumentsCount - 1, newBlock)
                    }
                }
            } else if (value is IrGetValueImpl) {
                Log.d(TAG, "in IrGetValueImpl")
                val type = (value.type as? IrSimpleTypeImpl)?.arguments?.lastOrNull() as? IrType
                if (type != null) {
                    track = true
                    val calculationFunSymbol = IrSimpleFunctionSymbolImpl()
                    val call = IrCallImpl(
                        startOffset = SYNTHETIC_OFFSET,
                        endOffset = SYNTHETIC_OFFSET,
                        type = type,
                        symbol = pluginContext.markRun(),
                        typeArgumentsCount = 1,
                        valueArgumentsCount = 1
                    ).apply {
                        putValueArgument(0, value)
                    }
                    val newBlock = pluginContext.createLambda0(
                        type,
                        calculationFunSymbol,
                        listOf(irReturn(calculationFunSymbol, call))
                    )
                    val func = expression.symbol.owner
                    newBlock.function.body = func.addIrClickable(
                        pluginContext,
                        newBlock.function.body,
                        value.symbol.owner,
                        newBlock.function.symbol,
                        newBlock.function.returnType,
                        origin.startOffset
                    )
                    origin.putValueArgument(origin.valueArgumentsCount - 1, newBlock)
                }
            }
            if (!track) {
                Log.w(TAG, "can't track clickable $value")
            }
        }
        return origin
    }

}