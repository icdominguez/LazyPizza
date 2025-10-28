plugins {
    alias(libs.plugins.lazypizza.android.library.compose)
}

android {
    namespace = "com.seno.cart.presentation"
}

dependencies {
    with(projects) {
        with(core) {
            implementation(domain)
            implementation(presentation)
        }
        implementation(products.domain)
        implementation(cart.domain)
        implementation(libs.coil.compose)
    }
}