package com.firebase_expert.fireauth.android.ui.screen.auth

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase_expert.fireauth.android.domain.repository.AuthPrefsRepository
import com.firebase_expert.fireauth.android.domain.repository.AuthRepository
import com.firebase_expert.fireauth.android.util.DELETE_ACCOUNT_FAILURE
import com.firebase_expert.fireauth.android.util.DELETE_ACCOUNT_SUCCESS
import com.firebase_expert.fireauth.android.util.INVALID_CREDENTIALS
import com.firebase_expert.fireauth.android.util.INVALID_EMAIL
import com.firebase_expert.fireauth.android.util.INVALID_EMAIL_LINK
import com.firebase_expert.fireauth.android.util.INVALID_PHONE_NUMBER
import com.firebase_expert.fireauth.android.util.MISSING_ACTIVITY_FOR_RECAPTCHA
import com.firebase_expert.fireauth.android.util.PHONE_NUMBER_VERIFICATION_FAILURE
import com.firebase_expert.fireauth.android.util.PHONE_NUMBER_VERIFICATION_SUCCESS
import com.firebase_expert.fireauth.android.util.REAUTHENTICATION_LINK_SENT_FAILURE
import com.firebase_expert.fireauth.android.util.REAUTHENTICATION_LINK_SENT_SUCCESS
import com.firebase_expert.fireauth.android.util.REAUTHENTICATION_WITH_EMAIL_LINK_FAILURE
import com.firebase_expert.fireauth.android.util.REAUTHENTICATION_WITH_EMAIL_LINK_SUCCESS
import com.firebase_expert.fireauth.android.util.REAUTHENTICATION_WITH_PHONE_FAILURE
import com.firebase_expert.fireauth.android.util.REAUTHENTICATION_WITH_PHONE_SUCCESS
import com.firebase_expert.fireauth.android.util.SENSITIVE_OPERATION
import com.firebase_expert.fireauth.android.util.SIGN_IN_LINK_SENT_FAILURE
import com.firebase_expert.fireauth.android.util.SIGN_IN_LINK_SENT_SUCCESS
import com.firebase_expert.fireauth.android.util.SIGN_IN_WITH_EMAIL_LINK_FAILURE
import com.firebase_expert.fireauth.android.util.SIGN_IN_WITH_EMAIL_LINK_SUCCESS
import com.firebase_expert.fireauth.android.util.SIGN_IN_WITH_PHONE_FAILURE
import com.firebase_expert.fireauth.android.util.SIGN_IN_WITH_PHONE_SUCCESS
import com.firebase_expert.fireauth.android.util.SIGN_OUT_SUCCESS
import com.firebase_expert.fireauth.android.util.TAG
import com.firebase_expert.fireauth.android.util.TOO_MANY_REQUESTS
import com.firebase_expert.fireauth.android.util.UNKNOWN_FAILURE
import com.firebase_expert.fireauth.android.util.VERIFICATION_CODE_RESENT_SUCCESS
import com.firebase_expert.fireauth.android.util.VERIFICATION_CODE_SENT_SUCCESS
import com.firebase_expert.fireauth.android.util.extensions.isEmailValid
import com.firebase_expert.fireauth.android.util.extensions.isPhoneNumberValid
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty1

class AuthViewModel(
    private val authRepo: AuthRepository,
    private val authPrefsRepo: AuthPrefsRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<AuthEvent>()
    val events = _events.asSharedFlow()

    val phoneNumber get() = authRepo.currentUser?.phoneNumber
    val isSignedIn get() = authRepo.currentUser != null

    var storedVerificationId: String?
        get() = PhoneAuthSingleton.storedVerificationId
        set(value) {
            PhoneAuthSingleton.storedVerificationId = value
        }

    var resendToken: PhoneAuthProvider.ForceResendingToken?
        get() = PhoneAuthSingleton.resendToken
        set(value) {
            PhoneAuthSingleton.resendToken = value
        }

    val authState get() = authRepo.getAuthState().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = AuthState.Loading
    )

    fun sendSignInLinkToEmail(email: String) = viewModelScope.launch {
        try {
            if (!email.isEmailValid()) {
                _events.emit(AuthEvent.ShowToast(INVALID_EMAIL))
                return@launch
            }
            setLoading(AuthUiState::isSendingSignInLinkToEmail, true)
            authRepo.sendSignInLinkToEmail(email)
            authPrefsRepo.setEmail(email)
            _events.emit(AuthEvent.ShowToast(SIGN_IN_LINK_SENT_SUCCESS))
        } catch (e: Exception) {
            Log.e(TAG, SIGN_IN_LINK_SENT_FAILURE, e)
            _events.emit(AuthEvent.ShowToast(e.message ?: UNKNOWN_FAILURE))
        } finally {
            setLoading(AuthUiState::isSendingSignInLinkToEmail, false)
        }
    }

    fun signInWithWithEmailLink(emailLink: String) = viewModelScope.launch {
        try {
            val email = authPrefsRepo.getEmail() ?: return@launch
            if (authRepo.isSignInWithEmailLink(emailLink)) {
                setLoading(AuthUiState::isSigningInWithWithEmailLink, true)
                authRepo.signInWithEmailLink(email, emailLink)
                _events.emit(AuthEvent.ShowToast(SIGN_IN_WITH_EMAIL_LINK_SUCCESS))
                _events.emit(AuthEvent.ClearEmailLink)
            } else {
                _events.emit(AuthEvent.ShowToast(INVALID_EMAIL_LINK))
                return@launch
            }
        } catch (e: Exception) {
            Log.e(TAG, SIGN_IN_WITH_EMAIL_LINK_FAILURE, e)
            _events.emit(AuthEvent.ShowToast(e.message ?: UNKNOWN_FAILURE))
            setLoading(AuthUiState::isSigningInWithWithEmailLink, false)
        }
    }

    fun sendReauthenticationLinkToEmail() = viewModelScope.launch {
        try {
            setLoading(AuthUiState::isSendingReauthenticationLinkToEmail, true)
            authRepo.sendReauthenticationLinkToEmail()
            _events.emit(AuthEvent.ShowToast(REAUTHENTICATION_LINK_SENT_SUCCESS))
        } catch (e: Exception) {
            Log.e(TAG, REAUTHENTICATION_LINK_SENT_FAILURE, e)
            _events.emit(AuthEvent.ShowToast(e.message ?: UNKNOWN_FAILURE))
        } finally {
            setLoading(AuthUiState::isSendingReauthenticationLinkToEmail, false)
        }
    }

    fun reauthenticateWithEmailLink(emailLink: String) = viewModelScope.launch {
        try {
            if (authRepo.isSignInWithEmailLink(emailLink)) {
                setLoading(AuthUiState::isReauthenticatingWithEmailLink, true)
                authRepo.reauthenticateWithEmailLink(emailLink)
                _events.emit(AuthEvent.ShowToast(REAUTHENTICATION_WITH_EMAIL_LINK_SUCCESS))
                _events.emit(AuthEvent.ClearEmailLink)
            } else {
                _events.emit(AuthEvent.ShowToast(INVALID_EMAIL_LINK))
                return@launch
            }
        } catch (e: Exception) {
            Log.e(TAG, REAUTHENTICATION_WITH_EMAIL_LINK_FAILURE, e)
            _events.emit(AuthEvent.ShowToast(e.message ?: UNKNOWN_FAILURE))
        } finally {
            setLoading(AuthUiState::isReauthenticatingWithEmailLink, false)
        }
    }

    fun verifyPhoneNumber(phoneNumber: String, activity: Activity) = viewModelScope.launch {
        if (!phoneNumber.isPhoneNumberValid()) {
            _events.emit(AuthEvent.ShowToast(INVALID_PHONE_NUMBER))
            return@launch
        }
        setLoading(AuthUiState::isVerifyingPhoneNumber, true)
        authRepo.verifyPhoneNumber(phoneNumber, resendToken, activity).collect { phoneAuthState ->
            handlePhoneAuthState(phoneAuthState, phoneNumber)
        }
    }

    private suspend fun handlePhoneAuthState(
        phoneAuthState: PhoneAuthState,
        phoneNumber: String
    ) = when(phoneAuthState) {
        is PhoneAuthState.VerificationCompleted -> handleVerificationCompleted(phoneAuthState)
        is PhoneAuthState.VerificationFailed -> handleVerificationFailed(phoneAuthState)
        is PhoneAuthState.CodeSent -> handleCodeSent(phoneAuthState, phoneNumber)
    }

    private suspend fun handleVerificationCompleted(phoneAuthState: PhoneAuthState.VerificationCompleted) {
        _events.emit(AuthEvent.ShowToast(PHONE_NUMBER_VERIFICATION_SUCCESS))
        setLoading(AuthUiState::isVerifyingPhoneNumber, false)
        if (isSignedIn) {
            reauthenticateWithPhoneCredential(phoneAuthState.credential)
        } else {
            signInWithPhoneCredential(phoneAuthState.credential)
        }
    }

    private suspend fun handleVerificationFailed(phoneAuthState: PhoneAuthState.VerificationFailed) {
        val e = phoneAuthState.e
        Log.e(TAG, PHONE_NUMBER_VERIFICATION_FAILURE, e)
        when (e) {
            is FirebaseAuthInvalidCredentialsException ->
                _events.emit(AuthEvent.ShowToast(INVALID_CREDENTIALS))
            is FirebaseTooManyRequestsException ->
                _events.emit(AuthEvent.ShowToast(TOO_MANY_REQUESTS))
            is FirebaseAuthMissingActivityForRecaptchaException ->
                _events.emit(AuthEvent.ShowToast(MISSING_ACTIVITY_FOR_RECAPTCHA))
            else ->
                _events.emit(AuthEvent.ShowToast(e.message ?: UNKNOWN_FAILURE))
        }
        setLoading(AuthUiState::isVerifyingPhoneNumber, false)
    }

    private suspend fun handleCodeSent(phoneAuthState: PhoneAuthState.CodeSent, phoneNumber: String) {
        if (resendToken == null) {
            _events.emit(AuthEvent.ShowToast(VERIFICATION_CODE_SENT_SUCCESS))
            _events.emit(AuthEvent.NavigateToVerifyCodeScreen(phoneNumber))
        } else {
            _events.emit(AuthEvent.ShowToast(VERIFICATION_CODE_RESENT_SUCCESS))
            if (isSignedIn) {
                _events.emit(AuthEvent.NavigateToVerifyCodeScreen(phoneNumber))
            }
            setLoading(AuthUiState::isVerifyingPhoneNumber, false)
        }
        storedVerificationId = phoneAuthState.verificationId
        resendToken = phoneAuthState.token
    }

    private fun signInWithPhoneCredential(credential: PhoneAuthCredential) = viewModelScope.launch {
        try {
            setLoading(AuthUiState::isSigningInWithPhone, true)
            authRepo.signInWithPhone(credential)
            _events.emit(AuthEvent.ShowToast(SIGN_IN_WITH_PHONE_SUCCESS))
        } catch (e: Exception) {
            Log.e(TAG, SIGN_IN_WITH_PHONE_FAILURE, e)
            _events.emit(AuthEvent.ShowToast(e.message ?: UNKNOWN_FAILURE))
            setLoading(AuthUiState::isSigningInWithPhone, false)
        }
    }

    fun signInWithVerificationCode(verificationCode: String) = viewModelScope.launch {
        val verificationId = storedVerificationId ?: return@launch
        val credential = PhoneAuthProvider.getCredential(verificationId, verificationCode)
        signInWithPhoneCredential(credential)
    }

    fun reauthenticateWithPhoneCredential(credential: PhoneAuthCredential) = viewModelScope.launch {
        try {
            setLoading(AuthUiState::isReauthenticatingWithPhone, true)
            authRepo.reauthenticateWithPhone(credential)
            _events.emit(AuthEvent.ShowToast(REAUTHENTICATION_WITH_PHONE_SUCCESS))
            _events.emit(AuthEvent.NavigateBack)
        } catch (e: Exception) {
            Log.e(TAG, REAUTHENTICATION_WITH_PHONE_FAILURE, e)
            _events.emit(AuthEvent.ShowToast(e.message ?: UNKNOWN_FAILURE))
            setLoading(AuthUiState::isReauthenticatingWithPhone, false)
        }
    }

    fun reauthenticateWithVerificationCode(verificationCode: String) = viewModelScope.launch {
        val verificationId = storedVerificationId ?: return@launch
        val credential = PhoneAuthProvider.getCredential(verificationId, verificationCode)
        reauthenticateWithPhoneCredential(credential)
    }

    fun signOut() = viewModelScope.launch {
        setLoading(AuthUiState::isSigningOut, true)
        delay(1500) //ToRemove
        authRepo.signOut()
        resetPhoneAuthState()
        setLoading(AuthUiState::isSigningOut, false)
        _events.emit(AuthEvent.ShowToast(SIGN_OUT_SUCCESS))
    }

    fun deleteAccount() = viewModelScope.launch {
        try {
            setLoading(AuthUiState::isDeletingUser, true)
            authRepo.deleteUser()
            resetPhoneAuthState()
            _events.emit(AuthEvent.ShowToast(DELETE_ACCOUNT_SUCCESS))
            _events.emit(AuthEvent.ShowToast(SIGN_OUT_SUCCESS))
        } catch (e: Exception) {
            Log.e(TAG, DELETE_ACCOUNT_FAILURE, e)
            if (e is FirebaseAuthRecentLoginRequiredException) {
                if (authRepo.isSignedInWitEmail) {
                    _events.emit(AuthEvent.ShowSnackbar(SENSITIVE_OPERATION, AuthProvider.EMAIL))
                } else if (authRepo.isSignedInWitPhone) {
                    _events.emit(AuthEvent.ShowSnackbar(SENSITIVE_OPERATION, AuthProvider.PHONE))
                }
            } else {
                _events.emit(AuthEvent.ShowToast(e.message ?: UNKNOWN_FAILURE))
            }
            setLoading(AuthUiState::isDeletingUser, false)
        }
    }

    fun setLoading(field: KProperty1<AuthUiState, Boolean>, loading: Boolean) = _uiState.update { state ->
        when (field) {
            AuthUiState::isSendingSignInLinkToEmail -> state.copy(isSendingSignInLinkToEmail = loading)
            AuthUiState::isSigningInWithWithEmailLink -> state.copy(isSigningInWithWithEmailLink = loading)
            AuthUiState::isSendingReauthenticationLinkToEmail -> state.copy(isSendingReauthenticationLinkToEmail = loading)
            AuthUiState::isReauthenticatingWithEmailLink -> state.copy(isReauthenticatingWithEmailLink = loading)
            AuthUiState::isVerifyingPhoneNumber -> state.copy(isVerifyingPhoneNumber = loading)
            AuthUiState::isSigningInWithPhone -> state.copy(isSigningInWithPhone = loading)
            AuthUiState::isDeletingUser -> state.copy(isDeletingUser = loading)
            else -> state
        }
    }

    fun resetPhoneAuthState() {
        storedVerificationId = null
        resendToken = null
    }
}

enum class AuthProvider {
    EMAIL, PHONE
}

object PhoneAuthSingleton {
    var storedVerificationId: String? = null
    var resendToken: PhoneAuthProvider.ForceResendingToken? = null
}