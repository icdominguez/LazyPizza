import com.android.build.api.dsl.ApplicationExtension
import com.seno.convention.ExtensionType
import com.seno.convention.configureAndroidCompose
import com.seno.convention.configureBuildTypes
import com.seno.convention.configureKotlinAndroid
import com.seno.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidAppConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply(libs.findPlugin("android.application").get().get().pluginId)
                apply(libs.findPlugin("kotlin.android").get().get().pluginId)
                apply(libs.findPlugin("kotlin.compose").get().get().pluginId)
                apply(libs.findPlugin("kotlin.serialization").get().get().pluginId)
                apply(libs.findPlugin("google.services").get().get().pluginId)
                apply(libs.findPlugin("lazypizza.ktlint").get().get().pluginId)
           }

            extensions.configure<ApplicationExtension> {
                defaultConfig {
                    applicationId = libs.findVersion("projectAppId").get().toString()
                    targetSdk = libs.findVersion("projectTargetSdkVersion").get().toString().toInt()

                    versionCode = libs.findVersion("projectVersionCode").get().toString().toInt()
                    versionName = libs.findVersion("projectVersionName").get().toString()
                }

                configureKotlinAndroid(this)
                configureAndroidCompose(this)

                configureBuildTypes(
                    commonExtension = this,
                    extensionType = ExtensionType.APPLICATION
                )
            }
        }
    }
}