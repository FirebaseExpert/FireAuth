package com.firebase_expert.fireauth.android.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.firebase_expert.fireauth.android.navigation.FireAuthNavDisplay
import com.firebase_expert.fireauth.android.ui.theme.FireAuthManagerTheme
import org.koin.androidx.compose.koinViewModel

class FireAuthActivity : ComponentActivity() {
    var currentEmailLink by mutableStateOf<String?>(null)
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        handleIntent(intent)

        setContent {
            val viewModel = koinViewModel<FireAuthViewModel>()
            val authState by viewModel.authState.collectAsStateWithLifecycle()

            FireAuthManagerTheme {
                FireAuthNavDisplay(
                    authState = authState,
                    emailLink = currentEmailLink
                )
            }
        }
    }

    private fun handleIntent(intent: Intent?) {
        val emailLink = intent?.data
        if (emailLink != null) {
            currentEmailLink = emailLink.toString()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    fun clearEmailLink() {
        currentEmailLink = null
        intent.data = null
    }
}