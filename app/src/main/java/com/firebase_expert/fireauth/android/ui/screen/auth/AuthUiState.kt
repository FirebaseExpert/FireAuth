package com.firebase_expert.fireauth.android.ui.screen.auth

data class AuthUiState(
    val isSendingSignInLinkToEmail: Boolean = false,
    val isSigningInWithWithEmailLink: Boolean = false,
    val isSendingReauthenticationLinkToEmail: Boolean = false,
    val isReauthenticatingWithEmailLink: Boolean = false,
    val isVerifyingPhoneNumber: Boolean = false,
    val isSigningInWithPhone: Boolean = false,
    val isReauthenticatingWithPhone: Boolean = false,
    val isSigningOut: Boolean = false,
    val isDeletingUser: Boolean = false
) {
    val isLoading get() = isSendingSignInLinkToEmail ||
            isSigningInWithWithEmailLink ||
            isSendingReauthenticationLinkToEmail ||
            isReauthenticatingWithEmailLink ||
            isVerifyingPhoneNumber ||
            isSigningInWithPhone ||
            isReauthenticatingWithPhone ||
            isSigningOut ||
            isDeletingUser
}