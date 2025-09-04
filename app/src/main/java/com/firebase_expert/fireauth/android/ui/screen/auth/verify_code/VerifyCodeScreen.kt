package com.firebase_expert.fireauth.android.ui.screen.auth.verify_code

import android.app.Activity
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.firebase_expert.fireauth.android.ui.screen.auth.AuthEvent
import com.firebase_expert.fireauth.android.ui.screen.auth.AuthViewModel
import com.firebase_expert.fireauth.android.ui.screen.auth.verify_code.component.VerifyCodeContent
import com.firebase_expert.fireauth.android.ui.screen.auth.verify_code.component.VerifyCodeTopBar
import com.firebase_expert.fireauth.android.util.showToast
import org.koin.androidx.compose.koinViewModel

@Composable
fun VerifyCodeScreen(
    viewModel: AuthViewModel = koinViewModel(),
    phoneNumber: String,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as Activity
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            VerifyCodeTopBar(
                onBackButtonClick = onNavigateBack
            )
        }
    ) { innerPadding ->
        VerifyCodeContent(
            innerPadding = innerPadding,
            phoneNumber = phoneNumber,
            onAuthWithVerificationCode = { verificationCode ->
                if (viewModel.isSignedIn) {
                    viewModel.reauthenticateWithVerificationCode(verificationCode)
                } else {
                    viewModel.signInWithVerificationCode(verificationCode)
                }
            },
            onSendAgain = {
                viewModel.verifyPhoneNumber(
                    phoneNumber = phoneNumber,
                    activity = activity
                )
            },
            isLoading = uiState.isLoading
        )
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when(event) {
                is AuthEvent.ShowToast -> showToast(context, event.message)
                is AuthEvent.NavigateBack -> onNavigateBack()
                else -> Unit
            }
        }
    }
}