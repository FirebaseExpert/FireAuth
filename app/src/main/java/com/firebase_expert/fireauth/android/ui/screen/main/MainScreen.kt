package com.firebase_expert.fireauth.android.ui.screen.main

import android.content.Intent
import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.firebase_expert.fireauth.android.navigation.Screen
import com.firebase_expert.fireauth.android.ui.FireAuthActivity
import com.firebase_expert.fireauth.android.ui.screen.auth.AuthEvent
import com.firebase_expert.fireauth.android.ui.screen.auth.AuthProvider
import com.firebase_expert.fireauth.android.ui.screen.auth.AuthUiState
import com.firebase_expert.fireauth.android.ui.screen.auth.AuthViewModel
import com.firebase_expert.fireauth.android.ui.screen.main.component.MainBottomBar
import com.firebase_expert.fireauth.android.ui.screen.main.component.MainContent
import com.firebase_expert.fireauth.android.ui.screen.main.component.MainTopBar
import com.firebase_expert.fireauth.android.util.REAUTHENTICATION_LINK_NOT_SENT
import com.firebase_expert.fireauth.android.util.SEND_CODE
import com.firebase_expert.fireauth.android.util.SEND_EMAIL
import com.firebase_expert.fireauth.android.util.TAG
import com.firebase_expert.fireauth.android.util.showToast
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

const val GOOGLE_PLAY_APP_URL = "https://play.google.com/store/apps/details?id=com.firebase_expert.fireapp"

@Composable
fun MainScreen(
    authViewModel: AuthViewModel = koinViewModel(),
    mainViewModel: MainViewModel = koinViewModel(),
    emailLink: String?,
    onNavigate: (Screen) -> Unit
) {
    val context = LocalContext.current
    val activity = context as FireAuthActivity
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState by authViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        Log.d(TAG, mainViewModel.message)
    }

    Scaffold(
        topBar = {
            MainTopBar(
                onSignOut = authViewModel::signOut,
                onDeleteAccount = authViewModel::deleteAccount,
                isLoading = uiState.isLoading
            )
        },
        bottomBar = {
            MainBottomBar(
                openGooglePlayApp = {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = GOOGLE_PLAY_APP_URL.toUri()
                        setPackage("com.android.vending")
                    }

                    context.startActivity(intent)
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) { innerPadding ->
        MainContent(
            innerPadding = innerPadding
        )
    }

    LaunchedEffect(emailLink) {
        emailLink?.let { emailLink ->
            authViewModel.reauthenticateWithEmailLink(emailLink)
        }
    }

    fun showSnackbar(message: String, authProvider: AuthProvider) = scope.launch {
        val result = snackbarHostState.showSnackbar(
            message = message,
            actionLabel = when (authProvider) {
                AuthProvider.EMAIL -> SEND_EMAIL
                AuthProvider.PHONE -> SEND_CODE
            },
            withDismissAction = true,
            duration = SnackbarDuration.Indefinite
        )
        when (result) {
            SnackbarResult.ActionPerformed -> when (authProvider) {
                AuthProvider.EMAIL -> authViewModel.sendReauthenticationLinkToEmail()
                AuthProvider.PHONE -> authViewModel.verifyPhoneNumber(
                    phoneNumber = authViewModel.phoneNumber ?: return@launch,
                    activity = activity
                )
            }
            SnackbarResult.Dismissed -> showToast(context, REAUTHENTICATION_LINK_NOT_SENT)
        }
    }

    LaunchedEffect(Unit) {
        authViewModel.events.collect { event ->
            when (event) {
                is AuthEvent.ShowToast -> showToast(context, event.message)
                is AuthEvent.ShowSnackbar -> showSnackbar(event.message, event.authProvider)
                is AuthEvent.ClearEmailLink -> activity.clearEmailLink()
                is AuthEvent.NavigateToVerifyCodeScreen -> onNavigate(Screen.VerifyCode(event.phoneNumber))
                else -> Unit
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            authViewModel.setLoading(AuthUiState::isVerifyingPhoneNumber, false)
        }
    }
}