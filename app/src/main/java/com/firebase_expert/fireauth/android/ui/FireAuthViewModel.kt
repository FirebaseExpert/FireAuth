package com.firebase_expert.fireauth.android.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase_expert.fireauth.android.domain.repository.AuthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class FireAuthViewModel(
    private val authRepo: AuthRepository
) : ViewModel() {
    val authState get() = authRepo.getAuthState().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = FireAuthState.Loading
    )
}