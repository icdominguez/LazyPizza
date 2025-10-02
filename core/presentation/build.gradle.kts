plugins {
    alias(libs.plugins.lazypizza.android.library.compose)
}

android {
    namespace = "com.seno.core.presentation"
}

dependencies {
    implementation(projects.core.domain)
}