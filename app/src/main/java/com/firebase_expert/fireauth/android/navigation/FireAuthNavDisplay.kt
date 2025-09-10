package com.firebase_expert.fireauth.android.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.firebase_expert.fireauth.android.ui.FireAuthState
import com.firebase_expert.fireauth.android.ui.screen.auth.auth_entry.AuthEntryScreen
import com.firebase_expert.fireauth.android.ui.screen.auth.verify_code.VerifyCodeScreen
import com.firebase_expert.fireauth.android.ui.screen.main.MainScreen
import com.firebase_expert.fireauth.android.ui.screen.splash.SplashScreen

@Composable
fun FireAuthNavDisplay(
    authState: FireAuthState,
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
            is FireAuthState.Loading -> Unit
            is FireAuthState.SignedIn -> {
                if (currentScreen != Screen.Main) {
                    backStack.onClearAndNavigate(Screen.Main)
                }
            }
            is FireAuthState.SignedOut -> {
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

fun NavBackStack.onClearAndNavigate(screen: Screen) {
    clear()
    add(screen)
}