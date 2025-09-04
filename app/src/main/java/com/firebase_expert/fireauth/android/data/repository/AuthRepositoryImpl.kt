package com.firebase_expert.fireauth.android.data.repository

import android.app.Activity
import com.firebase_expert.fireauth.android.domain.repository.AuthRepository
import com.firebase_expert.fireauth.android.ui.FireAuthState
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val actionCodeSettings: ActionCodeSettings
) : AuthRepository {
    override val currentUser get() = auth.currentUser

    override fun getAuthState() = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            val authState = if (auth.currentUser == null) FireAuthState.SignedOut else FireAuthState.SignedIn
            trySend(authState)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    override val isSignedInWitEmail get() = currentUser?.providerData?.any { userInfo ->
        userInfo.providerId == EmailAuthProvider.PROVIDER_ID
    } == true

    override val isSignedInWitPhone get() = currentUser?.providerData?.any { userInfo ->
        userInfo.providerId == PhoneAuthProvider.PROVIDER_ID
    } == true

    override fun isSignInWithEmailLink(emailLink: String) = auth.isSignInWithEmailLink(emailLink)

    override suspend fun sendSignInLinkToEmail(email: String) {
        auth.sendSignInLinkToEmail(email, actionCodeSettings).await()
    }

    override suspend fun sendReauthenticationLinkToEmail() {
        val email = currentUser?.email ?: return
        auth.sendSignInLinkToEmail(email, actionCodeSettings).await()
    }

    override suspend fun signInWithEmailLink(email: String, emailLink: String) {
        auth.signInWithEmailLink(email, emailLink).await()
    }

    override suspend fun reauthenticateWithEmailLink(emailLink: String) {
        val email = currentUser?.email ?: return
        val credential = EmailAuthProvider.getCredentialWithLink(email, emailLink)
        currentUser?.reauthenticate(credential)
    }

    override suspend fun verifyPhoneNumber(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?,
        activity: Activity
    ) = callbackFlow {
        val callbacks = PhoneAuthCallbackHandler { phoneAuthState ->
            trySend(phoneAuthState)
        }
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
        if (token != null) {
            optionsBuilder.setForceResendingToken(token)
        }
        val options = optionsBuilder.build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        awaitClose {}
    }

    override suspend fun signInWithPhone(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).await()
    }

    override suspend fun reauthenticateWithPhone(credential: PhoneAuthCredential) {
        currentUser?.reauthenticate(credential)
    }

    override fun signOut() = auth.signOut()

    override suspend fun deleteUser() {
        currentUser?.delete()?.await()
    }
}