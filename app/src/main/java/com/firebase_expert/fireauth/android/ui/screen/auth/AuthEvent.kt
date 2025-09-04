package com.firebase_expert.fireauth.android.ui.screen.auth

sealed class AuthEvent {
    data class ShowToast(val message: String) : AuthEvent()
    data class ShowSnackbar(val message: String, val authProvider: AuthProvider) : AuthEvent()
    data object ClearEmailLink : AuthEvent()
    data class NavigateToVerifyCodeScreen(val phoneNumber: String) : AuthEvent()
    data object NavigateBack : AuthEvent()
}