plugins {
    alias(libs.plugins.lazypizza.android.library.compose)
}

android {
    namespace = "com.seno.cart.presentation"
}

dependencies {
    implementation(projects.core.presentation)
}