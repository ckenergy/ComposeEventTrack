package com.ckenergy.cet.plugin

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import com.ckenergy.cet.plugin.visitor.RegisterComposeClassVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

abstract class FooClassVisitorFactory : AsmClassVisitorFactory<InstrumentationParameters.None> {

    override fun isInstrumentable(classData: ClassData): Boolean {
        println("class===,${classData.className}")

        return classData.className.startsWith("androidx.navigation.NavController")
    }

    override fun createClassVisitor(
        classContext: ClassContext, nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        return RegisterComposeClassVisitor(Opcodes.ASM7, nextClassVisitor)
    }
}
