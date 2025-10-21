plugins {
    alias(libs.plugins.lazypizza.android.library)
}

android {
    namespace = "com.seno.history.data"
}

dependencies {
    with(projects) {
        implementation(history.domain)
        implementation(core.domain)
    }
}