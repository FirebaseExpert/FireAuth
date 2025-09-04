package com.firebase_expert.fireauth.android.ui.screen.auth

import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

sealed class PhoneAuthState {
    data class VerificationCompleted(
        val credential: PhoneAuthCredential
    ) : PhoneAuthState()

    data class VerificationFailed(
        val e: FirebaseException
    ) : PhoneAuthState()

    data class CodeSent(
        val verificationId: String,
        val token: PhoneAuthProvider.ForceResendingToken
    ) : PhoneAuthState()
}