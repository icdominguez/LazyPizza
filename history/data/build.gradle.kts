plugins {
    alias(libs.plugins.lazypizza.android.library)
}

android {
    namespace = "com.seno.history.data"
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)
    with(projects) {
        implementation(history.domain)
        implementation(core.domain)
        implementation(core.data)
    }
}
