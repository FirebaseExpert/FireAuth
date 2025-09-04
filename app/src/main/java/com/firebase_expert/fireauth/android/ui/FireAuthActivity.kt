package com.firebase_expert.fireauth.android.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.firebase_expert.fireauth.android.navigation.FireAuthNavDisplay
import com.firebase_expert.fireauth.android.ui.theme.FireAppManagerTheme
import org.koin.androidx.compose.koinViewModel

class FireAuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel = koinViewModel<FireAuthViewModel>()
            val authState by viewModel.authState.collectAsStateWithLifecycle()

            FireAppManagerTheme {
                FireAuthNavDisplay(
                    authState = authState
                )
            }
        }
    }
}