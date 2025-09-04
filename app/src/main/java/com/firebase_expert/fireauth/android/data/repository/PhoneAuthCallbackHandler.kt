package com.firebase_expert.fireauth.android.data.repository

import com.firebase_expert.fireauth.android.ui.screen.auth.PhoneAuthState
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class PhoneAuthCallbackHandler(
    private val trySend: (PhoneAuthState) -> Unit
) : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
        trySend(PhoneAuthState.VerificationCompleted(credential))
    }

    override fun onVerificationFailed(e: FirebaseException) {
        trySend(PhoneAuthState.VerificationFailed(e))
    }

    override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
        trySend(PhoneAuthState.CodeSent(verificationId, token))
    }
}