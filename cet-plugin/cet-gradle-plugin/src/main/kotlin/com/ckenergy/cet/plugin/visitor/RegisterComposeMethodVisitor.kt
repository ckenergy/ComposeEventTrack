package com.ckenergy.cet.plugin.visitor

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.AdviceAdapter

private const val LISTENER_CLASS = "com/ckenergy/cet/core/compose/CETDestinationChangedListener"
/**
 * Created by chengkai on 2023/1/19.
 */
class RegisterComposeMethodVisitor(
    api: Int,
    methodVisitor: MethodVisitor?,
    access: Int,
    name: String?,
    descriptor: String?
) : AdviceAdapter(api, methodVisitor, access, name, descriptor) {


    override fun onMethodExit(opcode: Int) {
        super.onMethodExit(opcode)
        println("RegisterComposeMethodVisitor onMethodExit")

        mv.visitFieldInsn(
            GETSTATIC,
            LISTENER_CLASS,
            "Companion",
            "L$LISTENER_CLASS\$Companion;"
        )
        mv.visitVarInsn(ALOAD, 0)
        mv.visitTypeInsn(CHECKCAST, "androidx/navigation/NavController")
        mv.visitMethodInsn(
            INVOKEVIRTUAL,
            "$LISTENER_CLASS\$Companion",
            "register",
            "(Landroidx/navigation/NavController;)V",
            false
        )
    }

}