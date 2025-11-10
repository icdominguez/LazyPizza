plugins {
    alias(libs.plugins.lazypizza.android.library)
}

android {
    namespace = "com.seno.auth.data"
}

dependencies {
    with(projects) {
        implementation(auth.domain)
        implementation(core.domain)
        implementation(core.data)
    }

    implementation(libs.timber)
    implementation(libs.com.googlecode.libphonenumber)
}
