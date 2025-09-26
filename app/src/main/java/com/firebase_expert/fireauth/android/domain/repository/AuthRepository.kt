package com.firebase_expert.fireauth.android.domain.repository

import android.app.Activity
import com.firebase_expert.fireauth.android.ui.screen.auth.AuthState
import com.firebase_expert.fireauth.android.ui.screen.auth.PhoneAuthState
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: FirebaseUser?

    fun getAuthState(): Flow<AuthState>

    val isSignedInWitEmail: Boolean

    val isSignedInWitPhone: Boolean

    fun isSignInWithEmailLink(emailLink: String): Boolean

    suspend fun sendSignInLinkToEmail(email: String)

    suspend fun sendReauthenticationLinkToEmail()

    suspend fun signInWithEmailLink(email: String, emailLink: String)

    suspend fun reauthenticateWithEmailLink(emailLink: String)

    suspend fun verifyPhoneNumber(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?,
        activity: Activity
    ): Flow<PhoneAuthState>

    suspend fun signInWithPhone(credential: PhoneAuthCredential)

    suspend fun reauthenticateWithPhone(credential: PhoneAuthCredential)

    fun signOut()

    suspend fun deleteUser()
}