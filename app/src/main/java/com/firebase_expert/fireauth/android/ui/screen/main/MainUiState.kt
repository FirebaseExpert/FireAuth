package com.firebase_expert.fireauth.android.ui.screen.main

import com.firebase_expert.fireauth.android.domain.model.App

data class MainUiState(
    val isGettingApps: Boolean = false,
    val apps: List<App>? = null
) {
    val isLoading get() = isGettingApps
}