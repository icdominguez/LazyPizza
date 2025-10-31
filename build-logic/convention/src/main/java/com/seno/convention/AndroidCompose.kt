package com.seno.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    commonExtension.run {
        buildFeatures {
            compose = true
        }

        dependencies {
            "ktlintRuleset"(project.libs.findLibrary("ktlint.compose.rules").get())
            "implementation"(project.libs.findBundle("koin.compose").get())
            "implementation"(project.libs.findBundle("compose").get())

            val bom = libs.findLibrary("androidx.compose.bom").get()
            "implementation"(platform(bom))
            "androidTestImplementation"(platform(bom))
            "debugImplementation"(libs.findBundle("compose.debug").get())
        }
    }
}