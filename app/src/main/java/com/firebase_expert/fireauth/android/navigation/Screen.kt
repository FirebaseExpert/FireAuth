package com.firebase_expert.fireauth.android.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Screen: NavKey {
    @Serializable
    data object Splash : Screen

    @Serializable
    data object AuthEntry : Screen

    @Serializable
    data class VerifyCode(val phoneNumber: String) : Screen

    @Serializable
    data object Main : Screen
}