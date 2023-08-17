package com.ckenergy.cet.plugin

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

class CETPlugin : KotlinCompilerPluginSupportPlugin {
    companion object {
        const val TAG = "CETPlugin"

        const val CETVersion = "1.0.1"

    }

    override fun apply(target: Project) {

        val extension = target.extensions.getByType(AndroidComponentsExtension::class.java)
        extension.onVariants(extension.selector().all()) { variant ->
            variant.instrumentation.apply {
                transformClassesWith(FooClassVisitorFactory::class.java, InstrumentationScope.ALL) {}
                setAsmFramesComputationMode(FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS)
            }
        }

    }

    // 是否适用, 默认True，会执行applyToCompilation
    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = true

    // 获取 Kotlin 插件唯一ID
    override fun getCompilerPluginId(): String = "cet-kcp"

    // 获取 Kotlin 插件 Maven 坐标信息
    override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
        groupId = "io.github.ckenergy",
        artifactId = "cet-kotlin-plugin",
        version = CETVersion,
    )

    // 读取 Gradle 插件扩展信息并写入 SubpluginOption
    // 本插件没有扩展信息，所以返回空集合
    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        val project = kotlinCompilation.target.project
        return project.provider { emptyList() }
    }

}
