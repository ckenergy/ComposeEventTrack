package com.ckenergy.cet.kcp

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.backend.jvm.functionByName
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.name
import org.jetbrains.kotlin.ir.expressions.IrBlockBody
import org.jetbrains.kotlin.ir.expressions.IrBody
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.addArgument
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.util.file
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.ir.util.statements
import org.jetbrains.kotlin.name.FqName

//fun IrPluginContext.printlnFunc(): IrSimpleFunctionSymbol =
//    referenceFunctions(FqName("kotlin.io.println")).single {
//        val parameters = it.owner.valueParameters
//        parameters.size == 1 && parameters[0].type == irBuiltIns.anyNType
//    }

/**
 * 调用[Constants.SENSOR_TRACK_CLASS].trackClick方法
 */
fun IrPluginContext.sensorTrackClickFunc(): IrSimpleFunctionSymbol? =
    kotlin.runCatching { referenceClass(FqName(Constants.SENSOR_TRACK_CLASS))?.functionByName("trackClick") }.getOrNull() /*?: printlnFunc()*/

fun IrFunction.addIrClickable(
    pluginContext: IrPluginContext,
    irBody: IrBody
): IrBlockBody {
    return DeclarationIrBuilder(pluginContext, symbol).irBlockBody {
        addClickTrack(pluginContext, this@addIrClickable)?.let {
            +it
        } //打印目标函数信息

        +irBlock(resultType = this@addIrClickable.returnType) {
            for (statement in irBody.statements) { //原有方法体中的表达式
                +statement
            }
        }
    }
}

fun IrBuilderWithScope.addClickTrack(
    pluginContext: IrPluginContext,
    function: IrFunction
): IrCall? {
    val concat = irConcat() // 拼接目标函数信息 【方法名（）】
    val name = function.kotlinFqName.asString().replaceAfterLast(Constants.LAMBDA_NAME, ".clickable")
    val file = function.file.name
    val line = function.file.fileEntry.getLineNumber(function.startOffset) + 1
    val traceInfo = "$name ($file:$line)"
    Log.w("addClickTrack", traceInfo)
    concat.addArgument(irString(traceInfo))

    val func = pluginContext.sensorTrackClickFunc()

    if (func != null)
        return irCall(func).also {
            it.putValueArgument(0, concat)
        }
    return null
}