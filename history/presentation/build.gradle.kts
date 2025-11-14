plugins {
    alias(libs.plugins.lazypizza.android.library.compose)
}

android {
    namespace = "com.seno.history.presentation"
}

dependencies {
    with(projects) {
        implementation(history.domain)
        implementation(core.presentation)
        implementation(core.domain)
    }
}
