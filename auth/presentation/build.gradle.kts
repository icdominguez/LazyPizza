plugins {
    alias(libs.plugins.lazypizza.android.library.compose)
}

android {
    namespace = "com.seno.auth.presentation"
}

dependencies {
    with(projects) {
        with(core) {
            implementation(domain)
            implementation(presentation)
        }
        implementation(auth.domain)
    }
}
