package com.firebase_expert.fireauth.android.ui.screen.auth.auth_entry

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.firebase_expert.fireauth.android.navigation.Screen
import com.firebase_expert.fireauth.android.ui.FireAuthActivity
import com.firebase_expert.fireauth.android.ui.screen.auth.AuthEvent
import com.firebase_expert.fireauth.android.ui.screen.auth.AuthUiState
import com.firebase_expert.fireauth.android.ui.screen.auth.AuthViewModel
import com.firebase_expert.fireauth.android.ui.screen.auth.auth_entry.component.AuthEntryContent
import com.firebase_expert.fireauth.android.ui.screen.auth.auth_entry.component.AuthEntryTopBar
import com.firebase_expert.fireauth.android.util.showToast
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthEntryScreen(
    viewModel: AuthViewModel = koinViewModel(),
    emailLink: String?,
    onNavigate: (Screen) -> Unit
) {
    val context = LocalContext.current
    val activity = context as FireAuthActivity
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            AuthEntryTopBar()
        }
    ) { innerPadding ->
        AuthEntryContent(
            innerPadding = innerPadding,
            onSendSignInLinkToEmail = viewModel::sendSignInLinkToEmail,
            onVerifyPhoneNumber = { phoneNumber ->
                viewModel.verifyPhoneNumber(phoneNumber, activity)
            },
            isLoading = uiState.isLoading
        )
    }

    LaunchedEffect(emailLink) {
        emailLink?.let { emailLink ->
            viewModel.signInWithWithEmailLink(emailLink)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is AuthEvent.ShowToast -> showToast(context, event.message)
                is AuthEvent.ClearEmailLink -> activity.clearEmailLink()
                is AuthEvent.NavigateToVerifyCodeScreen -> onNavigate(Screen.VerifyCode(event.phoneNumber))
                else -> Unit
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.setLoading(AuthUiState::isVerifyingPhoneNumber, false)
        }
    }
}