import com.android.build.api.dsl.LibraryExtension
import com.seno.convention.configureAndroidCompose
import com.seno.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidLibComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply(libs.findPlugin("lazypizza.android.library").get().get().pluginId)
                apply(libs.findPlugin("kotlin.compose").get().get().pluginId)
            }

            val extension = extensions.getByType<LibraryExtension>()
            configureAndroidCompose(extension)
        }
    }
}