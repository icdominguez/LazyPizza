package com.seno.lazypizza

sealed interface MainAction {
    data object ShowLogoutDialog : MainAction
    data object DismissLogoutDialog : MainAction
    data object ConfirmLogout : MainAction
}