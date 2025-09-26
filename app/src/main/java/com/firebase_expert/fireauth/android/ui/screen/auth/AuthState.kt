package com.firebase_expert.fireauth.android.ui.screen.auth

sealed class AuthState {
    object Loading : AuthState()
    object SignedIn : AuthState()
    object SignedOut : AuthState()
}