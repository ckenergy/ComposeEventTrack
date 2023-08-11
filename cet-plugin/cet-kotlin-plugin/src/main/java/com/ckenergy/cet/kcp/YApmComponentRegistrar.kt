package com.ckenergy.cet.kcp

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration

@AutoService(ComponentRegistrar::class)
class CETComponentRegistrar : ComponentRegistrar {

    override fun registerProjectComponents(
        project: MockProject,
        configuration: CompilerConfiguration
    ) {
        // 获取日志收集器
        val messageCollector =
            configuration.get(CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY, MessageCollector.NONE)

        messageCollector.report(
            CompilerMessageSeverity.WARNING,
            "kcp kotlin plugin"
        )
        Log.initLog(messageCollector)

        // 此处在 `ClassBuilderInterceptorExtension` 中注册扩展
        IrGenerationExtension.registerExtension(
            project,
            ComposeClickIrGenerationExtension()
        )
    }
}