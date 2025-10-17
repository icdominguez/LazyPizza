import com.seno.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

class KtlintConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply(libs.findPlugin("ktlint").get().get().pluginId)
            }

            extensions.configure<KtlintExtension> {
                android.set(true)
                ignoreFailures.set(true) // TODO: this should be false when force apply the rules

                reporters {
                    reporter(ReporterType.JSON)
                }

                filter {
                    exclude("**/build/**")
                    include("**/*.kt", "**/*.kts")
                }
            }
        }
    }
}