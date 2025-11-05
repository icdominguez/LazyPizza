plugins {
    alias(libs.plugins.lazypizza.jvm.library)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(projects.core.domain)
}
