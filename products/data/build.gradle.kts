plugins {
    alias(libs.plugins.lazypizza.android.library)
}

android {
    namespace = "com.seno.products.data"
}

dependencies {
    with(projects) {
        implementation(core.domain)
        implementation(core.data)
        implementation(products.domain)
    }
}