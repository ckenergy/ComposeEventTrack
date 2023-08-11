package com.ckenergy.cet.kcp

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.config.CompilerConfiguration

@AutoService(CommandLineProcessor::class)
class CETCommandLineProcessor : CommandLineProcessor {

    // 配置 Kotlin 插件唯一 ID
    override val pluginId: String = "cet-kcp"

    // 读取 `SubpluginOptions` 参数，并写入 `CliOption`
    // 本插件没有配置信息，返回空集合
    override val pluginOptions: Collection<AbstractCliOption> = emptyList()

    // 处理 `CliOption` 写入 `CompilerConfiguration`
    // 本插件没有配置信息，此处没有实现
    override fun processOption(
        option: AbstractCliOption,
        value: String,
        configuration: CompilerConfiguration
    ) {
        super.processOption(option, value, configuration)
    }


}