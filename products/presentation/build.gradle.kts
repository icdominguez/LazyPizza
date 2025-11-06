plugins {
    alias(libs.plugins.lazypizza.android.library.compose)
}

android {
    namespace = "com.seno.products.presentation"
}

dependencies {
    implementation(libs.timber)
    implementation(libs.coil.compose)

    with(projects) {
        implementation(core.presentation)
        implementation(core.domain)
        implementation(products.domain)
    }
}
