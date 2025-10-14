plugins {
    alias(libs.plugins.lazypizza.android.library)
}

android {
    namespace = "com.seno.products.data"
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)
    implementation(libs.timber)
    with(projects) {
        implementation(core.domain)
        implementation(core.data)
        implementation(products.domain)
    }
}
