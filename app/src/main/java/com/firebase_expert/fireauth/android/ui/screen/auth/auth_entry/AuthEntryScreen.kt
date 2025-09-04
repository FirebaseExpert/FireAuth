package com.firebase_expert.fireauth.android.ui.screen.auth.auth_entry

import android.app.Activity
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.firebase_expert.fireauth.android.navigation.Screen
import com.firebase_expert.fireauth.android.ui.screen.auth.AuthEvent
import com.firebase_expert.fireauth.android.ui.screen.auth.AuthUiState
import com.firebase_expert.fireauth.android.ui.screen.auth.AuthViewModel
import com.firebase_expert.fireauth.android.ui.screen.auth.auth_entry.component.AuthEntryContent
import com.firebase_expert.fireauth.android.ui.screen.auth.auth_entry.component.AuthEntryTopBar
import com.firebase_expert.fireauth.android.util.SIGN_IN_SUCCESS
import com.firebase_expert.fireauth.android.util.extensions.clearEmailLink
import com.firebase_expert.fireauth.android.util.extensions.emailLink
import com.firebase_expert.fireauth.android.util.showToast
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthEntryScreen(
    viewModel: AuthViewModel = koinViewModel(),
    isSignedIn: Boolean,
    onNavigate: (Screen) -> Unit
) {
    val context = LocalContext.current
    val activity = context as Activity
    val intent = activity.intent
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
                viewModel.verifyPhoneNumber(
                    phoneNumber = phoneNumber,
                    activity = activity
                )
            },
            isLoading = uiState.isLoading
        )
    }

    LaunchedEffect(isSignedIn) {
        if (isSignedIn) showToast(context, SIGN_IN_SUCCESS)
    }

    LaunchedEffect(intent.emailLink) {
        intent.emailLink?.let { emailLink ->
            viewModel.signInWithWithEmailLink(emailLink)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is AuthEvent.ShowToast -> showToast(context, event.message)
                is AuthEvent.ClearEmailLink -> intent.clearEmailLink()
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