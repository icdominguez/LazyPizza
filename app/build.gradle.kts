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
    }
}