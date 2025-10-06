plugins {
    alias(libs.plugins.lazypizza.android.library)
}

android {
    namespace = "com.seno.core.data"
}

dependencies {
    implementation(projects.core.domain)
}