package com.seno.core.presentation.utils

import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass

enum class DeviceConfiguration {
    MOBILE_PORTRAIT,
    MOBILE_LANDSCAPE,
    TABLET_PORTRAIT,
    TABLET_LANDSCAPE,
    DESKTOP;

    fun isTablet() = this == TABLET_PORTRAIT || this == TABLET_LANDSCAPE

    companion object {
        fun fromWindowSizeClass(windowSizeClass: WindowSizeClass): DeviceConfiguration {
            val widthSizeClass = windowSizeClass.windowWidthSizeClass
            val heightSizeClass = windowSizeClass.windowHeightSizeClass
            return when {
                widthSizeClass == WindowWidthSizeClass.COMPACT && heightSizeClass == WindowHeightSizeClass.MEDIUM -> MOBILE_PORTRAIT
                widthSizeClass == WindowWidthSizeClass.COMPACT && heightSizeClass == WindowHeightSizeClass.EXPANDED -> MOBILE_PORTRAIT
                widthSizeClass == WindowWidthSizeClass.EXPANDED && heightSizeClass == WindowHeightSizeClass.COMPACT -> MOBILE_LANDSCAPE
                widthSizeClass == WindowWidthSizeClass.MEDIUM && heightSizeClass == WindowHeightSizeClass.EXPANDED -> TABLET_PORTRAIT
                widthSizeClass == WindowWidthSizeClass.EXPANDED && heightSizeClass == WindowHeightSizeClass.MEDIUM -> TABLET_LANDSCAPE
                else -> DESKTOP
            }
        }
    }
}
