package com.ckenergy.cet.kcp

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.backend.jvm.functionByName
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.declarations.IrDeclaration
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.impl.IrFunctionImpl
import org.jetbrains.kotlin.ir.declarations.name
import org.jetbrains.kotlin.ir.expressions.IrBlockBody
import org.jetbrains.kotlin.ir.expressions.IrBody
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrConstKind
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.addArgument
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockBodyImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrConstImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrFunctionExpressionImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrReturnImpl
import org.jetbrains.kotlin.ir.symbols.IrReturnTargetSymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.IrSymbol
import org.jetbrains.kotlin.ir.symbols.impl.IrSimpleFunctionSymbolImpl
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.file
import org.jetbrains.kotlin.ir.util.fileOrNull
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.ir.util.statements
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.SpecialNames

//fun IrPluginContext.printlnFunc(): IrSimpleFunctionSymbol =
//    referenceFunctions(FqName("kotlin.io.println")).single {
//        val parameters = it.owner.valueParameters
//        parameters.size == 1 && parameters[0].type == irBuiltIns.anyNType
//    }

/**
 * 调用[Constants.TRACK_CLASS].trackClick方法
 */
fun IrPluginContext.trackClickFunc(): IrSimpleFunctionSymbol? =
    kotlin.runCatching { referenceClass(FqName(Constants.TRACK_CLASS))?.functionByName("trackClick") }.getOrNull() /*?: printlnFunc()*/

fun IrPluginContext.markRun(): IrSimpleFunctionSymbol = referenceFunctions(FqName("kotlin.run")).first {
    val parameters = it.owner.valueParameters
    parameters.size == 1
}

fun IrFunction.addIrClickable(
    pluginContext: IrPluginContext,
    irBody: IrBody?,
    irDeclaration: IrDeclaration = this,
    symbol1: IrSymbol = this.symbol,
    returnType1: IrType = this.returnType,
    offset: Int = irDeclaration.startOffset
): IrBlockBody {
    return DeclarationIrBuilder(pluginContext, symbol1).irBlockBody {
        addClickTrack(pluginContext, irDeclaration, offset)?.let {
            +it
        } //打印目标函数信息

        +irBlock(resultType = returnType1) {
            irBody?.statements?.forEach{//原有方法体中的表达式
                +it
            }
        }
    }
}

fun irReturn(
    target: IrReturnTargetSymbol,
    value: IrExpression,
    type: IrType = value.type
): IrExpression {
    return IrReturnImpl(
        UNDEFINED_OFFSET,
        UNDEFINED_OFFSET,
        type,
        target,
        value
    )
}

fun IrBuilderWithScope.addClickTrack(
    pluginContext: IrPluginContext,
    function: IrDeclaration,
    offset: Int = function.startOffset
): IrCall? {
    val concat = irConcat() // 拼接目标函数信息 【方法名（）】
    val name = function.parent.kotlinFqName.asString().replaceAfterLast(Constants.LAMBDA_NAME, ".${Constants.CLICKABLE_NAME}")
    val file = function.fileOrNull
    val line = file?.fileEntry?.getLineNumber(offset)?.plus(1)
    val traceInfo = "$name (${file?.name}:$line)"
    Log.w("addClickTrack", traceInfo)
    concat.addArgument(irString(traceInfo))

    val func = pluginContext.trackClickFunc()

    if (func != null)
        return irCall(func).also {
            it.putValueArgument(0, concat)
        }
    return null
}

fun IrPluginContext.addClickTrack(
    function: IrFunction,
    returnType: IrType,
): IrCall? {
    val name = function.kotlinFqName.asString().replaceAfterLast(Constants.LAMBDA_NAME, ".${Constants.CLICKABLE_NAME}")
    val file = function.file.name
    val line = function.file.fileEntry.getLineNumber(function.startOffset) + 1
    val traceInfo = "$name ($file:$line)"
    val concat = irConst(traceInfo) // 拼接目标函数信息 【方法名（）】
    Log.w("addClickTrack", traceInfo)

    val func = trackClickFunc()

    if (func != null)
        return IrCallImpl(
            startOffset = SYNTHETIC_OFFSET,
            endOffset = SYNTHETIC_OFFSET,
            type = returnType,
            symbol = func,
            typeArgumentsCount = 1,
            valueArgumentsCount = 1).also {
            it.putValueArgument(0, concat)
        }
    return null
}

fun IrPluginContext.irConst(value: String): IrConst<String> = IrConstImpl(
    UNDEFINED_OFFSET,
    UNDEFINED_OFFSET,
    irBuiltIns.stringType,
    IrConstKind.String,
    value
)

fun IrPluginContext.createLambda0(
    returnType: IrType,
    functionSymbol: IrSimpleFunctionSymbol = IrSimpleFunctionSymbolImpl(),
    statements: List<IrStatement>
): IrFunctionExpressionImpl {
    return IrFunctionExpressionImpl(
        startOffset = SYNTHETIC_OFFSET,
        endOffset = SYNTHETIC_OFFSET,
        type = irBuiltIns.functionN(0).typeWith(returnType),
        origin = IrStatementOrigin.LAMBDA,
        function = IrFunctionImpl(
            startOffset = SYNTHETIC_OFFSET,
            endOffset = SYNTHETIC_OFFSET,
            origin = IrDeclarationOrigin.LOCAL_FUNCTION_FOR_LAMBDA,
            symbol = functionSymbol,
            name = SpecialNames.ANONYMOUS,
            visibility = DescriptorVisibilities.LOCAL,
            modality = Modality.FINAL,
            returnType = returnType,
            isInline = true,
            isExternal = false,
            isTailrec = false,
            isSuspend = false,
            isOperator = false,
            isInfix = false,
            isExpect = false
        ).apply {
            body = IrBlockBodyImpl(SYNTHETIC_OFFSET, SYNTHETIC_OFFSET, statements)
        }
    )
}