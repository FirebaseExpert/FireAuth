package com.firebase_expert.fireauth.android.ui.screen.main

import com.firebase_expert.fireauth.android.domain.model.SaleInfo

data class MainUiState(
    val isGettingSaleInfo: Boolean = false,
    val saleInfo: SaleInfo? = null
) {
    val isLoading get() = isGettingSaleInfo
}