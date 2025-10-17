import com.android.build.api.dsl.LibraryExtension
import com.seno.convention.ExtensionType
import com.seno.convention.configureBuildTypes
import com.seno.convention.configureKotlinAndroid
import com.seno.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply(libs.findPlugin("android.library").get().get().pluginId)
                apply(libs.findPlugin("kotlin.android").get().get().pluginId)
                apply(libs.findPlugin("kotlin.serialization").get().get().pluginId)
                apply(libs.findPlugin("lazypizza.ktlint").get().get().pluginId)
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)

                configureBuildTypes(
                    commonExtension = this,
                    extensionType = ExtensionType.LIBRARY
                )

                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }
            }

            dependencies {
                "implementation"(project.libs.findBundle("koin").get())
                "implementation"(project.libs.findLibrary("kotlinx-serialization").get())
                "testImplementation"(kotlin("test"))
            }
        }
    }
}