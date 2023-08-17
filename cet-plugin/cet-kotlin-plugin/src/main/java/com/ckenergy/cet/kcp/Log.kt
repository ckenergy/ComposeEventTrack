package com.ckenergy.cet.kcp

import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector

/**
 * @author ckenergy
 * @date 2023/8/10
 * @desc
 */
object Log {

    private const val isPrintLog = true

    private var messageCollector: MessageCollector? = null

    fun initLog(collector: MessageCollector) {
        this.messageCollector = collector
    }

    fun d(tag: String, msg: String) {
        if (!isPrintLog) return
        print(tag, msg)
    }

    fun w(tag: String, msg: String) {
        print(tag, msg)
    }

    private fun print(tag: String, msg: String) {
        messageCollector?.report(
            CompilerMessageSeverity.WARNING,
            "===$tag===, $msg"
        )
    }


}