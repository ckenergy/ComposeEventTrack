package com.ckenergy.cet.kcp

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.jvm.functionByName
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.name.FqName

private const val TAG = "ComposeClickIrGenerationExtension"

class ComposeClickIrGenerationExtension : IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        Log.w(TAG, "compose click kcp kotlin ir plugin")

        val start = System.currentTimeMillis()
        val trackClass =
            pluginContext.referenceClass(FqName(Constants.TRACK_CLASS))
        if (trackClass != null) {
            trackClass.functions.forEach {
                Log.w(TAG, "func:${it.owner.name.asString()}")
            }
            val func = trackClass.functionByName("trackClick")
            Log.w(TAG, "func11:${func}")

            moduleFragment.transform(
                ComposeClickTransformer(pluginContext),
                null
            )
        }else {
            Log.w(
                TAG,
                "Not found `${Constants.TRACK_CLASS}` class, make sure to add the \"io.github.ckenergy:cet-core\" library to your dependencies"
            )
        }


        Log.w(TAG, "kcp use time:${System.currentTimeMillis() - start}")
    }
}