
import com.seno.convention.configureKotlinJvm
import com.seno.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

class JvmLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply(libs.findPlugin("kotlin.jvm").get().get().pluginId)
                apply(libs.findPlugin("lazypizza.ktlint").get().get().pluginId)
            }

            configureKotlinJvm()
        }
    }
}