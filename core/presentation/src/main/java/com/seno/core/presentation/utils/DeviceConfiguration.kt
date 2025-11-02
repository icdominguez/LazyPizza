package com.seno.core.presentation.utils

import androidx.window.core.layout.WindowSizeClass

enum class DeviceConfiguration {
    MOBILE_PORTRAIT,
    MOBILE_LANDSCAPE,
    TABLET_PORTRAIT,
    TABLET_LANDSCAPE,
    DESKTOP;

    fun isTablet() = this == TABLET_PORTRAIT || this == TABLET_LANDSCAPE

    companion object {
        private const val WIDTH_COMPACT_THRESHOLD = 600
        private const val WIDTH_MEDIUM_THRESHOLD = 840

        private const val HEIGHT_COMPACT_THRESHOLD = 480
        private const val HEIGHT_MEDIUM_THRESHOLD = 900

        fun fromWindowSizeClass(windowSizeClass: WindowSizeClass): DeviceConfiguration {
            val isCompactWidth = !windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_COMPACT_THRESHOLD)
            val isMediumWidth =
                windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_COMPACT_THRESHOLD) &&
                    !windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_MEDIUM_THRESHOLD)
            val isExpandedWidth = windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_MEDIUM_THRESHOLD)

            val isCompactHeight =
                !windowSizeClass.isHeightAtLeastBreakpoint(HEIGHT_COMPACT_THRESHOLD)
            val isMediumHeight =
                windowSizeClass.isHeightAtLeastBreakpoint(HEIGHT_COMPACT_THRESHOLD) &&
                    !windowSizeClass.isHeightAtLeastBreakpoint(HEIGHT_MEDIUM_THRESHOLD)
            val isExpandedHeight =
                windowSizeClass.isHeightAtLeastBreakpoint(HEIGHT_MEDIUM_THRESHOLD)

            return when {
                isCompactWidth && (isMediumHeight || isExpandedHeight) -> MOBILE_PORTRAIT
                isExpandedWidth && isCompactHeight -> MOBILE_LANDSCAPE
                isMediumWidth && isExpandedHeight -> TABLET_PORTRAIT
                isExpandedWidth && isMediumHeight -> TABLET_LANDSCAPE
                else -> DESKTOP
            }
        }
    }
}
