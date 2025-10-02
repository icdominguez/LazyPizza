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
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material.icons.extended)

    implementation(libs.androidx.core.splashscreen)
    implementation(libs.kotlinx.serialization)
    implementation(libs.timber)
    implementation(libs.bundles.koin.compose)

    with(projects) {
        implementation(core.data)
        implementation(core.domain)
        implementation(core.presentation)

        implementation(products.data)
        implementation(products.domain)
        implementation(products.presentation)
    }
}