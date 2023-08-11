package com.ckenergy.cet.plugin.visitor

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor

/**
 * Created by chengkai on 2023/1/19.
 */
class RegisterComposeClassVisitor(api: Int, classVisitor: ClassVisitor?) :
    ClassVisitor(api, classVisitor) {

    private var className: String? = null

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        className = name
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)

        println("class:$className,method:$name")
        if (className == "androidx/navigation/NavController" && name == "<init>") {
            return RegisterComposeMethodVisitor(api, mv, access, name, descriptor)
        }
        return mv
    }

}