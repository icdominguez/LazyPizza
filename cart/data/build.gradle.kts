plugins {
    alias(libs.plugins.lazypizza.android.library)
}

android {
    namespace = "com.seno.cart.data"
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)
    with(projects) {
        implementation(cart.domain)
        implementation(core.domain)
    }
}