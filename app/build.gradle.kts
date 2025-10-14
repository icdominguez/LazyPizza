import org.gradle.kotlin.dsl.support.serviceOf

plugins {
    alias(libs.plugins.lazypizza.android.application)
}

android {
    namespace = "com.seno.lazypizza"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material.icons.extended)

    implementation(libs.androidx.core.splashscreen)
    implementation(libs.kotlinx.serialization)
    implementation(libs.timber)
    implementation(libs.bundles.koin.compose)

    with(projects) {
        with(core) {
            implementation(data)
            implementation(domain)
            implementation(presentation)
        }

        with(products) {
            implementation(data)
            implementation(domain)
            implementation(presentation)
        }

        with(cart) {
            implementation(data)
            implementation(domain)
            implementation(presentation)
        }

        with(history) {
            implementation(data)
            implementation(domain)
            implementation(presentation)
        }
    }
}

tasks.register("installGitHooks") {
    group = "git"
    description = "Setup Git hooks path and permissions."

    doLast {
        val execOps = project.serviceOf<ExecOperations>()
        val hooksDir = rootProject.file(".githooks")
        val hookFile = rootProject.file(".githooks/pre-commit")

        if (!hooksDir.exists() || !hookFile.exists()) {
            println(".githooks/pre-commit not found. Skipping hook setup.")
            return@doLast
        }

        if (!hookFile.canExecute()) {
            hookFile.setExecutable(true)
        }

        execOps.exec {
            commandLine("git", "config", "core.hooksPath", ".githooks")
        }
        println("Git hooks installed")
    }
}
