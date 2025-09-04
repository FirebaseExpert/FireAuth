package com.firebase_expert.fireauth.android.data.repository

import com.google.firebase.auth.PhoneAuthProvider

object PhoneAuthSingleton {
    var storedVerificationId: String? = null
    var resendToken: PhoneAuthProvider.ForceResendingToken? = null
}