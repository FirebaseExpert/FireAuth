package com.firebase_expert.fireauth.android.ui.screen.main

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
import com.firebase_expert.fireauth.android.util.VERIFICATION_CODE_NOT_SENT
import com.firebase_expert.fireauth.android.util.showToast
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

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
    val authUiState by authViewModel.uiState.collectAsStateWithLifecycle()
    val mainUiState by mainViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            MainTopBar(
                onSignOut = authViewModel::signOut,
                onDeleteAccount = authViewModel::deleteAccount,
                isLoading = authUiState.isLoading || mainUiState.isLoading
            )
        },
        bottomBar = {
            MainBottomBar()
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) { innerPadding ->
        mainUiState.apps?.let { apps ->
            MainContent(
                innerPadding = innerPadding,
                apps = apps
            )
        }
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
            SnackbarResult.Dismissed -> when(authProvider) {
                AuthProvider.EMAIL -> showToast(context, REAUTHENTICATION_LINK_NOT_SENT)
                AuthProvider.PHONE -> showToast(context, VERIFICATION_CODE_NOT_SENT)
            }
        }
    }

    LaunchedEffect(Unit) {
        authViewModel.events.collect { event ->
            when (event) {
                is AuthEvent.ShowToast -> showToast(context, event.message)
                is AuthEvent.ShowSnackbar -> showSnackbar(event.message, event.authProvider)
                is AuthEvent.ClearEmailLink -> activity.clearEmailLink()
                is AuthEvent.NavigateToVerifyCodeScreen -> onNavigate(Screen.VerifyCode(event.phoneNumber))
                else -> {}
            }
        }
    }

    LaunchedEffect(Unit) {
        mainViewModel.events.collect { event ->
            when (event) {
                is MainEvent.ShowToast -> showToast(context, event.message)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            authViewModel.setLoading(AuthUiState::isVerifyingPhoneNumber, false)
        }
    }
}