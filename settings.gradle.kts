pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "LazyPizza"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":core:domain")
include(":core:data")
include(":core:presentation")
include(":products:domain")
include(":products:data")
include(":products:presentation")
include(":cart:domain")
include(":cart:presentation")
include(":cart:data")
include(":history:data")
include(":history:presentation")
include(":history:domain")
