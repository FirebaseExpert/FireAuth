package com.firebase_expert.fireauth.android.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.scene.rememberSceneSetupNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.firebase_expert.fireauth.android.ui.screen.auth.AuthState
import com.firebase_expert.fireauth.android.ui.screen.auth.auth_entry.AuthEntryScreen
import com.firebase_expert.fireauth.android.ui.screen.auth.verify_code.VerifyCodeScreen
import com.firebase_expert.fireauth.android.ui.screen.main.MainScreen
import com.firebase_expert.fireauth.android.ui.screen.splash.SplashScreen

@Composable
fun FireAuthNavDisplay(
    authState: AuthState,
    emailLink: String?
) {
    val backStack = rememberNavBackStack(Screen.Splash)
    val currentScreen by remember(backStack) {
        derivedStateOf {
            backStack.lastOrNull()
        }
    }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Loading -> Unit
            is AuthState.SignedIn -> {
                if (currentScreen != Screen.Main) {
                    backStack.onClearAndNavigate(Screen.Main)
                }
            }
            is AuthState.SignedOut -> {
                if (currentScreen != Screen.AuthEntry && currentScreen !is Screen.VerifyCode) {
                    backStack.onClearAndNavigate(Screen.AuthEntry)
                }
            }
        }
    }

    NavDisplay(
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        backStack = backStack,
        onBack = {
            backStack.removeLastOrNull()
        },
        entryProvider = entryProvider {
            entry<Screen.Splash> {
                SplashScreen()
            }
            entry<Screen.AuthEntry> {
                AuthEntryScreen(
                    emailLink = emailLink,
                    onNavigate = backStack::add
                )
            }
            entry<Screen.VerifyCode> { screen ->
                VerifyCodeScreen(
                    phoneNumber = screen.phoneNumber,
                    onNavigateBack = backStack::removeLastOrNull
                )
            }
            entry<Screen.Main> {
                MainScreen(
                    emailLink = emailLink,
                    onNavigate = backStack::add
                )
            }
        }
    )
}

fun <T : NavKey> NavBackStack<T>.onClearAndNavigate(screen: T) {
    clear()
    add(screen)
}