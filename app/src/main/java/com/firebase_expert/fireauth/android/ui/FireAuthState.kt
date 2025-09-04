package com.firebase_expert.fireauth.android.ui

sealed class FireAuthState {
    object Loading : FireAuthState()
    object SignedIn : FireAuthState()
    object SignedOut : FireAuthState()
}