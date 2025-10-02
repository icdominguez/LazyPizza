plugins {
    `kotlin-dsl`
}

group = "com.seno.build-logic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApp") {
            id = "lazypizza.android.application"
            implementationClass = "AndroidAppConventionPlugin"
        }
        register("androidLib") {
            id = "lazypizza.android.library"
            implementationClass = "AndroidLibConventionPlugin"
        }
        register("androidLibCompose") {
            id = "lazypizza.android.library.compose"
            implementationClass = "AndroidLibComposeConventionPlugin"
        }
        register("jvmLibrary") {
            id = "lazypizza.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
    }
}